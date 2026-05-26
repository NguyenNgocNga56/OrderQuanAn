// Render giỏ hàng trên trang cart.html
document.addEventListener('DOMContentLoaded', () => {
    renderCartPage();
    loadSelects();
});

function renderCartPage() {
    const items = Cart.getAll();
    const container = document.getElementById('cartPageItems');
    const sumMon    = document.getElementById('sumMon');
    const sumTam    = document.getElementById('sumTam');
    const sumTotal  = document.getElementById('sumTotal');

    const total = Cart.total();
    const count = items.reduce((s, i) => s + i.qty, 0);
    sumMon.textContent   = count + ' món';
    sumTam.textContent   = fmtVND(total);
    sumTotal.textContent = fmtVND(total);

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
                <td><strong>${i.tenMon}</strong></td>
                <td style="text-align:center;">
                    <div style="display:flex;align-items:center;justify-content:center;gap:8px;">
                        <button class="qty-btn" onclick="Cart.setQty(${i.monID},${i.qty-1});renderCartPage()">−</button>
                        <span>${i.qty}</span>
                        <button class="qty-btn" onclick="Cart.setQty(${i.monID},${i.qty+1});renderCartPage()">+</button>
                    </div>
                </td>
                <td style="text-align:right;">${fmtVND(i.gia)}</td>
                <td style="text-align:right;color:var(--accent);font-weight:700;">${fmtVND(i.gia*i.qty)}</td>
                <td><button style="background:none;border:none;color:var(--text-muted);cursor:pointer;font-size:1rem;" onclick="Cart.remove(${i.monID});renderCartPage()">✕</button></td>
            </tr>`).join('')}
            </tbody>
        </table>
        <div style="margin-top:12px;">
            <button onclick="Cart.clear();renderCartPage();" style="background:none;border:1px solid var(--border);color:var(--text-muted);padding:8px 16px;border-radius:8px;cursor:pointer;font-size:0.88rem;">
                🗑 Xoá tất cả
            </button>
        </div>`;
}

async function loadSelects() {
    const khSel = document.getElementById('selKhachHang');
    const nvSel = document.getElementById('selNhanVien');

    try {
        const [khResponse, nvResponse] = await Promise.all([
            fetch('/api/khachhang').then(r => r.json()),
            fetch('/api/nhanvien').then(r => r.json())
        ]);

        const khs = Array.isArray(khResponse) ? khResponse : (khResponse.data || []);
        const nvs = Array.isArray(nvResponse) ? nvResponse : (nvResponse.data || []);

        khs.forEach(k => {
            const o = document.createElement('option');
            o.value = k.id;
            o.textContent = k.hoTen + (k.sdt ? ` (${k.sdt})` : '');
            khSel.appendChild(o);
        });

        nvs.filter(n => n.trangThai !== false).forEach(n => {
            const o = document.createElement('option');
            o.value = n.id;
            o.textContent = n.hoTen + (n.chucVu ? ` - ${n.chucVu}` : '');
            nvSel.appendChild(o);
        });
    } catch {
        khSel.innerHTML = '<option value="">-- Không tải được khách hàng --</option>';
        nvSel.innerHTML = '<option value="">-- Không tải được nhân viên --</option>';
    }
}

async function datMon() {
    const items = Cart.getAll();
    if (!items.length) { alert('Giỏ hàng trống!'); return; }

    const khId = document.getElementById('selKhachHang').value;
    const nvId = document.getElementById('selNhanVien').value;

    const payload = {
        khachHangId: khId ? parseInt(khId) : null,
        nhanVienId:  nvId ? parseInt(nvId) : null,
        items: items.map(i => ({ monId: i.monID, soLuong: i.qty }))
    };

    try {
        const res = await fetch('/orders', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        if (!res.ok) throw new Error();
        const order = await res.json();
        Cart.clear();
        renderCartPage();
        renderSuccess(order);
        openModal('successModal');
    } catch {
        alert('Lỗi khi đặt hàng. Kiểm tra server đang chạy!');
    }
}

function renderSuccess(order) {
    const body = document.getElementById('successBody');
    const rows = (order.chiTiet || []).map(ct => `
        <tr>
            <td style="padding:6px 0;">${ct.tenMon}</td>
            <td style="text-align:center;">x${ct.soLuong}</td>
            <td style="text-align:right;color:var(--accent);font-weight:700;">${fmtVND(ct.thanhTien)}</td>
        </tr>`).join('');

    body.innerHTML = `
        <p style="text-align:center;margin-bottom:12px;">Mã đơn: <strong style="color:var(--accent);font-size:1.1rem;">#${order.donHangID}</strong></p>
        <table style="width:100%;border-collapse:collapse;font-size:0.9rem;">
            <thead><tr style="border-bottom:1px solid var(--border);">
                <th style="text-align:left;padding:4px 0;">Món</th>
                <th style="text-align:center;">SL</th>
                <th style="text-align:right;">Thành tiền</th>
            </tr></thead>
            <tbody>${rows}</tbody>
        </table>
        <div style="border-top:2px solid var(--border);margin-top:12px;padding-top:12px;display:flex;justify-content:space-between;align-items:center;">
            <strong>Tổng cộng:</strong>
            <span style="font-size:1.3rem;font-weight:800;color:var(--accent);">${fmtVND(order.tongTien)}</span>
        </div>`;
         // THÊM VÀO: sinh QR sau khi render xong
            const qrData  = encodeURIComponent(`QuanAn67 - Don #${order.donHangID} - Thanh toan: ${order.tongTien}VND`);
            document.getElementById('qrImg').src = `https://api.qrserver.com/v1/create-qr-code/?size=170x170&data=${qrData}&color=8b0000&bgcolor=ffffff&margin=8`;
            document.getElementById('qrLabel').textContent = `Đơn #${order.donHangID} · ${fmtVND(order.tongTien)}`;
        }
}