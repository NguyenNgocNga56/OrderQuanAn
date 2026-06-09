// ============================================================
// STATE trang cart
// ============================================================
let _khachHangId  = null;   // ID khách tìm được qua SĐT
let _sdtDaNhap    = false;  // true nếu nhập SĐT nhưng không tìm thấy → block đặt
let _giamGia      = 0;      // số tiền giảm (đã tính)
let _kmId         = null;   // ID khuyến mãi đang chọn
let _allKm        = [];     // danh sách KM tải từ API
let _loaiDon      = 'TAI_CHO'; // loại đơn: 'TAI_CHO' hoặc 'MANG_VE'
let _sdtDebounce  = null;   // debounce timer cho input SĐT

// ============================================================
// INIT
// ============================================================
document.addEventListener('DOMContentLoaded', () => {
    renderCartPage();
    loadKhuyenMaiTheoSdt(null); // load KM không cần SĐT lúc đầu
    loadBanList();
    chonLoaiDon('TAI_CHO'); // mặc định Tại chỗ
});

// ============================================================
// LOẠI ĐƠN: TẠI CHỖ / MANG VỀ
// ============================================================
function chonLoaiDon(loai) {
    _loaiDon = loai;

    const btnTaiCho    = document.getElementById('btnTaiCho');
    const btnMangVe    = document.getElementById('btnMangVe');
    const groupChonBan = document.getElementById('groupChonBan');

    if (loai === 'TAI_CHO') {
        // Nút Tại chỗ: active
        btnTaiCho.style.background    = 'var(--accent)';
        btnTaiCho.style.color         = '#fff';
        btnTaiCho.style.borderColor   = 'var(--accent)';
        // Nút Mang về: inactive
        btnMangVe.style.background    = 'var(--surface2)';
        btnMangVe.style.color         = 'var(--text)';
        btnMangVe.style.borderColor   = 'var(--border)';
        // Hiện phần chọn bàn
        groupChonBan.style.display    = '';
    } else {
        // Nút Mang về: active
        btnMangVe.style.background    = 'var(--accent)';
        btnMangVe.style.color         = '#fff';
        btnMangVe.style.borderColor   = 'var(--accent)';
        // Nút Tại chỗ: inactive
        btnTaiCho.style.background    = 'var(--surface2)';
        btnTaiCho.style.color         = 'var(--text)';
        btnTaiCho.style.borderColor   = 'var(--border)';
        // Ẩn phần chọn bàn
        groupChonBan.style.display    = 'none';
    }
}

// ============================================================
// LOAD DANH SÁCH BÀN
// ============================================================
async function loadBanList() {
    try {
        const res   = await fetch('/api/ban');
        const json  = await res.json();
        const dsBan = json.data || [];
        const sel   = document.getElementById('selBan');
        if (!sel) return;

        // Chỉ lấy bàn TRONG (còn trống)
        const banTrong = dsBan.filter(ban =>
            (ban.trangThai || '').toUpperCase() === 'TRONG'
        );

        sel.innerHTML = '<option value="">-- Chọn bàn --</option>';

        if (!banTrong.length) {
            sel.innerHTML = '<option value="" disabled>Hiện không có bàn trống</option>';
            return;
        }

        banTrong.forEach(ban => {
            const opt = document.createElement('option');
            opt.value       = ban.banID;   // ← FIX: đúng tên field (chữ hoa ID)
            opt.textContent = (ban.tenBan ?? ('Bàn ' + ban.banID))
                            + (ban.soChoNgoi ? ` (${ban.soChoNgoi} chỗ)` : '');
            sel.appendChild(opt);
        });
    } catch (e) {
        console.warn('Không tải được danh sách bàn:', e);
    }
}

// ============================================================
// RENDER GIỎ HÀNG
// ============================================================
function renderCartPage() {
    const items     = Cart.getAll();
    const container = document.getElementById('cartPageItems');
    const sumMon    = document.getElementById('sumMon');
    const sumTam    = document.getElementById('sumTam');

    const subtotal = Cart.total();
    const count    = items.reduce((s, i) => s + i.qty, 0);
    sumMon.textContent = count + ' món';
    sumTam.textContent = fmtVND(subtotal);
    updateTotal();

    if (!items.length) {
        container.innerHTML = `<div style="text-align:center;padding:4rem;color:var(--text-muted);">
            <div style="font-size:3rem;margin-bottom:1rem;">🛒</div>
            <p>Giỏ hàng trống. <a href="/khachhang.html#menu-section" style="color:var(--accent);">Chọn món ngay →</a></p>
        </div>`;
        return;
    }

    container.innerHTML = `
        <table class="cart-table">
            <thead><tr>
                <th colspan="2">Món ăn</th>
                <th style="text-align:center">Số lượng</th>
                <th style="text-align:right">Đơn giá</th>
                <th style="text-align:right">Thành tiền</th>
                <th></th>
            </tr></thead>
            <tbody>
            ${items.map(i => `<tr>
                <td style="width:52px;padding-right:0;">
                    ${i.hinhAnh
                        ? `<img src="${i.hinhAnh}" style="width:44px;height:44px;border-radius:8px;object-fit:cover;" onerror="this.style.display='none'">`
                        : `<span style="font-size:1.8rem;">🍽</span>`}
                </td>
                <td><strong>${i.tenMon}${i.size ? ` <span class="cart-size-tag">${i.size}</span>` : ''}</strong></td>
                <td style="text-align:center;">
                    <div style="display:flex;align-items:center;justify-content:center;gap:8px;">
                        <button class="qty-btn" onclick="Cart.setQty(${i.monID},${i.qty - 1});renderCartPage()">−</button>
                        <span>${i.qty}</span>
                        <button class="qty-btn" onclick="Cart.setQty(${i.monID},${i.qty + 1});renderCartPage()">+</button>
                    </div>
                </td>
                <td style="text-align:right;">${fmtVND(i.gia)}</td>
                <td style="text-align:right;color:var(--accent);font-weight:700;">${fmtVND(i.gia * i.qty)}</td>
                <td><button style="background:none;border:none;color:var(--text-muted);cursor:pointer;font-size:1rem;" onclick="Cart.remove(${i.monID});renderCartPage()">✕</button></td>
            </tr>`).join('')}
            </tbody>
        </table>
        <div style="margin-top:12px;">
            <button onclick="Cart.clear();renderCartPage();" style="background:none;border:1px solid var(--border);color:var(--text-muted);padding:8px 16px;border-radius:8px;cursor:pointer;font-size:0.88rem;">
                 Xoá tất cả
            </button>
        </div>`;
}

// ============================================================
// SĐT INPUT → AUTO CHECK (DEBOUNCE 600ms)
// ============================================================
function onSdtInput() {
    // Reset trạng thái khách hàng ngay khi gõ
    _khachHangId = null;
    _sdtDaNhap   = false;
    document.getElementById('khachInfo').style.display     = 'none';
    document.getElementById('khachNotFound').style.display = 'none';

    // Reset KM về trống trong khi chờ
    clearTimeout(_sdtDebounce);

    const sdt = document.getElementById('inputSdt').value.trim();

    // Nếu rỗng → load KM không cần SĐT
    if (!sdt) {
        applyKhuyenMai();   // reset giảm giá
        loadKhuyenMaiTheoSdt(null);
        return;
    }

    // Chờ 600ms sau khi ngừng gõ rồi mới check
    _sdtDebounce = setTimeout(() => autoCheckSdt(sdt), 600);
}

async function autoCheckSdt(sdt) {
    // Chỉ xử lý khi đủ 10 số hợp lệ
    const valid = /^0[0-9]{9}$/.test(sdt);

    const spinner = document.getElementById('sdtSpinner');
    spinner.style.display = 'inline';

    try {
        // 1. Tra cứu thông tin khách hàng
        if (valid) {
            try {
                const res = await fetch(`/api/khachhang/sdt/${encodeURIComponent(sdt)}`);
                if (res.ok) {
                    const resp = await res.json();
                    // API trả về ApiResponse wrapper { success, message, data: {...} }
                    const kh = resp.data ?? resp;
                    _khachHangId = kh.id ?? kh.khachHangID;
                    _sdtDaNhap   = false;

                    const hangMap = { DONG:'🥉 Đồng', BAC:'🥈 Bạc', VANG:'🥇 Vàng', KIM_CUONG:'💎 Kim Cương', THUONG:'👤 Thường', THANH_VIEN:'⭐ Thành viên', VIP:'💎 VIP' };
                    const giam    = { DONG:0, THUONG:0, BAC:5, THANH_VIEN:5, VANG:10, VIP:10, KIM_CUONG:15 }[kh.loaiKhachHang] || 0;

                    document.getElementById('khachHang').textContent  = hangMap[kh.loaiKhachHang] || '👤';
                    document.getElementById('khachTen').textContent   = kh.hoTen + ` — ${kh.diemTichLuy || 0} điểm`;
                    document.getElementById('khachLoai').textContent  = giam > 0 ? `Ưu đãi thành viên: -${giam}% mỗi đơn` : 'Hạng Đồng – tích điểm để lên hạng';
                    document.getElementById('khachInfo').style.display     = 'block';
                    document.getElementById('khachNotFound').style.display = 'none';
                } else {
                    _khachHangId = null;
                    _sdtDaNhap   = true;
                    document.getElementById('khachInfo').style.display     = 'none';
                    document.getElementById('khachNotFound').style.display = 'block';
                }
            } catch {
                // bỏ qua lỗi tra cứu, vẫn tiếp tục load KM
            }
        }

        // 2. Load KM khả dụng theo SĐT (gọi API kha-dung)
        await loadKhuyenMaiTheoSdt(valid ? sdt : null);

    } finally {
        spinner.style.display = 'none';
    }
}

// ============================================================
// LOAD KHUYẾN MÃI KHẢ DỤNG THEO SĐT + TỔNG TIỀN
// ============================================================
async function loadKhuyenMaiTheoSdt(sdt) {
    const sel     = document.getElementById('selKhuyenMai');
    const badge   = document.getElementById('kmLoadingBadge');
    const subtotal = Cart.total();

    if (badge) badge.style.display = 'inline';

    // Ghi nhớ lựa chọn hiện tại (nếu có)
    const prevKmId = sel.value;

    // Reset select
    sel.innerHTML = '<option value="">-- Không dùng khuyến mãi --</option>';
    _kmId    = null;
    _giamGia = 0;
    updateTotal();

    try {
        let url = `/api/khuyenmai/kha-dung?tongTien=${subtotal}`;
        if (sdt) url += `&sdt=${encodeURIComponent(sdt)}`;

        const res  = await fetch(url);
        const body = await res.json();
        const kmList = Array.isArray(body) ? body : (body.data || []);

        if (kmList.length === 0 && sdt) {
            const opt = document.createElement('option');
            opt.value = '';
            opt.disabled = true;
            opt.textContent = '— Không có khuyến mãi khả dụng —';
            sel.appendChild(opt);
        }

        kmList.forEach(km => {
            const gia   = km.giaTri ?? km.giaTriKhuyenMai ?? 0;
            const loai  = km.loaiKhuyenMai;
            const label = loai === 'PHAN_TRAM' ? `Giảm ${gia}%` : `Giảm ${fmtVND(gia)}`;
            const o = document.createElement('option');
            o.value        = km.khuyenMaiID;
            o.dataset.loai = loai;
            o.dataset.gia  = gia;
            o.textContent  = `${km.tenKhuyenMai || 'Ưu đãi'} — ${label}`;
            sel.appendChild(o);
        });

        // Khôi phục lựa chọn cũ nếu còn tồn tại
        if (prevKmId) {
            const stillExists = [...sel.options].some(o => o.value === prevKmId);
            if (stillExists) {
                sel.value = prevKmId;
            }
        }

    } catch (e) {
        console.warn('Không tải được KM:', e);
    } finally {
        if (badge) badge.style.display = 'none';
        applyKhuyenMai();
    }
}

// ============================================================
// ÁP DỤNG KHUYẾN MÃI → CẬP NHẬT TỔNG
// ============================================================
function applyKhuyenMai() {
    const sel      = document.getElementById('selKhuyenMai');
    const kmInfo   = document.getElementById('kmInfo');
    const kmInfoTx = document.getElementById('kmInfoText');
    const rowGiam  = document.getElementById('rowGiamGia');
    const sumGiam  = document.getElementById('sumGiam');
    const subtotal = Cart.total();

    if (!sel.value) {
        _giamGia = 0;
        _kmId    = null;
        kmInfo.style.display  = 'none';
        rowGiam.style.display = 'none';
        updateTotal();
        return;
    }

    const opt  = sel.options[sel.selectedIndex];
    const loai = opt.dataset.loai;
    const gia  = parseFloat(opt.dataset.gia) || 0;

    _kmId = parseInt(sel.value);

    if (loai === 'PHAN_TRAM') {
        _giamGia = Math.round(subtotal * gia / 100);
        kmInfoTx.textContent = `Giảm ${gia}% → Tiết kiệm ${fmtVND(_giamGia)}`;
    } else {
        _giamGia = Math.min(gia, subtotal);
        kmInfoTx.textContent = `Giảm trực tiếp ${fmtVND(_giamGia)}`;
    }

    kmInfo.style.display  = 'block';
    rowGiam.style.display = '';
    sumGiam.textContent   = '−' + fmtVND(_giamGia);
    updateTotal();
}

function updateTotal() {
    const subtotal = Cart.total();
    document.getElementById('sumTotal').textContent = fmtVND(Math.max(0, subtotal - _giamGia));
}

// ============================================================
// ĐẶT MÓN → POST /orders
// ============================================================
async function datMon() {
    const items = Cart.getAll();
    if (!items.length) { alert('Giỏ hàng trống!'); return; }

    const sdtNhap = document.getElementById('inputSdt').value.trim();
    if (sdtNhap && !/^0[0-9]{9}$/.test(sdtNhap)) {
        alert('Số điện thoại không hợp lệ. Vui lòng nhập đúng 10 số, bắt đầu bằng 0.');
        return;
    }

    const phuongThuc = document.getElementById('selPhuongThuc')?.value || 'TIEN_MAT';
    const banId      = _loaiDon === 'TAI_CHO'
        ? (document.getElementById('selBan')?.value || null)
        : null;

    if (_loaiDon === 'TAI_CHO' && !banId) {
        alert('Vui lòng chọn bàn trước khi đặt món!');
        return;
    }

    const payload = {
        khachHangId:  _khachHangId || null,
        sdtKhachHang: sdtNhap || null,
        nhanVienId:   null,
        khuyenMaiId:  _kmId || null,
        loaiDon:      _loaiDon,
        banId:        banId ? Number(banId) : null,
        items: items.map(i => ({ monId: i.monID, soLuong: i.qty }))
    };

    const btn = document.querySelector('button.btn-primary[onclick="datMon()"]');
    if (btn) { btn.disabled = true; btn.textContent = 'Đang đặt...'; }

    try {
        const res = await fetch('/orders', {
            method:  'POST',
            headers: { 'Content-Type': 'application/json' },
            body:    JSON.stringify(payload)
        });
        if (!res.ok) {
            const err = await res.json().catch(() => ({}));
            throw new Error(err.message || 'Lỗi server');
        }
        const order = await res.json();

        const ttRes = await fetch(`/api/thanhtoan/donhang/${order.donHangID}`, {
            method:  'POST',
            headers: { 'Content-Type': 'application/json' },
            body:    JSON.stringify({ phuongThuc, soTien: order.tongTien })
        });
        if (!ttRes.ok) {
            const err = await ttRes.json().catch(() => ({}));
            throw new Error(err.message || 'Thanh toán thất bại');
        }
        const payment = await ttRes.json();

        Cart.clear();
        _giamGia = 0; _kmId = null; _khachHangId = null; _sdtDaNhap = false;

        // Reset input SĐT và KM
        const sdtInput = document.getElementById('inputSdt');
        if (sdtInput) sdtInput.value = '';
        document.getElementById('khachInfo').style.display     = 'none';
        document.getElementById('khachNotFound').style.display = 'none';

        renderCartPage();
        loadKhuyenMaiTheoSdt(null); // reset danh sách KM
        renderSuccess(order, payment.data);
        openModal('successModal');
    } catch (e) {
        alert('Lỗi khi đặt hàng: ' + e.message + '\n(Kiểm tra server đang chạy!)');
    } finally {
        if (btn) { btn.disabled = false; btn.textContent = 'Đặt món ngay'; }
    }
}


// MODAL SUCCESS

function renderSuccess(order, payment) {
    const body = document.getElementById('successBody');
    const rows = (order.chiTiet || []).map(ct => `
        <tr>
            <td style="padding:6px 0;">${ct.tenMon}</td>
            <td style="text-align:center;">x${ct.soLuong}</td>
            <td style="text-align:right;color:var(--accent);font-weight:700;">${fmtVND(ct.thanhTien)}</td>
        </tr>`).join('');

    const giamRow = _giamGia > 0
        ? `<tr><td colspan="2" style="color:var(--accent);">Giảm giá</td><td style="text-align:right;color:var(--accent);">−${fmtVND(_giamGia)}</td></tr>`
        : '';

    const diemMessage = payment?.diemCong > 0
        ? `<p style="text-align:center;margin-top:10px;font-size:0.85rem;color:var(--text-muted);">Đã cộng ${payment.diemCong} điểm sau khi thanh toán.</p>`
        : '';

    body.innerHTML = `
        <p style="text-align:center;margin-bottom:12px;">Mã đơn: <strong style="color:var(--accent);font-size:1.1rem;">#${order.donHangID}</strong></p>
        <table style="width:100%;border-collapse:collapse;font-size:0.9rem;">
            <thead><tr style="border-bottom:1px solid var(--border);">
                <th style="text-align:left;padding:4px 0;">Món</th>
                <th style="text-align:center;">SL</th>
                <th style="text-align:right;">Thành tiền</th>
            </tr></thead>
            <tbody>${rows}${giamRow}</tbody>
        </table>
        <div style="border-top:2px solid var(--border);margin-top:12px;padding-top:12px;display:flex;justify-content:space-between;align-items:center;">
            <strong>Tổng cộng:</strong>
            <span style="font-size:1.3rem;font-weight:800;color:var(--accent);">${fmtVND(order.tongTien)}</span>
        </div>
        ${_khachHangId ? '<p style="text-align:center;margin-top:10px;font-size:0.85rem;color:var(--text-muted);">✨ Điểm tích lũy đã được cộng vào tài khoản của bạn!</p>' : ''}
        ${diemMessage}`;

    // Sinh QR thanh toán
    const qrData = encodeURIComponent(`QuanAn67 - Don #${order.donHangID} - Thanh toan: ${order.tongTien}VND`);
    document.getElementById('qrImg').src   = `https://api.qrserver.com/v1/create-qr-code/?size=170x170&data=${qrData}&color=8b0000&bgcolor=ffffff&margin=8`;
    document.getElementById('qrLabel').textContent = `Đơn #${order.donHangID} · ${fmtVND(order.tongTien)}`;
}
