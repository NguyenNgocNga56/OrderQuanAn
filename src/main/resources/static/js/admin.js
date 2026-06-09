// KIỂM TRA SESSION KHI VÀO TRANG ADMIN
(function () {
    const token = sessionStorage.getItem('adminToken');
    if (!token) {
        window.location.href = '/index.html';
    }
})();

// KHỞI TẠO KHI LOAD TRANG
window.addEventListener('DOMContentLoaded', function () {
    const name = sessionStorage.getItem('adminName') || 'Admin';
    const el = document.getElementById('adminName');
    if (el) el.textContent = name;

    applyRoleUI();
    loadStats();
    setupQuickLinks();
});

// PHÂN QUYỀN GIAO DIỆN
function isAdmin() {
    return sessionStorage.getItem('adminRole') === 'ADMIN';
}

function applyRoleUI() {
    const role = sessionStorage.getItem('adminRole') || 'NHAN_VIEN';
    const admin = role === 'ADMIN';

    // Ẩn link Nhân viên với NHAN_VIEN
    const adminOnlyLinks = ['/api/nhanvien'];
    if (!admin) {
        document.querySelectorAll('.quick-links .link-card').forEach(link => {
            const href = link.getAttribute('href');
            if (adminOnlyLinks.includes(href)) {
                link.style.display = 'none';
            }
        });
    }

    // Ẩn thống kê + welcome với NHAN_VIEN
    if (!admin) {
        const statGrid = document.querySelector('.stat-grid');
        if (statGrid) statGrid.style.display = 'none';
        const welcome = document.querySelector('.admin-welcome');
        if (welcome) welcome.style.display = 'none';
    }

    // Badge role trên header
    const adminNameEl = document.getElementById('adminName');
    if (adminNameEl) {
        const badge = document.createElement('span');
        badge.textContent = admin ? ' [ADMIN]' : ' [Nhân viên]';
        badge.style.cssText = `
            font-size: 0.75rem;
            padding: 2px 8px;
            border-radius: 12px;
            margin-left: 6px;
            background: ${admin ? '#e8f5e9' : '#e3f2fd'};
            color: ${admin ? '#2e7d32' : '#1565c0'};
            font-weight: 600;
        `;
        adminNameEl.parentNode.insertBefore(badge, adminNameEl.nextSibling);
    }
}

// LOAD THỐNG KÊ TỪ API
async function loadStats() {
    const token = sessionStorage.getItem('adminToken');
    const headers = { 'Authorization': 'Bearer ' + token };
    const apis = [
        { url: '/api/ban',        id: 'statBan',       count: rows => rows.filter(isServingTable).length },
        { url: '/orders',         id: 'statDonHang',   count: rows => rows.filter(isTodayOrder).length },
        { url: '/api/khachhang',  id: 'statKhachHang', count: rows => rows.length },
        { url: '/api/nhanvien',   id: 'statNhanVien',  count: rows => rows.length },
    ];

    for (const api of apis) {
        const el = document.getElementById(api.id);
        try {
            const res = await fetch(api.url, { headers });
            if (!res.ok) throw new Error('Request failed');
            const data = unwrapApiData(await res.json());
            const rows = Array.isArray(data) ? data : [];
            if (el) el.textContent = api.count(rows);
        } catch (e) {
            if (el) el.textContent = '0';
        }
    }
}

function isServingTable(ban) {
    return String(ban.trangThai || '').toUpperCase() === 'CO_KHACH';
}

function isTodayOrder(order) {
    if (!order.ngayDat) return false;
    const orderDate = new Date(order.ngayDat);
    const today = new Date();
    return orderDate.getFullYear() === today.getFullYear()
        && orderDate.getMonth() === today.getMonth()
        && orderDate.getDate() === today.getDate();
}

// ĐĂNG XUẤT
function logout() {
    if (confirm('Bạn có chắc muốn đăng xuất không?')) {
        const token = sessionStorage.getItem('adminToken');
        fetch('/api/auth/logout', {
            method: 'POST',
            headers: { 'Authorization': 'Bearer ' + token }
        }).finally(() => {
            sessionStorage.clear();
            window.location.href = '/index.html';
        });
    }
}

// QUICK LINKS — CẤU HÌNH MANAGER
const quickManagers = {
    '/api/ban':       { title: 'Quản lý bàn',       sub: 'Danh sách bàn hiện có' },
    '/api/monan':     { title: 'Quản lý món ăn',     sub: 'Xem, thêm và xóa món trong menu',   monAn: true },
    '/api/donhang':   { title: 'Quản lý đơn hàng',   sub: 'Danh sách đơn hàng',                apiUrl: '/orders' },
    '/api/hoadon':    { title: 'Quản lý hóa đơn',    sub: 'Danh sách hóa đơn' },
    '/api/khachhang': { title: 'Quản lý khách hàng', sub: 'Danh sách khách hàng thành viên' },
    '/api/nhanvien':  { title: 'Quản lý nhân viên',  sub: 'Danh sách nhân viên' },
    '/api/khuyenmai': { title: 'Quản lý khuyến mãi', sub: 'Danh sách chương trình ưu đãi',     khuyenMai: true },
    '/api/thanhtoan': { title: 'Quản lý thanh toán', sub: 'Lịch sử giao dịch' }
};

function setupQuickLinks() {
    document.querySelectorAll('.quick-links .link-card').forEach(link => {
        link.addEventListener('click', event => {
            event.preventDefault();
            openManager(link.getAttribute('href'));
        });
    });
}

function closeManager() {
    const box = document.getElementById('adminManager');
    if (box) box.style.display = 'none';
}

async function openManager(api) {
    const config = quickManagers[api];
    if (!config) return;

    document.getElementById('managerTitle').textContent = config.title;
    document.getElementById('managerSub').textContent   = config.sub;
    document.getElementById('adminManager').style.display = 'block';
    // Form thêm món chỉ hiện khi là monAn VÀ ADMIN
    document.getElementById('monForm').style.display = config.monAn && isAdmin() ? 'grid' : 'none';

    if (api === '/api/ban') {
        await loadBanManager();
    } else if (api === '/api/khachhang') {
        await loadKhachHangManager();
    } else if (api === '/api/nhanvien') {
        await loadNhanVienManager();
    } else if (api === '/api/hoadon') {
        await loadHoaDonManager();
    } else if (api === '/api/thanhtoan') {
        await loadThanhToanManager();
    } else if (config.monAn) {
        await loadMenuOptions();
        await loadMonAnAdmin();
    } else if (config.khuyenMai) {
        await loadKhuyenMaiManager();
    } else {
        await loadGenericManager(config.apiUrl || api);
    }

    document.getElementById('adminManager').scrollIntoView({ behavior: 'smooth', block: 'start' });
}

// ============================================================
// MANAGER BÀN — CRUD đầy đủ
// ============================================================
async function loadBanManager() {
    const content = document.getElementById('managerContent');
    content.innerHTML = '<div class="admin-loading">Đang tải danh sách bàn...</div>';

    try {
        const data = unwrapApiData(await fetch('/api/ban').then(r => r.json()));
        const rows = Array.isArray(data) ? data : [];
        const admin = isAdmin();

        // Form thêm bàn — chỉ ADMIN
        const formHtml = admin ? `
            <div id="banFormBox" style="background:var(--surface2,#f9f9f9);border:1px solid var(--border,#ddd);
                        border-radius:10px;padding:16px;margin-bottom:20px;">
                <h3 id="banFormTitle" style="margin:0 0 12px;font-size:1rem;color:#8b0000;font-weight:700;">➕ Thêm bàn mới</h3>
                <input type="hidden" id="banEditId">
                <div style="display:grid;grid-template-columns:1fr 1fr 1fr;gap:10px;">
                    <input id="banTen"       placeholder="Tên bàn *"          style="${inputStyle}">
                    <input id="banViTri"     placeholder="Vị trí"              style="${inputStyle}">
                    <input id="banSoChoNgoi" placeholder="Sức chứa *" type="number" min="1" style="${inputStyle}">
                    <input id="banLoaiBan"   placeholder="Loại bàn (VD: Ban VIP)" style="${inputStyle}">
                    <input id="banGhiChu"    placeholder="Ghi chú"             style="${inputStyle}">
                </div>
                <div style="margin-top:12px;display:flex;gap:10px;">
                    <button id="banSubmitBtn" onclick="submitBan()"
                        style="padding:8px 24px;border-radius:8px;border:none;
                               background:#8b0000;color:#fff;font-weight:700;cursor:pointer;font-size:0.9rem;">
                        Thêm bàn
                    </button>
                    <button id="banCancelBtn" onclick="resetBanForm()" style="display:none;
                        padding:8px 18px;border-radius:8px;border:1px solid #aaa;
                        background:#fff;color:#333;font-weight:600;cursor:pointer;font-size:0.9rem;">
                        Hủy
                    </button>
                </div>
                <div id="banFormMsg" style="margin-top:8px;font-size:0.85rem;color:#c62828;display:none;"></div>
            </div>` : '';

        if (!rows.length) {
            content.innerHTML = formHtml + '<div class="admin-empty">Chưa có bàn nào.</div>';
            return;
        }

        const tableHtml = `
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>TÊN BÀN</th>
                        <th>VỊ TRÍ</th>
                        <th>SỨC CHỨA</th>
                        <th>LOẠI BÀN</th>
                        <th>TRẠNG THÁI</th>
                        <th>THAO TÁC</th>
                    </tr>
                </thead>
                <tbody>
                    ${rows.map(ban => {
                        const laTrong = (ban.trangThai || '').toUpperCase() === 'TRONG';
                        const trangThaiLabel = laTrong
                            ? '<span style="color:#2e7d32;font-weight:600;">Trống</span>'
                            : '<span style="color:#c62828;font-weight:600;">Có khách</span>';

                        const btnToggle = `
                            <button onclick="toggleBan(${ban.banID})"
                                style="padding:5px 10px;border-radius:6px;border:none;cursor:pointer;
                                       font-weight:600;margin-right:4px;color:white;font-size:0.82rem;
                                       background:${laTrong ? '#43a047' : '#e53935'};">
                                ${laTrong ? 'Đánh dấu có khách' : 'Trả về trống'}
                            </button>`;

                        const btnSua = admin ? `
                            <button onclick="suaBan(${ban.banID},'${escapeHtml(ban.tenBan||'')}','${escapeHtml(ban.viTri||'')}',${ban.soChoNgoi},'${escapeHtml(ban.loaiBan||'')}','${escapeHtml(ban.ghiChu||'')}')"
                                style="padding:5px 10px;border-radius:6px;border:none;cursor:pointer;
                                       font-weight:600;margin-right:4px;background:#1565c0;color:white;font-size:0.82rem;">
                                Sửa
                            </button>` : '';

                        const btnXoa = admin ? `
                            <button onclick="xoaBan(${ban.banID})"
                                style="padding:5px 10px;border-radius:6px;border:none;cursor:pointer;
                                       font-weight:600;background:#b71c1c;color:white;font-size:0.82rem;">
                                Xóa
                            </button>` : '';

                        return `
                            <tr id="ban-row-${ban.banID}">
                                <td>${ban.banID}</td>
                                <td><strong>${escapeHtml(ban.tenBan || '')}</strong></td>
                                <td>${escapeHtml(ban.viTri || '—')}</td>
                                <td>${ban.soChoNgoi} chỗ</td>
                                <td>${escapeHtml(ban.loaiBan || '—')}</td>
                                <td>${trangThaiLabel}</td>
                                <td>${btnToggle}${btnSua}${btnXoa}</td>
                            </tr>`;
                    }).join('')}
                </tbody>
            </table>`;

        content.innerHTML = formHtml + tableHtml;
    } catch {
        content.innerHTML = '<div class="admin-empty">Không tải được danh sách bàn.</div>';
    }
}

function suaBan(id, ten, viTri, soChoNgoi, loaiBan, ghiChu) {
    document.getElementById('banEditId').value     = id;
    document.getElementById('banTen').value        = ten;
    document.getElementById('banViTri').value      = viTri;
    document.getElementById('banSoChoNgoi').value  = soChoNgoi;
    document.getElementById('banLoaiBan').value    = loaiBan;
    document.getElementById('banGhiChu').value     = ghiChu;
    document.getElementById('banFormTitle').textContent = '✏️ Sửa bàn #' + id;
    document.getElementById('banSubmitBtn').textContent = 'Cập nhật';
    document.getElementById('banCancelBtn').style.display = 'inline-block';
    document.getElementById('banFormBox').scrollIntoView({ behavior: 'smooth', block: 'start' });
}

function resetBanForm() {
    ['banEditId','banTen','banViTri','banSoChoNgoi','banLoaiBan','banGhiChu'].forEach(id => {
        document.getElementById(id).value = '';
    });
    document.getElementById('banFormTitle').textContent = '➕ Thêm bàn mới';
    document.getElementById('banSubmitBtn').textContent = 'Thêm bàn';
    document.getElementById('banCancelBtn').style.display = 'none';
    document.getElementById('banFormMsg').style.display = 'none';
}

async function submitBan() {
    if (!isAdmin()) { alert('Bạn không có quyền.'); return; }
    const ten       = document.getElementById('banTen').value.trim();
    const soChoNgoi = parseInt(document.getElementById('banSoChoNgoi').value || '0');
    const msg       = document.getElementById('banFormMsg');

    if (!ten)          { showFormMsg(msg, 'Vui lòng nhập tên bàn.'); return; }
    if (soChoNgoi < 1) { showFormMsg(msg, 'Sức chứa phải >= 1.'); return; }

    const editId = document.getElementById('banEditId').value;
    const payload = {
        tenBan:     ten,
        viTri:      document.getElementById('banViTri').value.trim() || null,
        soChoNgoi:  soChoNgoi,
        loaiBan:    document.getElementById('banLoaiBan').value.trim() || 'Ban thuong',
        ghiChu:     document.getElementById('banGhiChu').value.trim() || null
    };

    try {
        const token = sessionStorage.getItem('adminToken');
        const isEdit = !!editId;
        const res = await fetch(isEdit ? `/api/ban/${editId}` : '/api/ban', {
            method:  isEdit ? 'PUT' : 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
            body:    JSON.stringify(payload)
        });
        if (!res.ok) throw new Error(await res.text());
        resetBanForm();
        await loadBanManager();
        await loadStats();
    } catch (e) {
        showFormMsg(msg, 'Lỗi: ' + (e.message || 'Không lưu được bàn.'));
    }
}

async function toggleBan(banId) {
    try {
        const token = sessionStorage.getItem('adminToken');
        const res = await fetch(`/api/ban/${banId}/toggle-trang-thai`, {
            method: 'PATCH',
            headers: { 'Authorization': 'Bearer ' + token }
        });
        if (!res.ok) throw new Error();
        await loadBanManager();
        await loadStats();
    } catch {
        alert('Không thể đổi trạng thái bàn. Vui lòng thử lại.');
    }
}

async function xoaBan(banId) {
    if (!isAdmin()) { alert('Bạn không có quyền xóa bàn.'); return; }
    if (!confirm('Xóa bàn này?')) return;
    try {
        const token = sessionStorage.getItem('adminToken');
        const res = await fetch(`/api/ban/${banId}`, {
            method: 'DELETE',
            headers: { 'Authorization': 'Bearer ' + token }
        });
        if (!res.ok) throw new Error();
        await loadBanManager();
        await loadStats();
    } catch {
        alert('Không xóa được bàn này.');
    }
}

// MANAGER MÓN ĂN
async function loadMenuOptions() {
    const select = document.getElementById('monMenu');
    if (!select || select.options.length) return;
    try {
        const menus = unwrapApiData(await fetch('/api/menu').then(r => r.json()));
        select.innerHTML = menus.map(m =>
            `<option value="${m.menuID}">${escapeHtml(m.tenMenu || 'Menu')}</option>`
        ).join('');
    } catch {
        select.innerHTML = '<option value="">Không tải được menu</option>';
    }
}

async function loadMonAnAdmin() {
    const content = document.getElementById('managerContent');
    content.innerHTML = '<div class="admin-loading">Đang tải món ăn...</div>';
    try {
        const mons = await fetch('/api/monan').then(r => r.json());
        if (!mons.length) {
            content.innerHTML = '<div class="admin-empty">Chưa có món ăn nào.</div>';
            return;
        }

        const admin = isAdmin();
        content.innerHTML = `
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Món ăn</th>
                        <th>Giá</th>
                        <th>Trạng thái</th>
                        <th>Menu</th>
                        ${admin ? '<th></th>' : ''}
                    </tr>
                </thead>
                <tbody>
                    ${mons.map(mon => `
                        <tr>
                            <td>${mon.monID || ''}</td>
                            <td>
                                <strong>${escapeHtml(mon.tenMon || '')}</strong><br>
                                <small style="color:#888">${escapeHtml(mon.moTa || '')}</small>
                            </td>
                            <td>${fmtVNDAdmin(mon.gia || 0)}</td>
                            <td>${escapeHtml(mon.trangThai || '')}</td>
                            <td>${escapeHtml(mon.menu?.tenMenu || '')}</td>
                            ${admin ? `<td>
                                <button onclick="xoaMonAnAdmin(${mon.monID})"
                                    style="padding:5px 12px;border-radius:6px;border:none;cursor:pointer;
                                           background:#b71c1c;color:white;font-weight:600;">
                                     Xóa
                                </button>
                            </td>` : ''}
                        </tr>
                    `).join('')}
                </tbody>
            </table>`;
    } catch {
        content.innerHTML = '<div class="admin-empty">Không tải được danh sách món ăn.</div>';
    }
}

async function themMonAnAdmin() {
    if (!isAdmin()) { alert('Bạn không có quyền thêm món.'); return; }
    const phanLoai = document.getElementById('monPhanLoai').value;
    const payload = {
        tenMon:    document.getElementById('monTen').value.trim(),
        gia:       Number(document.getElementById('monGia').value || 0),
        menuID:    phanLoai === 'douong' ? 2 : 1,
        phanLoai:  phanLoai,
        trangThai: document.getElementById('monTrangThai').value,
        loai:      document.getElementById('monLoai').value.trim(),
        hinhAnh:   document.getElementById('monHinhAnh').value.trim(),
        moTa:      document.getElementById('monMoTa').value.trim()
    };

    if (!payload.tenMon || !payload.gia || !payload.menuID) {
        alert('Vui lòng nhập tên món, giá và menu.');
        return;
    }
    if (payload.phanLoai === 'douong') {
        payload.loai = ['S', 'M', 'L'].includes(payload.loai.toUpperCase())
            ? payload.loai.toUpperCase() : 'M';
    }

    try {
        const res = await fetch('/api/monan', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        if (!res.ok) throw new Error();
        ['monTen', 'monGia', 'monLoai', 'monHinhAnh', 'monMoTa'].forEach(
            id => document.getElementById(id).value = ''
        );
        await loadMonAnAdmin();
        await loadStats();
    } catch {
        alert('Không thêm được món ăn. Kiểm tra dữ liệu hoặc server.');
    }
}

async function xoaMonAnAdmin(id) {
    if (!isAdmin()) { alert('Bạn không có quyền xóa món.'); return; }
    if (!confirm('Bạn có chắc muốn xóa món này không?')) return;
    try {
        const res = await fetch(`/api/monan/${id}`, { method: 'DELETE' });
        if (!res.ok) throw new Error();
        await loadMonAnAdmin();
        await loadStats();
    } catch {
        alert('Không xóa được món này. Có thể món đã nằm trong đơn hàng.');
    }
}

// ============================================================
// MANAGER NHÂN VIÊN — CRUD đầy đủ (chỉ ADMIN)
// ============================================================
async function loadNhanVienManager() {
    const content = document.getElementById('managerContent');
    content.innerHTML = '<div class="admin-loading">Đang tải danh sách nhân viên...</div>';

    try {
        const token = sessionStorage.getItem('adminToken');
        const data = unwrapApiData(await fetch('/api/nhanvien', {
            headers: { 'Authorization': 'Bearer ' + token }
        }).then(r => r.json()));
        const rows = Array.isArray(data) ? data : [];
        const admin = isAdmin();

        const formHtml = admin ? `
            <div id="nvFormBox" style="background:var(--surface2,#f9f9f9);border:1px solid var(--border,#ddd);
                        border-radius:10px;padding:16px;margin-bottom:20px;">
                <h3 id="nvFormTitle" style="margin:0 0 12px;font-size:1rem;color:#8b0000;font-weight:700;">➕ Thêm nhân viên mới</h3>
                <input type="hidden" id="nvEditId">
                <div style="display:grid;grid-template-columns:1fr 1fr 1fr;gap:10px;">
                    <input id="nvHoTen"   placeholder="Họ tên *"            style="${inputStyle}">
                    <input id="nvSdt"     placeholder="Số điện thoại *"      style="${inputStyle}">
                    <input id="nvEmail"   placeholder="Email *"              style="${inputStyle}">
                    <input id="nvDiaChi"  placeholder="Địa chỉ"             style="${inputStyle}">
                    <input id="nvChucVu"  placeholder="Chức vụ"             style="${inputStyle}">
                    <input id="nvLuong"   placeholder="Lương" type="number" min="0" style="${inputStyle}">
                    <input id="nvPassword" placeholder="Mật khẩu (để trống = không đổi)" type="password" style="${inputStyle}">
                    <select id="nvRole" style="${inputStyle}">
                        <option value="NHAN_VIEN">Nhân viên</option>
                        <option value="ADMIN">Admin</option>
                    </select>
                </div>
                <div style="margin-top:12px;display:flex;gap:10px;">
                    <button id="nvSubmitBtn" onclick="submitNhanVien()"
                        style="padding:8px 24px;border-radius:8px;border:none;
                               background:#8b0000;color:#fff;font-weight:700;cursor:pointer;font-size:0.9rem;">
                        Thêm nhân viên
                    </button>
                    <button id="nvCancelBtn" onclick="resetNhanVienForm()" style="display:none;
                        padding:8px 18px;border-radius:8px;border:1px solid #aaa;
                        background:#fff;color:#333;font-weight:600;cursor:pointer;font-size:0.9rem;">
                        Hủy
                    </button>
                </div>
                <div id="nvFormMsg" style="margin-top:8px;font-size:0.85rem;color:#c62828;display:none;"></div>
            </div>` : '';

        if (!rows.length) {
            content.innerHTML = formHtml + '<div class="admin-empty">Chưa có nhân viên nào.</div>';
            return;
        }

        const tableHtml = `
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>HỌ TÊN</th>
                        <th>SĐT</th>
                        <th>DIACHI</th>
                        <th>EMAIL</th>
                        <th>LƯƠNG</th>
                        <th>CHỨC VỤ</th>
                        <th>ROLE</th>
                        <th>THAO TÁC</th>
                    </tr>
                </thead>
                <tbody>
                    ${rows.map(nv => {
                        const roleBadge = nv.role === 'ADMIN'
                            ? '<span style="padding:2px 8px;border-radius:10px;background:#fce4ec;color:#880e4f;font-weight:700;font-size:0.78rem;">ADMIN</span>'
                            : '<span style="padding:2px 8px;border-radius:10px;background:#e8f5e9;color:#1b5e20;font-weight:700;font-size:0.78rem;">NV</span>';

                        const btnSua = admin ? `
                            <button onclick="suaNhanVien(${nv.id},'${escapeHtml(nv.hoTen||'')}','${escapeHtml(nv.sdt||'')}','${escapeHtml(nv.email||'')}','${escapeHtml(nv.diaChi||'')}','${escapeHtml(nv.chucVu||'')}',${nv.luong||0},'${nv.role||'NHAN_VIEN'}')"
                                style="padding:5px 10px;border-radius:6px;border:none;cursor:pointer;
                                       font-weight:600;margin-right:4px;background:#1565c0;color:white;font-size:0.82rem;">
                                Sửa
                            </button>` : '';

                        const btnXoa = admin ? `
                            <button onclick="xoaNhanVien(${nv.id})"
                                style="padding:5px 10px;border-radius:6px;border:none;cursor:pointer;
                                       font-weight:600;background:#b71c1c;color:white;font-size:0.82rem;">
                                Xóa
                            </button>` : '';

                        return `
                            <tr id="nv-row-${nv.id}">
                                <td>${nv.id}</td>
                                <td><strong>${escapeHtml(nv.hoTen || '')}</strong></td>
                                <td>${escapeHtml(nv.sdt || '—')}</td>
                                <td>${escapeHtml(nv.diaChi || '—')}</td>
                                <td>${escapeHtml(nv.email || '—')}</td>
                                <td>${nv.luong ? nv.luong.toLocaleString('vi-VN') + '₫' : '—'}</td>
                                <td>${escapeHtml(nv.chucVu || '—')}</td>
                                <td>${roleBadge}</td>
                                <td>${btnSua}${btnXoa}</td>
                            </tr>`;
                    }).join('')}
                </tbody>
            </table>`;

        content.innerHTML = formHtml + tableHtml;
    } catch {
        content.innerHTML = '<div class="admin-empty">Không tải được danh sách nhân viên.</div>';
    }
}

function suaNhanVien(id, hoTen, sdt, email, diaChi, chucVu, luong, role) {
    document.getElementById('nvEditId').value  = id;
    document.getElementById('nvHoTen').value   = hoTen;
    document.getElementById('nvSdt').value     = sdt;
    document.getElementById('nvEmail').value   = email;
    document.getElementById('nvDiaChi').value  = diaChi;
    document.getElementById('nvChucVu').value  = chucVu;
    document.getElementById('nvLuong').value   = luong;
    document.getElementById('nvRole').value    = role;
    document.getElementById('nvPassword').value = '';
    document.getElementById('nvFormTitle').textContent = '✏️ Sửa nhân viên #' + id;
    document.getElementById('nvSubmitBtn').textContent = 'Cập nhật';
    document.getElementById('nvCancelBtn').style.display = 'inline-block';
    document.getElementById('nvFormBox').scrollIntoView({ behavior: 'smooth', block: 'start' });
}

function resetNhanVienForm() {
    ['nvEditId','nvHoTen','nvSdt','nvEmail','nvDiaChi','nvChucVu','nvLuong','nvPassword'].forEach(id => {
        document.getElementById(id).value = '';
    });
    document.getElementById('nvRole').value = 'NHAN_VIEN';
    document.getElementById('nvFormTitle').textContent = '➕ Thêm nhân viên mới';
    document.getElementById('nvSubmitBtn').textContent = 'Thêm nhân viên';
    document.getElementById('nvCancelBtn').style.display = 'none';
    document.getElementById('nvFormMsg').style.display = 'none';
}

async function submitNhanVien() {
    if (!isAdmin()) { alert('Bạn không có quyền.'); return; }
    const hoTen = document.getElementById('nvHoTen').value.trim();
    const sdt   = document.getElementById('nvSdt').value.trim();
    const email = document.getElementById('nvEmail').value.trim();
    const msg   = document.getElementById('nvFormMsg');

    if (!hoTen)  { showFormMsg(msg, 'Vui lòng nhập họ tên.'); return; }
    if (!sdt)    { showFormMsg(msg, 'Vui lòng nhập số điện thoại.'); return; }
    if (!/^0[0-9]{9}$/.test(sdt)) { showFormMsg(msg, 'SĐT không hợp lệ (VD: 0912345678).'); return; }

    const editId   = document.getElementById('nvEditId').value;
    const password = document.getElementById('nvPassword').value;
    const payload  = {
        hoTen:   hoTen,
        sdt:     sdt,
        email:   email || null,
        diaChi:  document.getElementById('nvDiaChi').value.trim() || null,
        chucVu:  document.getElementById('nvChucVu').value.trim() || null,
        luong:   parseFloat(document.getElementById('nvLuong').value || '0'),
        role:    document.getElementById('nvRole').value,
        trangThai: true
    };
    if (password) payload.password = password;
    if (!editId && !password) { showFormMsg(msg, 'Vui lòng nhập mật khẩu cho nhân viên mới.'); return; }

    try {
        const token  = sessionStorage.getItem('adminToken');
        const isEdit = !!editId;
        const res = await fetch(isEdit ? `/api/nhanvien/${editId}` : '/api/nhanvien', {
            method:  isEdit ? 'PUT' : 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
            body:    JSON.stringify(payload)
        });
        if (!res.ok) {
            const err = await res.json().catch(() => ({}));
            throw new Error(err.message || 'Lỗi server');
        }
        resetNhanVienForm();
        await loadNhanVienManager();
        await loadStats();
    } catch (e) {
        showFormMsg(msg, 'Lỗi: ' + (e.message || 'Không lưu được.'));
    }
}

async function xoaNhanVien(id) {
    if (!isAdmin()) { alert('Bạn không có quyền xóa nhân viên.'); return; }
    if (!confirm('Xóa nhân viên này? Hành động không thể hoàn tác.')) return;
    try {
        const token = sessionStorage.getItem('adminToken');
        const res = await fetch(`/api/nhanvien/${id}`, {
            method:  'DELETE',
            headers: { 'Authorization': 'Bearer ' + token }
        });
        if (!res.ok) throw new Error();
        await loadNhanVienManager();
        await loadStats();
    } catch {
        alert('Không xóa được nhân viên này.');
    }
}

// ============================================================
// MANAGER KHUYẾN MÃI (ADMIN thêm / sửa / xóa)
// ============================================================
const inputStyle = 'width:100%;padding:8px 10px;border-radius:8px;border:1px solid var(--border,#ddd);font-size:0.88rem;box-sizing:border-box;background:white;';

async function loadKhuyenMaiManager() {
    const content = document.getElementById('managerContent');
    content.innerHTML = '<div class="admin-loading">Đang tải khuyến mãi...</div>';

    try {
        const data = unwrapApiData(await fetch('/api/khuyenmai').then(r => r.json()));
        const rows = Array.isArray(data) ? data : [];
        const admin = isAdmin();

        // Form thêm KM — chỉ ADMIN thấy
        const formHtml = admin ? `
            <div style="background:var(--surface2,#f9f9f9);border:1px solid var(--border,#ddd);
                        border-radius:10px;padding:16px;margin-bottom:20px;">
                <h3 style="margin:0 0 12px;font-size:1rem;color:#8b0000;font-weight:700;">➕ Thêm khuyến mãi mới</h3>
                <div style="display:grid;grid-template-columns:1fr 1fr;gap:10px;">
                    <input  id="kmTen"          placeholder="Tên khuyến mãi *"                      style="${inputStyle}">
                    <input  id="kmMa"           placeholder="Mã khuyến mãi (VD: KM10)"              style="${inputStyle}">
                    <select id="kmLoai"         style="${inputStyle}">
                        <option value="PHAN_TRAM">Giảm theo %</option>
                        <option value="GIAM_TIEN_MAT">Giảm tiền mặt (VNĐ)</option>
                    </select>
                    <input  id="kmGiaTri"       placeholder="Giá trị (% hoặc VNĐ) *" type="number" min="0" style="${inputStyle}">
                    <input  id="kmBatDau"       type="datetime-local" title="Ngày bắt đầu *"        style="${inputStyle}">
                    <input  id="kmKetThuc"      type="datetime-local" title="Ngày kết thúc *"       style="${inputStyle}">
                    <input  id="kmToiThieuTien" placeholder="Tổng tiền tối thiểu (0 = không giới hạn)" type="number" min="0" style="${inputStyle}">
                    <input  id="kmMoTa"         placeholder="Mô tả"                                 style="${inputStyle}">
                </div>
                <button onclick="themKhuyenMai()"
                    style="margin-top:12px;padding:8px 24px;border-radius:8px;border:none;
                           background:#8b0000;color:#fff;font-weight:700;cursor:pointer;font-size:0.9rem;">
                    Thêm khuyến mãi
                </button>
            </div>` : '';

        if (!rows.length) {
            content.innerHTML = formHtml + '<div class="admin-empty">Chưa có khuyến mãi nào.</div>';
            return;
        }

        const tableHtml = `
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tên / Mô tả</th>
                        <th>Mã</th>
                        <th>Ưu đãi</th>
                        <th>Đơn tối thiểu</th>
                        <th>Hạn dùng</th>
                        <th>Trạng thái</th>
                        ${admin ? '<th>THAO TÁC</th>' : ''}
                    </tr>
                </thead>
                <tbody>
                    ${rows.map(km => {
                        const loaiLabel = km.loaiKhuyenMai === 'PHAN_TRAM'
                            ? `Giảm <strong>${km.giaTri}%</strong>`
                            : `Giảm <strong>${fmtVNDAdmin(km.giaTri)}</strong>`;

                        const hetHan   = km.ngayKetThuc ? new Date(km.ngayKetThuc) : null;
                        const conHan   = !hetHan || hetHan >= new Date();
                        const hanLabel = hetHan ? hetHan.toLocaleDateString('vi-VN') : 'Không giới hạn';
                        const ttLabel  = (km.trangThai && conHan)
                            ? '<span style="color:#2e7d32;font-weight:600;"> Đang hoạt động</span>'
                            : '<span style="color:#c62828;font-weight:600;"> Hết hạn / Tắt</span>';

                        return `<tr>
                            <td>${km.khuyenMaiID}</td>
                            <td>
                                <strong>${escapeHtml(km.tenKhuyenMai || '')}</strong><br>
                                <small style="color:#888">${escapeHtml(km.moTa || '')}</small>
                            </td>
                            <td>
                                <code style="background:#f3f3f3;padding:2px 6px;border-radius:4px;">
                                    ${escapeHtml(km.maKhuyenMai || '—')}
                                </code>
                            </td>
                            <td>${loaiLabel}</td>
                            <td>${km.tongTienToiThieu > 0 ? 'Từ ' + fmtVNDAdmin(km.tongTienToiThieu) : '—'}</td>
                            <td>${hanLabel}</td>
                            <td>${ttLabel}</td>
                            ${admin ? `<td style="white-space:nowrap;">
                                <button onclick="suaKhuyenMai(${km.khuyenMaiID},'${escapeHtml(km.tenKhuyenMai||'')}','${escapeHtml(km.maKhuyenMai||'')}','${km.loaiKhuyenMai}',${km.giaTri},'${km.ngayBatDau||''}','${km.ngayKetThuc||''}',${km.tongTienToiThieu||0},'${escapeHtml(km.moTa||'')}',${km.trangThai})"
                                    style="padding:5px 10px;border-radius:6px;border:none;cursor:pointer;
                                           font-weight:600;margin-right:4px;background:#1565c0;color:white;font-size:0.82rem;">
                                    Sửa
                                </button>
                                <button onclick="xoaKhuyenMai(${km.khuyenMaiID})"
                                    style="padding:5px 10px;border-radius:6px;border:none;cursor:pointer;
                                           background:#b71c1c;color:white;font-weight:600;font-size:0.82rem;">
                                    Xóa
                                </button>
                            </td>` : ''}
                        </tr>`;
                    }).join('')}
                </tbody>
            </table>`;

        content.innerHTML = formHtml + tableHtml;
    } catch {
        content.innerHTML = '<div class="admin-empty">Không tải được danh sách khuyến mãi.</div>';
    }
}

function suaKhuyenMai(id, ten, ma, loai, giaTri, batDau, ketThuc, toiThieuTien, moTa, trangThai) {
    document.getElementById('kmTen').value          = ten;
    document.getElementById('kmMa').value           = ma;
    document.getElementById('kmLoai').value         = loai;
    document.getElementById('kmGiaTri').value       = giaTri;
    document.getElementById('kmBatDau').value       = batDau ? batDau.substring(0, 16) : '';
    document.getElementById('kmKetThuc').value      = ketThuc ? ketThuc.substring(0, 16) : '';
    document.getElementById('kmToiThieuTien').value = toiThieuTien;
    document.getElementById('kmMoTa').value         = moTa;

    // Đổi nút thành Cập nhật
    const btn = document.querySelector('[onclick="themKhuyenMai()"]');
    if (btn) {
        btn.textContent = 'Cập nhật khuyến mãi';
        btn.setAttribute('onclick', `capNhatKhuyenMai(${id})`);
    }
    document.querySelector('#managerContent').scrollIntoView({ behavior: 'smooth', block: 'start' });
}

async function capNhatKhuyenMai(id) {
    if (!isAdmin()) { alert('Bạn không có quyền.'); return; }
    const ten     = document.getElementById('kmTen').value.trim();
    const giaTri  = parseFloat(document.getElementById('kmGiaTri').value || '0');
    const batDau  = document.getElementById('kmBatDau').value;
    const ketThuc = document.getElementById('kmKetThuc').value;

    if (!ten)                   { alert('Vui lòng nhập tên khuyến mãi.'); return; }
    if (!giaTri || giaTri <= 0) { alert('Vui lòng nhập giá trị hợp lệ.'); return; }
    if (!batDau || !ketThuc)    { alert('Vui lòng chọn ngày bắt đầu và kết thúc.'); return; }
    if (new Date(batDau) >= new Date(ketThuc)) { alert('Ngày kết thúc phải sau ngày bắt đầu.'); return; }

    const payload = {
        tenKhuyenMai:     ten,
        maKhuyenMai:      document.getElementById('kmMa').value.trim() || null,
        loaiKhuyenMai:    document.getElementById('kmLoai').value,
        giaTri:           giaTri,
        ngayBatDau:       batDau,
        ngayKetThuc:      ketThuc,
        tongTienToiThieu: parseFloat(document.getElementById('kmToiThieuTien').value || '0'),
        moTa:             document.getElementById('kmMoTa').value.trim() || null,
        trangThai:        true
    };

    try {
        const token = sessionStorage.getItem('adminToken');
        const res = await fetch(`/api/khuyenmai/${id}`, {
            method:  'PUT',
            headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
            body:    JSON.stringify(payload)
        });
        if (!res.ok) throw new Error();
        await loadKhuyenMaiManager();
    } catch {
        alert('Không cập nhật được khuyến mãi. Kiểm tra lại dữ liệu hoặc server.');
    }
}

async function themKhuyenMai() {
    if (!isAdmin()) { alert('Bạn không có quyền thêm khuyến mãi.'); return; }

    const ten          = document.getElementById('kmTen').value.trim();
    const ma           = document.getElementById('kmMa').value.trim();
    const loai         = document.getElementById('kmLoai').value;
    const giaTri       = parseFloat(document.getElementById('kmGiaTri').value || '0');
    const batDau       = document.getElementById('kmBatDau').value;
    const ketThuc      = document.getElementById('kmKetThuc').value;
    const toiThieuTien = parseFloat(document.getElementById('kmToiThieuTien').value || '0');
    const moTa         = document.getElementById('kmMoTa').value.trim();

    if (!ten)                { alert('Vui lòng nhập tên khuyến mãi.'); return; }
    if (!giaTri || giaTri <= 0) { alert('Vui lòng nhập giá trị hợp lệ (> 0).'); return; }
    if (!batDau || !ketThuc) { alert('Vui lòng chọn ngày bắt đầu và kết thúc.'); return; }
    if (new Date(batDau) >= new Date(ketThuc)) {
        alert('Ngày kết thúc phải sau ngày bắt đầu.');
        return;
    }

    const payload = {
        tenKhuyenMai:     ten,
        maKhuyenMai:      ma || null,
        loaiKhuyenMai:    loai,
        giaTri:           giaTri,
        ngayBatDau:       batDau,
        ngayKetThuc:      ketThuc,
        tongTienToiThieu: toiThieuTien,
        moTa:             moTa || null,
        trangThai:        true
    };

    try {
        const token = sessionStorage.getItem('adminToken');
        const res = await fetch('/api/khuyenmai', {
            method:  'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify(payload)
        });
        if (!res.ok) throw new Error();
        await loadKhuyenMaiManager();
    } catch {
        alert('Không thêm được khuyến mãi. Kiểm tra lại dữ liệu hoặc server.');
    }
}

async function xoaKhuyenMai(id) {
    if (!isAdmin()) { alert('Bạn không có quyền xóa khuyến mãi.'); return; }
    if (!confirm('Xóa khuyến mãi này?')) return;
    try {
        const token = sessionStorage.getItem('adminToken');
        const res = await fetch(`/api/khuyenmai/${id}`, {
            method:  'DELETE',
            headers: { 'Authorization': 'Bearer ' + token }
        });
        if (!res.ok) throw new Error();
        await loadKhuyenMaiManager();
    } catch {
        alert('Không xóa được khuyến mãi này. Có thể đang được dùng trong đơn hàng.');
    }
}

// GENERIC MANAGER (hóa đơn, khách hàng, ...)
async function loadGenericManager(api) {
    const content = document.getElementById('managerContent');
    content.innerHTML = '<div class="admin-loading">Đang tải dữ liệu...</div>';
    try {
        const raw = await fetch(api).then(r => r.json());
        const data = unwrapApiData(raw) ?? raw; // fallback nếu không wrap ApiResponse
        const rows = Array.isArray(data) ? data : [data].filter(Boolean);
        if (!rows.length) {
            content.innerHTML = '<div class="admin-empty">Chưa có dữ liệu.</div>';
            return;
        }
        const keys = Object.keys(rows[0]).slice(0, 8);
        const getValue = (row, k) => {
            const v = row[k];
            if (v === null || v === undefined) return '';
            if (typeof v === 'object') {
                // lấy id hoặc tên đầu tiên tìm được
                return v.id ?? v.hoaDonID ?? v.donHangID ?? v.thanhToanID ?? JSON.stringify(v).slice(0, 30);
            }
            return v;
        };
        content.innerHTML = `
            <table class="admin-table">
                <thead>
                    <tr>${keys.map(k => `<th>${escapeHtml(k)}</th>`).join('')}</tr>
                </thead>
                <tbody>
                    ${rows.map(row =>
                        `<tr>${keys.map(k => `<td>${escapeHtml(String(getValue(row, k)))}</td>`).join('')}</tr>`
                    ).join('')}
                </tbody>
            </table>`;
    } catch(e) {
        content.innerHTML = '<div class="admin-empty">Không tải được dữ liệu.</div>';
        console.error(e);
    }
}

// ============================================================
// MANAGER KHÁCH HÀNG — CRUD đầy đủ
// ============================================================
async function loadKhachHangManager() {
    const content = document.getElementById('managerContent');
    content.innerHTML = '<div class="admin-loading">Đang tải danh sách khách hàng...</div>';

    try {
        const data = unwrapApiData(await fetch('/api/khachhang').then(r => r.json()));
        const rows = Array.isArray(data) ? data : [];
        const admin = isAdmin();

        const hangBadge = {
            'DONG':      { label: 'Đồng',      color: '#795548', bg: '#efebe9' },
            'BAC':       { label: 'Bạc',        color: '#546e7a', bg: '#eceff1' },
            'VANG':      { label: 'Vàng',       color: '#f57f17', bg: '#fff9c4' },
            'KIM_CUONG': { label: 'Kim Cương',  color: '#1565c0', bg: '#e3f2fd' }
        };

        // Form thêm/sửa — chỉ ADMIN
        const formHtml = admin ? `
            <div id="khFormBox" style="background:var(--surface2,#f9f9f9);border:1px solid var(--border,#ddd);
                        border-radius:10px;padding:16px;margin-bottom:20px;">
                <h3 id="khFormTitle" style="margin:0 0 12px;font-size:1rem;color:#8b0000;font-weight:700;">➕ Thêm khách hàng</h3>
                <input type="hidden" id="khEditId">
                <div style="display:grid;grid-template-columns:1fr 1fr 1fr;gap:10px;">
                    <input id="khHoTen"  placeholder="Họ tên *"                style="${inputStyle}">
                    <input id="khSdt"    placeholder="Số điện thoại *"          style="${inputStyle}">
                    <input id="khEmail"  placeholder="Email"                    style="${inputStyle}">
                    <input id="khDiaChi" placeholder="Địa chỉ"                 style="${inputStyle}">
                    <input id="khDiem"   placeholder="Điểm tích lũy" type="number" min="0" style="${inputStyle}">
                </div>
                <div style="margin-top:12px;display:flex;gap:10px;">
                    <button id="khSubmitBtn" onclick="submitKhachHang()"
                        style="padding:8px 24px;border-radius:8px;border:none;
                               background:#8b0000;color:#fff;font-weight:700;cursor:pointer;font-size:0.9rem;">
                        Thêm khách hàng
                    </button>
                    <button id="khCancelBtn" onclick="resetKhachHangForm()" style="display:none;
                        padding:8px 18px;border-radius:8px;border:1px solid #aaa;
                        background:#fff;color:#333;font-weight:600;cursor:pointer;font-size:0.9rem;">
                        Hủy
                    </button>
                </div>
                <div id="khFormMsg" style="margin-top:8px;font-size:0.85rem;color:#c62828;display:none;"></div>
            </div>` : '';

        if (!rows.length) {
            content.innerHTML = formHtml + '<div class="admin-empty">Chưa có khách hàng nào.</div>';
            return;
        }

        const tableHtml = `
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>HỌ TÊN</th>
                        <th>SĐT</th>
                        <th>EMAIL</th>
                        <th>ĐIỂM TÍCH LŨY</th>
                        <th>HẠNG</th>
                        <th>THAO TÁC</th>
                    </tr>
                </thead>
                <tbody>
                    ${rows.map(kh => {
                        const hang = hangBadge[kh.loaiKhachHang] || hangBadge['DONG'];
                        const badgeHtml = `<span style="padding:3px 10px;border-radius:12px;font-size:0.8rem;
                            font-weight:700;background:${hang.bg};color:${hang.color};">${hang.label}</span>`;

                        const btnSua = admin ? `
                            <button onclick="suaKhachHang(${kh.id},'${escapeHtml(kh.hoTen||'')}','${escapeHtml(kh.sdt||'')}','${escapeHtml(kh.email||'')}','${escapeHtml(kh.diaChi||'')}',${kh.diemTichLuy||0})"
                                style="padding:5px 10px;border-radius:6px;border:none;cursor:pointer;
                                       font-weight:600;margin-right:4px;background:#1565c0;color:white;font-size:0.82rem;">
                                Sửa
                            </button>` : '';

                        const btnXoa = admin ? `
                            <button onclick="xoaKhachHang(${kh.id})"
                                style="padding:5px 10px;border-radius:6px;border:none;cursor:pointer;
                                       font-weight:600;background:#b71c1c;color:white;font-size:0.82rem;">
                                Xóa
                            </button>` : '';

                        return `
                            <tr id="kh-row-${kh.id}">
                                <td>${kh.id}</td>
                                <td><strong>${escapeHtml(kh.hoTen || '')}</strong></td>
                                <td>${escapeHtml(kh.sdt || '—')}</td>
                                <td>${escapeHtml(kh.email || '—')}</td>
                                <td style="text-align:center;font-weight:700;">${kh.diemTichLuy ?? 0}</td>
                                <td>${badgeHtml}</td>
                                <td>${btnSua}${btnXoa}</td>
                            </tr>`;
                    }).join('')}
                </tbody>
            </table>`;

        content.innerHTML = formHtml + tableHtml;
    } catch {
        content.innerHTML = '<div class="admin-empty">Không tải được danh sách khách hàng.</div>';
    }
}

function suaKhachHang(id, hoTen, sdt, email, diaChi, diem) {
    document.getElementById('khEditId').value  = id;
    document.getElementById('khHoTen').value   = hoTen;
    document.getElementById('khSdt').value     = sdt;
    document.getElementById('khEmail').value   = email;
    document.getElementById('khDiaChi').value  = diaChi;
    document.getElementById('khDiem').value    = diem;
    document.getElementById('khFormTitle').textContent = '✏️ Sửa khách hàng #' + id;
    document.getElementById('khSubmitBtn').textContent = 'Cập nhật';
    document.getElementById('khCancelBtn').style.display = 'inline-block';
    document.getElementById('khFormBox').scrollIntoView({ behavior: 'smooth', block: 'start' });
}

function resetKhachHangForm() {
    ['khEditId','khHoTen','khSdt','khEmail','khDiaChi','khDiem'].forEach(id => {
        document.getElementById(id).value = '';
    });
    document.getElementById('khFormTitle').textContent = '➕ Thêm khách hàng';
    document.getElementById('khSubmitBtn').textContent = 'Thêm khách hàng';
    document.getElementById('khCancelBtn').style.display = 'none';
    document.getElementById('khFormMsg').style.display = 'none';
}

async function submitKhachHang() {
    if (!isAdmin()) { alert('Bạn không có quyền.'); return; }
    const hoTen = document.getElementById('khHoTen').value.trim();
    const sdt   = document.getElementById('khSdt').value.trim();
    const msg   = document.getElementById('khFormMsg');

    if (!hoTen) { showFormMsg(msg, 'Vui lòng nhập họ tên.'); return; }
    if (!sdt)   { showFormMsg(msg, 'Vui lòng nhập số điện thoại.'); return; }
    if (!/^0[0-9]{9}$/.test(sdt)) { showFormMsg(msg, 'SĐT không hợp lệ (VD: 0912345678).'); return; }

    const editId = document.getElementById('khEditId').value;
    const payload = {
        hoTen:        hoTen,
        sdt:          sdt,
        email:        document.getElementById('khEmail').value.trim() || null,
        diaChi:       document.getElementById('khDiaChi').value.trim() || null,
        diemTichLuy:  parseInt(document.getElementById('khDiem').value || '0')
    };

    try {
        const token = sessionStorage.getItem('adminToken');
        const isEdit = !!editId;
        const res = await fetch(isEdit ? `/api/khachhang/${editId}` : '/api/khachhang', {
            method:  isEdit ? 'PUT' : 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
            body:    JSON.stringify(payload)
        });
        if (!res.ok) {
            const err = await res.json().catch(() => ({}));
            throw new Error(err.message || 'Lỗi server');
        }
        resetKhachHangForm();
        await loadKhachHangManager();
        await loadStats();
    } catch (e) {
        showFormMsg(msg, 'Lỗi: ' + (e.message || 'Không lưu được.'));
    }
}

async function xoaKhachHang(id) {
    if (!isAdmin()) { alert('Bạn không có quyền xóa khách hàng.'); return; }
    if (!confirm('Xóa khách hàng này? Dữ liệu đơn hàng liên quan có thể bị ảnh hưởng.')) return;
    try {
        const token = sessionStorage.getItem('adminToken');
        const res = await fetch(`/api/khachhang/${id}`, {
            method:  'DELETE',
            headers: { 'Authorization': 'Bearer ' + token }
        });
        if (!res.ok) throw new Error();
        await loadKhachHangManager();
        await loadStats();
    } catch {
        alert('Không xóa được khách hàng này.');
    }
}

// ============================================================
// MANAGER HÓA ĐƠN — hiển thị kèm thông tin bàn
// ============================================================
async function loadHoaDonManager() {
    const content = document.getElementById('managerContent');
    content.innerHTML = '<div class="admin-loading">Đang tải hóa đơn...</div>';
    try {
        // Fetch song song hóa đơn và đơn hàng (để lấy tên bàn)
        const [rawHD, rawDH] = await Promise.all([
            fetch('/api/hoadon').then(r => r.json()),
            fetch('/api/donhang').then(r => r.json())
        ]);
        const hoaDons  = unwrapApiData(rawHD) ?? [];
        const donHangs = unwrapApiData(rawDH) ?? [];

        // Map donHangID → tenBan
        const banMap = {};
        for (const dh of (Array.isArray(donHangs) ? donHangs : [])) {
            banMap[dh.donHangID] = dh.ban ? (dh.ban.tenBan || 'Bàn ' + dh.ban.banID) : '—';
        }

        const rows = Array.isArray(hoaDons) ? hoaDons : [];
        if (!rows.length) {
            content.innerHTML = '<div class="admin-empty">Chưa có hóa đơn nào.</div>';
            return;
        }

        const tableHtml = `
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>ID HÓA ĐƠN</th>
                        <th>BÀN</th>
                        <th>NGÀY LẬP</th>
                        <th>TỔNG TIỀN</th>
                        <th>GIẢM GIÁ</th>
                        <th>THÀNH TIỀN</th>
                    </tr>
                </thead>
                <tbody>
                    ${rows.map(hd => {
                        // hoaDonID có thể map sang donHang qua index (hóa đơn tạo từ đơn hàng cùng ID)
                        const tenBan = banMap[hd.hoaDonID] || '—';
                        const ngay = hd.ngayLap ? new Date(hd.ngayLap).toLocaleString('vi-VN') : '—';
                        return `
                            <tr>
                                <td>${hd.hoaDonID}</td>
                                <td><strong>${escapeHtml(tenBan)}</strong></td>
                                <td>${ngay}</td>
                                <td>${fmtVNDAdmin(hd.tongTien)}</td>
                                <td>${fmtVNDAdmin(hd.giamGia)}</td>
                                <td style="color:#2e7d32;font-weight:700;">${fmtVNDAdmin(hd.thanhTien)}</td>
                            </tr>`;
                    }).join('')}
                </tbody>
            </table>`;
        content.innerHTML = tableHtml;
    } catch(e) {
        content.innerHTML = '<div class="admin-empty">Không tải được danh sách hóa đơn.</div>';
        console.error(e);
    }
}

// ============================================================
// MANAGER THANH TOÁN — hiển thị kèm thông tin bàn
// ============================================================
async function loadThanhToanManager() {
    const content = document.getElementById('managerContent');
    content.innerHTML = '<div class="admin-loading">Đang tải thanh toán...</div>';
    try {
        // Fetch song song thanh toán + hóa đơn + đơn hàng
        const [rawTT, rawHD, rawDH] = await Promise.all([
            fetch('/api/thanhtoan').then(r => r.json()),
            fetch('/api/hoadon').then(r => r.json()),
            fetch('/api/donhang').then(r => r.json())
        ]);
        const thanhToans = unwrapApiData(rawTT) ?? [];
        const hoaDons    = unwrapApiData(rawHD) ?? [];
        const donHangs   = unwrapApiData(rawDH) ?? [];

        // Map donHangID → tenBan
        const banByDonHang = {};
        for (const dh of (Array.isArray(donHangs) ? donHangs : [])) {
            banByDonHang[dh.donHangID] = dh.ban ? (dh.ban.tenBan || 'Bàn ' + dh.ban.banID) : '—';
        }
        // Map hoaDonID → tenBan (hoaDon.hoaDonID ≈ donHang.donHangID theo thiết kế OneToOne)
        const banByHoaDon = {};
        for (const hd of (Array.isArray(hoaDons) ? hoaDons : [])) {
            banByHoaDon[hd.hoaDonID] = banByDonHang[hd.hoaDonID] || '—';
        }

        const rows = Array.isArray(thanhToans) ? thanhToans : [];
        if (!rows.length) {
            content.innerHTML = '<div class="admin-empty">Chưa có giao dịch nào.</div>';
            return;
        }

        const tableHtml = `
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>BÀN</th>
                        <th>PHƯƠNG THỨC</th>
                        <th>SỐ TIỀN</th>
                        <th>THỜI GIAN</th>
                        <th>TRẠNG THÁI</th>
                    </tr>
                </thead>
                <tbody>
                    ${rows.map(tt => {
                        const tenBan = banByHoaDon[tt.thanhToanID] || '—';
                        const thoiGian = tt.thoiGian ? new Date(tt.thoiGian).toLocaleString('vi-VN') : '—';
                        const trangThaiColor = tt.trangThai === 'THANH_CONG' ? '#2e7d32'
                                             : tt.trangThai === 'THAT_BAI'   ? '#b71c1c'
                                             : '#e65100';
                        return `
                            <tr>
                                <td>${tt.thanhToanID}</td>
                                <td><strong>${escapeHtml(tenBan)}</strong></td>
                                <td>${escapeHtml(tt.phuongThuc || '—')}</td>
                                <td style="font-weight:700;">${fmtVNDAdmin(tt.soTien)}</td>
                                <td>${thoiGian}</td>
                                <td style="color:${trangThaiColor};font-weight:700;">${escapeHtml(tt.trangThai || '—')}</td>
                            </tr>`;
                    }).join('')}
                </tbody>
            </table>`;
        content.innerHTML = tableHtml;
    } catch(e) {
        content.innerHTML = '<div class="admin-empty">Không tải được dữ liệu thanh toán.</div>';
        console.error(e);
    }
}

// HELPER dùng chung cho form msg
function showFormMsg(el, text) {
    el.textContent = text;
    el.style.display = 'block';
}

function fmtVNDAdmin(value) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value || 0);
}

function escapeHtml(value) {
    return String(value)
        .replaceAll('&', '&amp;')
        .replaceAll('<', '&lt;')
        .replaceAll('>', '&gt;')
        .replaceAll('"', '&quot;')
        .replaceAll("'", '&#039;');
}

function unwrapApiData(response) {
    return response && typeof response === 'object' && 'data' in response
        ? response.data
        : response;
}