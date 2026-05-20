// KIỂM TRA SESSION KHI VÀO TRANG ADMIN
(function() {
    const token = sessionStorage.getItem('adminToken');
    if (!token) {
        // Chưa đăng nhập → về trang chủ
        window.location.href = '/index.html';
    }
})();

// HIỂN THỊ TÊN ADMIN
window.addEventListener('DOMContentLoaded', function() {
    const name = sessionStorage.getItem('adminName') || 'Admin';
    const el = document.getElementById('adminName');
    if (el) el.textContent = '👤 ' + name;

    // Load thống kê
    loadStats();
    setupQuickLinks();
});

// LOAD THỐNG KÊ TỪ API
async function loadStats() {
    const token = sessionStorage.getItem('adminToken');
    const headers = { 'Authorization': 'Bearer ' + token };

    // Đếm số bàn, khách hàng, nhân viên từ API có sẵn
    const apis = [
        { url: '/api/ban',       id: 'statBan' },
        { url: '/api/donhang',   id: 'statDonHang' },
        { url: '/api/khachhang', id: 'statKhachHang' },
        { url: '/api/nhanvien',  id: 'statNhanVien' },
    ];

    for (const api of apis) {
        try {
            const res = await fetch(api.url, { headers });
            if (res.ok) {
                const data = unwrapApiData(await res.json());
                const el = document.getElementById(api.id);
                if (el) el.textContent = Array.isArray(data) ? data.length : '—';
            }
        } catch (e) {
            // Lỗi kết nối → giữ nguyên "—"
        }
    }
}

// ĐĂNG XUẤT
function logout() {
    if (confirm('Bạn có chắc muốn đăng xuất không?')) {
        sessionStorage.removeItem('adminToken');
        sessionStorage.removeItem('adminName');
        window.location.href = '/index.html';
    }
}

// QUAN LY NHANH
const quickManagers = {
    '/api/ban': { title: 'Quản lý bàn', sub: 'Danh sách bàn hiện có' },
    '/api/monan': { title: 'Quản lý món ăn', sub: 'Xem, thêm và xóa món trong menu', monAn: true },
    '/api/donhang': { title: 'Quản lý đơn hàng', sub: 'Danh sách đơn hàng' },
    '/api/hoadon': { title: 'Quản lý hóa đơn', sub: 'Danh sách hóa đơn' },
    '/api/khachhang': { title: 'Quản lý khách hàng', sub: 'Danh sách khách hàng thành viên' },
    '/api/nhanvien': { title: 'Quản lý nhân viên', sub: 'Danh sách nhân viên' },
    '/api/khuyenmai': { title: 'Quản lý khuyến mãi', sub: 'Danh sách chương trình ưu đãi' },
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
    document.getElementById('managerSub').textContent = config.sub;
    document.getElementById('adminManager').style.display = 'block';
    document.getElementById('monForm').style.display = config.monAn ? 'grid' : 'none';

    if (config.monAn) {
        await loadMenuOptions();
        await loadMonAnAdmin();
    } else {
        await loadGenericManager(api);
    }
    document.getElementById('adminManager').scrollIntoView({ behavior: 'smooth', block: 'start' });
}

async function loadMenuOptions() {
    const select = document.getElementById('monMenu');
    if (!select || select.options.length) return;

    try {
        const menus = unwrapApiData(await fetch('/api/menu').then(r => r.json()));
        select.innerHTML = menus.map(m => `<option value="${m.menuID}">${escapeHtml(m.tenMenu || 'Menu')}</option>`).join('');
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

        content.innerHTML = `
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Món ăn</th>
                        <th>Giá</th>
                        <th>Trạng thái</th>
                        <th>Menu</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    ${mons.map(mon => `
                        <tr>
                            <td>${mon.monID || ''}</td>
                            <td>
                                <strong>${escapeHtml(mon.tenMon || '')}</strong>
                                <span>${escapeHtml(mon.moTa || '')}</span>
                            </td>
                            <td>${fmtVNDAdmin(mon.gia || 0)}</td>
                            <td>${escapeHtml(mon.trangThai || '')}</td>
                            <td>${escapeHtml(mon.menu?.tenMenu || '')}</td>
                            <td><button class="admin-danger" onclick="xoaMonAnAdmin(${mon.monID})">Xóa</button></td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>`;
    } catch {
        content.innerHTML = '<div class="admin-empty">Không tải được danh sách món ăn.</div>';
    }
}

async function themMonAnAdmin() {
    const payload = {
        tenMon: document.getElementById('monTen').value.trim(),
        gia: Number(document.getElementById('monGia').value || 0),
        menuID: document.getElementById('monMenu').value,
        phanLoai: document.getElementById('monPhanLoai').value,
        trangThai: document.getElementById('monTrangThai').value,
        loai: document.getElementById('monLoai').value.trim(),
        hinhAnh: document.getElementById('monHinhAnh').value.trim(),
        moTa: document.getElementById('monMoTa').value.trim()
    };

    if (!payload.tenMon || !payload.gia || !payload.menuID) {
        alert('Vui lòng nhập tên món, giá và menu.');
        return;
    }

    if (payload.phanLoai === 'douong') {
        payload.loai = ['S', 'M', 'L'].includes(payload.loai.toUpperCase()) ? payload.loai.toUpperCase() : 'M';
    }

    try {
        const res = await fetch('/api/monan', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        if (!res.ok) throw new Error();

        ['monTen', 'monGia', 'monLoai', 'monHinhAnh', 'monMoTa'].forEach(id => document.getElementById(id).value = '');
        await loadMonAnAdmin();
        await loadStats();
    } catch {
        alert('Không thêm được món ăn. Kiểm tra dữ liệu hoặc server.');
    }
}

async function xoaMonAnAdmin(id) {
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

async function loadGenericManager(api) {
    const content = document.getElementById('managerContent');
    content.innerHTML = '<div class="admin-loading">Đang tải dữ liệu...</div>';

    try {
        const data = unwrapApiData(await fetch(api).then(r => r.json()));
        const rows = Array.isArray(data) ? data : [data];
        if (!rows.length) {
            content.innerHTML = '<div class="admin-empty">Chưa có dữ liệu.</div>';
            return;
        }

        const keys = Object.keys(rows[0]).filter(k => typeof rows[0][k] !== 'object').slice(0, 6);
        content.innerHTML = `
            <table class="admin-table">
                <thead><tr>${keys.map(k => `<th>${escapeHtml(k)}</th>`).join('')}</tr></thead>
                <tbody>
                    ${rows.map(row => `<tr>${keys.map(k => `<td>${escapeHtml(row[k] ?? '')}</td>`).join('')}</tr>`).join('')}
                </tbody>
            </table>`;
    } catch {
        content.innerHTML = '<div class="admin-empty">Không tải được dữ liệu.</div>';
    }
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
