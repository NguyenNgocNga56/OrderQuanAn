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

// MANAGER BÀN (toggle + xóa)
async function loadBanManager() {
    const content = document.getElementById('managerContent');
    content.innerHTML = '<div class="admin-loading">Đang tải danh sách bàn...</div>';

    try {
        const data = unwrapApiData(await fetch('/api/ban').then(r => r.json()));
        const rows = Array.isArray(data) ? data : [];

        if (!rows.length) {
            content.innerHTML = '<div class="admin-empty">Chưa có bàn nào.</div>';
            return;
        }

        const admin = isAdmin();

        content.innerHTML = `
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tên bàn</th>
                        <th>Vị trí</th>
                        <th>Sức chứa</th>
                        <th>Trạng thái</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    ${rows.map(ban => {
                        const laTrong = (ban.trangThai || '').toUpperCase() === 'TRONG';
                        const trangThaiLabel = laTrong
                            ? '<span style="color:#2e7d32;font-weight:600;"> Trống</span>'
                            : '<span style="color:#c62828;font-weight:600;"> Có khách</span>';

                        // Nút toggle — cả ADMIN lẫn NHAN_VIEN đều thấy
                        const btnToggle = `
                            <button onclick="toggleBan(${ban.banID})"
                                style="padding:5px 12px;border-radius:6px;border:none;cursor:pointer;
                                       font-weight:600;margin-right:6px;color:white;
                                       background:${laTrong ? '#43a047' : '#e53935'};">
                                ${laTrong ? ' Đánh dấu có khách' : ' Trả bàn về trống'}
                            </button>`;

                        // Nút Xóa — chỉ ADMIN
                        const btnXoa = admin ? `
                            <button onclick="xoaBan(${ban.banID})"
                                style="padding:5px 12px;border-radius:6px;border:none;cursor:pointer;
                                       font-weight:600;background:#b71c1c;color:white;">
                                 Xóa
                            </button>` : '';

                        return `
                            <tr id="ban-row-${ban.banID}">
                                <td>${ban.banID}</td>
                                <td><strong>${escapeHtml(ban.tenBan || '')}</strong></td>
                                <td>${escapeHtml(ban.viTri || '—')}</td>
                                <td>${ban.soChoNgoi} chỗ</td>
                                <td>${trangThaiLabel}</td>
                                <td>${btnToggle}${btnXoa}</td>
                            </tr>`;
                    }).join('')}
                </tbody>
            </table>`;
    } catch {
        content.innerHTML = '<div class="admin-empty">Không tải được danh sách bàn.</div>';
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

// MANAGER KHUYẾN MÃI (ADMIN thêm / xóa)
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
                <h3 style="margin:0 0 12px;font-size:1rem;">➕ Thêm khuyến mãi mới</h3>
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
                        ${admin ? '<th></th>' : ''}
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
                            ${admin ? `<td>
                                <button onclick="xoaKhuyenMai(${km.khuyenMaiID})"
                                    style="padding:5px 12px;border-radius:6px;border:none;cursor:pointer;
                                           background:#b71c1c;color:white;font-weight:600;">
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
        const data = unwrapApiData(await fetch(api).then(r => r.json()));
        const rows = Array.isArray(data) ? data : [data].filter(Boolean);
        if (!rows.length) {
            content.innerHTML = '<div class="admin-empty">Chưa có dữ liệu.</div>';
            return;
        }
        const keys = Object.keys(rows[0]).filter(k => typeof rows[0][k] !== 'object').slice(0, 6);
        content.innerHTML = `
            <table class="admin-table">
                <thead>
                    <tr>${keys.map(k => `<th>${escapeHtml(k)}</th>`).join('')}</tr>
                </thead>
                <tbody>
                    ${rows.map(row =>
                        `<tr>${keys.map(k => `<td>${escapeHtml(row[k] ?? '')}</td>`).join('')}</tr>`
                    ).join('')}
                </tbody>
            </table>`;
    } catch {
        content.innerHTML = '<div class="admin-empty">Không tải được dữ liệu.</div>';
    }
}


// HELPERS

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
