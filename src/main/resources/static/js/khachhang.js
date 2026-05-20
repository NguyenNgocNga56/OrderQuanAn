// CART (localStorage)
const Cart = {
    key: 'qap_cart',
    getAll() { try { return JSON.parse(localStorage.getItem(this.key)) || []; } catch { return []; } },
    save(items) { localStorage.setItem(this.key, JSON.stringify(items)); updateCartBadge(); },
    add(item) {
        const items = this.getAll();
        const ex = items.find(i => i.monID === item.monID);
        if (ex) ex.qty += 1;
        else items.push({ ...item, qty: 1 });
        this.save(items);
    },
    remove(monID) { this.save(this.getAll().filter(i => i.monID !== monID)); },
    setQty(monID, qty) {
        if (qty <= 0) { this.remove(monID); return; }
        const items = this.getAll();
        const ex = items.find(i => i.monID === monID);
        if (ex) { ex.qty = qty; this.save(items); }
    },
    clear() { localStorage.removeItem(this.key); updateCartBadge(); },
    total() { return this.getAll().reduce((s, i) => s + i.gia * i.qty, 0); },
    count() { return this.getAll().reduce((s, i) => s + i.qty, 0); }
};

function updateCartBadge() {
    const b = document.getElementById('cartBadge');
    if (b) b.textContent = Cart.count();
}

<<<<<<< HEAD
=======
// ============================================================
>>>>>>> 854df00051b53759b0398cd5ff6d72d04bf962ec
// FORMAT TIỀN
function fmtVND(n) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(n || 0);
}

// MODAL
function openModal(id) {
    document.getElementById(id).classList.add('active');
    document.body.style.overflow = 'hidden';
}
function closeModal(id) {
    document.getElementById(id).classList.remove('active');
    document.body.style.overflow = '';
}
function closeModalOut(event, id) {
    if (event.target === event.currentTarget) closeModal(id);
}

// CART PANEL (slide)
function toggleCart() {
    const panel   = document.getElementById('cartPanel');
    const overlay = document.getElementById('cartOverlay');
    if (!panel) return;
    const open = panel.classList.toggle('open');
    if (overlay) overlay.classList.toggle('open', open);
    if (open) renderCartPanel();
}

function renderCartPanel() {
    const container = document.getElementById('cartItems');
    if (!container) return;
    const items = Cart.getAll();
    const totalEl = document.getElementById('cartTotal');
    if (totalEl) totalEl.textContent = fmtVND(Cart.total());

    if (!items.length) {
        container.innerHTML = '<div class="cart-empty">Giỏ hàng trống</div>';
        return;
    }
    container.innerHTML = items.map(i => `
        <div class="cart-item">
            <div class="cart-item-info">
                ${i.hinhAnh ? `<img src="${i.hinhAnh}" onerror="this.style.display='none'">` : '<span class="cart-item-emoji">🍽</span>'}
                <div>
                    <div class="cart-item-name">${i.tenMon}</div>
                    <div class="cart-item-price">${fmtVND(i.gia)}</div>
                </div>
            </div>
            <div class="cart-item-qty">
                <button class="qty-btn" onclick="Cart.setQty(${i.monID},${i.qty-1});renderCartPanel()">−</button>
                <span>${i.qty}</span>
                <button class="qty-btn" onclick="Cart.setQty(${i.monID},${i.qty+1});renderCartPanel()">+</button>
            </div>
        </div>`).join('');
}

// LOAD KHUYẾN MÃI
async function loadKhuyenMai() {
    const grid = document.getElementById('kmGrid');
    if (!grid) return;
    try {
        const kms = await fetch('/api/khuyenmai').then(r => r.json());
        if (!kms.length) {
            grid.innerHTML = '<p style="text-align:center;color:var(--text-muted);">Hiện chưa có khuyến mãi nào.</p>';
            return;
        }
        grid.innerHTML = kms.map(km => {
            const label = km.loaiKhuyenMai === 'PHAN_TRAM'
                ? `Giảm ${km.giaTriKhuyenMai}%`
                : `Giảm ${fmtVND(km.giaTriKhuyenMai)}`;
            const now = new Date();
            const hetHan = km.ngayKetThuc ? new Date(km.ngayKetThuc) : null;
            const con = !hetHan || hetHan >= now;
            return `
            <div class="km-card ${con ? '' : 'km-expired'}">
                <div class="km-badge">${label}</div>
                <div class="km-title">${km.tenKhuyenMai || 'Ưu đãi đặc biệt'}</div>
                <div class="km-desc">${km.moTa || ''}</div>
                <div class="km-deadline">
                    ${hetHan ? ` HSD: ${hetHan.toLocaleDateString('vi-VN')}` : ' Không giới hạn'}
                </div>
                <div class="km-status ${con ? 'km-con' : 'km-het'}">${con ? 'Còn hiệu lực' : ' Đã hết hạn'}</div>
            </div>`;
        }).join('');
    } catch {
        grid.innerHTML = '<p style="text-align:center;color:var(--text-muted);">Không tải được khuyến mãi.</p>';
    }
}

// LOAD THỰC ĐƠN
let currentTab = 'doan';

function switchTab(tab, btn) {
    currentTab = tab;
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    loadMonAn(tab);
}

async function loadMonAn(tab) {
    const grid = document.getElementById('monGrid');
    if (!grid) return;
    grid.innerHTML = '<div class="km-loading"> Đang tải...</div>';
    try {
        const endpoint = tab === 'doan' ? '/api/monan' : '/api/douong';
        const items = await fetch(endpoint).then(r => r.json());
        if (!items.length) {
            grid.innerHTML = '<p style="text-align:center;color:var(--text-muted);">Không có món nào.</p>';
            return;
        }
        grid.innerHTML = items.map(mon => {
            const gia = mon.gia || mon.giaTien || mon.giaMon || 0;
            const ten = mon.tenMon || mon.ten || '';
            const available = !mon.trangThai || mon.trangThai === 'AVAILABLE' || mon.trangThai === 'CON_HANG';
            return `
            <div class="mon-card">
                <div class="mon-img">
                    ${mon.hinhAnh
                        ? `<img src="${mon.hinhAnh}" alt="${ten}" onerror="this.outerHTML='<span style=font-size:3rem>🍽</span>'">`
                        : '<span style="font-size:3rem;">🍽</span>'}
                </div>
                <div class="mon-body">
                    <div class="mon-ten">${ten}</div>
                    <div class="mon-mota">${mon.moTa || ''}</div>
                    <div class="mon-footer">
                        <span class="mon-gia">${fmtVND(gia)}</span>
                        ${available
                            ? `<button class="btn-them" onclick="themVaoGio(${mon.monID},'${ten}',${gia},'${mon.hinhAnh||''}')">+ Thêm</button>`
                            : `<span style="color:var(--red);font-size:0.82rem;">Hết món</span>`}
                    </div>
                </div>
            </div>`;
        }).join('');
    } catch {
        grid.innerHTML = '<p style="text-align:center;color:var(--text-muted);">Không tải được thực đơn.</p>';
    }
}

function themVaoGio(monID, tenMon, gia, hinhAnh) {
    Cart.add({ monID, tenMon, gia, hinhAnh });
    renderCartPanel();
    // Mini toast
    const t = document.createElement('div');
    t.className = 'mini-toast';
    t.textContent = ` Đã thêm: ${tenMon}`;
    document.body.appendChild(t);
    setTimeout(() => t.classList.add('show'), 10);
    setTimeout(() => { t.classList.remove('show'); setTimeout(() => t.remove(), 300); }, 2000);
}

// ĐĂNG KÝ KHÁCH HÀNG → POST /api/khachhang
async function dangKy() {
    const hoTen  = document.getElementById('regHoTen').value.trim();
    const sdt    = document.getElementById('regSdt').value.trim();
    const email  = document.getElementById('regEmail').value.trim();
    const diaChi = document.getElementById('regDiaChi').value.trim();
    const msg    = document.getElementById('regMsg');

    if (!hoTen || !sdt) {
        setMsg(msg, 'Vui lòng nhập họ tên và số điện thoại.', 'error'); return;
    }

    try {
        const res = await fetch('/api/khachhang', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ hoTen, sdt, email, diaChi })
        });
        if (!res.ok) throw new Error();
        setMsg(msg, ' Đăng ký thành công! Chào mừng bạn.', 'success');
        document.getElementById('regHoTen').value = '';
        document.getElementById('regSdt').value   = '';
        document.getElementById('regEmail').value = '';
        document.getElementById('regDiaChi').value= '';
    } catch {
        setMsg(msg, 'Đăng ký thất bại. Vui lòng thử lại.', 'error');
    }
}

// TRA CỨU ĐIỂM → GET /api/khachhang?sdt=xxx
<<<<<<< HEAD
=======
// ============================================================
>>>>>>> 854df00051b53759b0398cd5ff6d72d04bf962ec
async function traCuu() {
    const sdt    = document.getElementById('tcSdt').value.trim();
    const msg    = document.getElementById('tcMsg');
    const result = document.getElementById('tcResult');
    if (!sdt) { setMsg(msg, 'Vui lòng nhập số điện thoại.', 'error'); return; }

    try {
        const data = await fetch(`/api/khachhang/sdt/${sdt}`).then(r => {
            if (!r.ok) throw new Error();
            return r.json();
        });

        document.getElementById('tcTen').textContent  = data.hoTen || '';
        document.getElementById('tcDiem').textContent = data.diemTichLuy || 0;

        const loai = data.loaiKhachHang || 'DONG';
        const hangMap = { DONG: '🥉 Đồng', BAC: '🥈 Bạc', VANG: '🥇 Vàng', KIM_CUONG: '💎 Kim Cương' };
        const nextMap = { DONG: { next: 'Bạc', can: 100 }, BAC: { next: 'Vàng', can: 500 }, VANG: { next: 'Kim Cương', can: 1000 }, KIM_CUONG: { next: null, can: null } };
        document.getElementById('tcHang').textContent = hangMap[loai] || '🥉 Đồng';
        document.getElementById('tcLoai').textContent = hangMap[loai] || 'Đồng';

        const diem = data.diemTichLuy || 0;
        const next = nextMap[loai];
        if (next.next) {
            const pct = Math.min((diem / next.can) * 100, 100);
            document.getElementById('tcProgressFill').style.width = pct + '%';
            document.getElementById('tcProgressLabel').textContent = `Còn ${next.can - diem} điểm lên hạng ${next.next}`;
        } else {
            document.getElementById('tcProgressFill').style.width = '100%';
            document.getElementById('tcProgressLabel').textContent = ' Hạng cao nhất!';
        }

        setMsg(msg, '', '');
        result.style.display = 'block';
    } catch {
        result.style.display = 'none';
        setMsg(msg, 'Không tìm thấy số điện thoại này.', 'error');
    }
}

function setMsg(el, text, type) {
    el.textContent = text;
    el.className   = 'form-msg ' + type;
    el.style.display = text ? 'block' : 'none';
}

// INIT
document.addEventListener('DOMContentLoaded', () => {
    updateCartBadge();
    loadKhuyenMai();
    loadMonAn('doan');
});
