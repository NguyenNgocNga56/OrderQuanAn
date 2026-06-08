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
                    <div class="cart-item-name">${i.tenMon}${i.size ? ` <span class="cart-size-tag">${i.size}</span>` : ''}</div>
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

async function loadKhuyenMai() {
    const grid = document.getElementById('kmGrid');
    if (!grid) return;
    try {
        const res  = await fetch('/api/khuyenmai');
        const body = await res.json();
        const kms = Array.isArray(body) ? body : (body.data || []);

        if (!kms.length) {
            grid.innerHTML = '<p class="km-empty-msg">Hiện chưa có khuyến mãi nào.</p>';
            return;
        }
        grid.innerHTML = kms.map(km => {
            const gia   = km.giaTri ?? km.giaTriKhuyenMai ?? 0;
            const label = km.loaiKhuyenMai === 'PHAN_TRAM'
                ? `Giảm ${gia}%`
                : `Giảm ${fmtVND(gia)}`;
            const now    = new Date();
            const hetHan = km.ngayKetThuc ? new Date(km.ngayKetThuc) : null;
            const con    = !hetHan || hetHan >= now;
            return `
            <div class="km-card ${con ? '' : 'km-expired'}">
                <div class="km-badge">${label}</div>
                <div class="km-title">${km.tenKhuyenMai||'Ưu đãi đặc biệt'}</div>
                <div class="km-desc">${km.moTa||''}</div>
                <div class="km-deadline">
                    ${hetHan ? ` HSD: ${hetHan.toLocaleDateString('vi-VN')}` : '♾ Không giới hạn'}
                </div>
                <div class="km-status ${con ? 'km-con' : 'km-het'}">${con ? ' Còn hiệu lực' : ' Đã hết hạn'}</div>
            </div>`;
        }).join('');
    } catch(e) {
        grid.innerHTML = '<p class="km-empty-msg">Không tải được khuyến mãi.</p>';
    }
}

function initScrollReveal() {
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('revealed');
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.12 });

    document.querySelectorAll('.mon-card').forEach((el, i) => {
        el.style.transitionDelay = `${(i % 4) * 80}ms`;
        observer.observe(el);
    });
}

// LOAD THỰC ĐƠN
let currentTab = 'doan';

function switchTab(tab, btn) {
    currentTab = tab;
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    loadMonAn(tab);
}

// Gom các món đồ uống cùng loaiTen thành 1 card với size picker S/M/L
function groupDoUong(items) {
    const groups = {};
    items.forEach(mon => {
        // loaiTen: tên gốc không kèm suffix size (do backend trả về)
        const key = mon.loaiTen || mon.tenMon;
        if (!groups[key]) groups[key] = [];
        groups[key].push(mon);
    });
    // Sắp xếp mỗi group theo thứ tự S → M → L
    const sizeOrder = { S: 0, M: 1, L: 2 };
    return Object.values(groups).map(group => {
        group.sort((a, b) => (sizeOrder[a.size] ?? 99) - (sizeOrder[b.size] ?? 99));
        return group;
    });
}

async function loadMonAn(tab) {
    const grid = document.getElementById('monGrid');
    if (!grid) return;
    grid.innerHTML = '<div class="km-loading"> Đang tải...</div>';
    try {
        const endpoint = tab === 'doan' ? '/api/monan' : '/api/monan/douong';
        const items = await fetch(endpoint).then(r => r.json());
        if (!items.length) {
            grid.innerHTML = '<p style="text-align:center;color:var(--text-muted);">Không có món nào.</p>';
            return;
        }

        if (tab === 'douong') {
            // Phụ phí size — khớp với enum SizeDoUong backend
            const SIZE_PHU_PHI = { S: 0, M: 5000, L: 10000 };
            const SIZE_LABELS  = ['S', 'M', 'L'];

            grid.innerHTML = items.map(mon => {
                const ten   = mon.tenMon || '';
                const mota  = mon.moTa  || '';
                const giaGoc = mon.gia  || 0;
                const available = !mon.trangThai || mon.trangThai === 'CON_HANG' || mon.trangThai === 'AVAILABLE';
                // uid dùng monID — mỗi món là 1 record riêng, uid unique
                const uid = 'size_' + mon.monID;

                const sizeBtns = SIZE_LABELS.map((s, idx) => {
                    const giaSize = giaGoc + SIZE_PHU_PHI[s];
                    return `<button class="size-btn ${idx===0?'active':''}"
                        onclick="selectSizeFE(this,'${uid}',${mon.monID},${giaSize},'${ten.replace(/'/g,"\\'")}','${(mon.hinhAnh||'').replace(/'/g,"\\'")}','${s}')"
                    >${s}</button>`;
                }).join('');

                // Init state mặc định (size S)
                window._sizeState[uid] = {
                    monID: mon.monID, gia: giaGoc, ten,
                    hinh: mon.hinhAnh||'', size: 'S'
                };

                return `
                <div class="mon-card scroll-reveal">
                    <div class="mon-img">
                        ${mon.hinhAnh
                            ? `<img src="${mon.hinhAnh}" alt="${ten}" onerror="this.outerHTML='<span style=font-size:3rem>🥤</span>'">`
                            : '<span style="font-size:3rem;">🥤</span>'}
                    </div>
                    <div class="mon-body">
                        <div class="mon-ten">${ten}</div>
                        <div class="mon-mota">${mota}</div>
                        <div class="size-row">${sizeBtns}</div>
                        <div class="mon-footer">
                            <span class="mon-gia" id="gia_${uid}">${fmtVND(giaGoc)}</span>
                            ${available
                                ? `<button class="btn-them" onclick="themDoUongVaoGio('${uid}')">+ Thêm</button>`
                                : `<span style="color:var(--red);font-size:0.82rem;">Hết món</span>`}
                        </div>
                    </div>
                </div>`;
            }).join('');
        } else {
            // Đồ ăn: render bình thường
            grid.innerHTML = items.map(mon => {
                const gia = mon.gia || 0;
                const ten = mon.tenMon || '';
                const mota = mon.moTa || '';
                const available = !mon.trangThai || mon.trangThai === 'AVAILABLE' || mon.trangThai === 'CON_HANG';
                return `
                <div class="mon-card scroll-reveal">
                    <div class="mon-img">
                        ${mon.hinhAnh ? `<img src="${mon.hinhAnh}" alt="${ten}" onerror="this.outerHTML='<span style=font-size:3rem>🍽</span>'">` : '<span style="font-size:3rem;">🍽</span>'}
                    </div>
                    <div class="mon-body">
                        <div class="mon-ten">${ten}</div>
                        <div class="mon-mota">${mota}</div>
                        <div class="mon-footer">
                            <span class="mon-gia">${fmtVND(gia)}</span>
                            ${available
                                ? `<button class="btn-them" onclick="themVaoGio(${mon.monID},'${ten}',${gia},'${mon.hinhAnh||''}',null)">+ Thêm</button>`
                                : `<span style="color:var(--red);font-size:0.82rem;">Hết món</span>`}
                        </div>
                    </div>
                </div>`;
            }).join('');
        }

        initScrollReveal();
    } catch(e) {
        grid.innerHTML = '<p style="text-align:center;color:var(--text-muted);">Không tải được thực đơn.</p>';
    }
}

// Lưu state size đang chọn cho từng uid
window._sizeState = {};

function selectSize(btn, uid, monID, gia, ten, hinh, size) {
    btn.closest('.size-row').querySelectorAll('.size-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    window._sizeState[uid] = { monID, gia, ten, hinh, size };
    const giaEl = document.getElementById('gia_' + uid);
    if (giaEl) giaEl.textContent = fmtVND(gia);
}

// Alias cho frontend-side size picker (dùng chung logic)
function selectSizeFE(btn, uid, monID, gia, ten, hinh, size) {
    selectSize(btn, uid, monID, gia, ten, hinh, size);
}

function themDoUongVaoGio(uid) {
    const state = window._sizeState[uid];
    if (!state) return;
    Cart.add({ monID: state.monID, tenMon: state.ten, gia: state.gia, hinhAnh: state.hinh, size: state.size });
    renderCartPanel();
    const t = document.createElement('div');
    t.className = 'mini-toast';
    t.textContent = ` Đã thêm: ${state.ten} (${state.size})`;
    document.body.appendChild(t);
    setTimeout(() => t.classList.add('show'), 10);
    setTimeout(() => { t.classList.remove('show'); setTimeout(() => t.remove(), 300); }, 2000);
}

function themVaoGio(monID, tenMon, gia, hinhAnh, size) {
    Cart.add({ monID, tenMon, gia, hinhAnh, size });
    renderCartPanel();
    const t = document.createElement('div');
    t.className = 'mini-toast';
    t.textContent = ` Đã thêm: ${tenMon}`;
    document.body.appendChild(t);
    setTimeout(() => t.classList.add('show'), 10);
    setTimeout(() => { t.classList.remove('show'); setTimeout(() => t.remove(), 300); }, 2000);
}

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
	const _urlParams   = new URLSearchParams(window.location.search);
	    const _banIdFromQR = _urlParams.get('banId');
	    if (_banIdFromQR) {
	        sessionStorage.setItem('banId', _banIdFromQR);
	        const banner     = document.getElementById('banBanner');
	        const bannerText = document.getElementById('banBannerText');
	        if (banner) banner.style.display = 'block';
	        if (bannerText) bannerText.textContent = 'Bàn #' + _banIdFromQR;
	    }
});

// SCROLL REVEAL
(function() {
    const targets = [
        '.km-card', '.hang-card', '.mon-card',
        '.khuyen-mai-section .section-title',
        '.khuyen-mai-section .section-sub',
        '.menu-section .section-title',
        '.menu-section .section-sub',
        '.menu-tabs',
    ];
    targets.forEach(sel => {
        document.querySelectorAll(sel).forEach(el => el.classList.add('reveal'));
    });

    const io = new IntersectionObserver(function(entries) {
        entries.forEach(function(entry) {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
                io.unobserve(entry.target);
            }
        });
    }, { threshold: 0.12 });

    document.querySelectorAll('.reveal').forEach(el => io.observe(el));
})();