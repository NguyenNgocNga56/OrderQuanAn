// Check admin session before showing the page.
(function() {
    const token = sessionStorage.getItem('adminToken');
    if (!token) {
        window.location.href = '/index.html';
    }
})();

window.addEventListener('DOMContentLoaded', function() {
    const name = sessionStorage.getItem('adminName') || 'Admin';
    const el = document.getElementById('adminName');
    if (el) el.textContent = name;

    loadStats();
    setupQuickLinks();
});

async function loadStats() {
    const token = sessionStorage.getItem('adminToken');
    const headers = { 'Authorization': 'Bearer ' + token };
    const apis = [
        { url: '/api/ban', id: 'statBan', count: rows => rows.filter(isServingTable).length },
        { url: '/orders', id: 'statDonHang', count: rows => rows.filter(isTodayOrder).length },
        { url: '/api/khachhang', id: 'statKhachHang', count: rows => rows.length },
        { url: '/api/nhanvien', id: 'statNhanVien', count: rows => rows.length },
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
    const status = removeVietnameseMarks(String(ban.trangThai || '')).toLowerCase();
    return status.includes('dang dung')
        || status.includes('dang phuc vu')
        || status.includes('co khach')
        || status.includes('co_khach');
}

function isTodayOrder(order) {
    if (!order.ngayDat) return false;
    const orderDate = new Date(order.ngayDat);
    const today = new Date();
    return orderDate.getFullYear() === today.getFullYear()
        && orderDate.getMonth() === today.getMonth()
        && orderDate.getDate() === today.getDate();
}

function logout() {
    if (confirm('Ban co chac muon dang xuat khong?')) {
        sessionStorage.removeItem('adminToken');
        sessionStorage.removeItem('adminName');
        window.location.href = '/index.html';
    }
}

const quickManagers = {
    '/api/ban': { title: 'Quan ly ban', sub: 'Danh sach ban hien co' },
    '/api/monan': { title: 'Quan ly mon an', sub: 'Xem, them va xoa mon trong menu', monAn: true },
    '/api/donhang': { title: 'Quan ly don hang', sub: 'Danh sach don hang', apiUrl: '/orders' },
    '/api/hoadon': { title: 'Quan ly hoa don', sub: 'Danh sach hoa don' },
    '/api/khachhang': { title: 'Quan ly khach hang', sub: 'Danh sach khach hang thanh vien' },
    '/api/nhanvien': { title: 'Quan ly nhan vien', sub: 'Danh sach nhan vien' },
    '/api/khuyenmai': { title: 'Quan ly khuyen mai', sub: 'Danh sach chuong trinh uu dai' },
    '/api/thanhtoan': { title: 'Quan ly thanh toan', sub: 'Lich su giao dich' }
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
        await loadGenericManager(config.apiUrl || api);
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
        select.innerHTML = '<option value="">Khong tai duoc menu</option>';
    }
}

async function loadMonAnAdmin() {
    const content = document.getElementById('managerContent');
    content.innerHTML = '<div class="admin-loading">Dang tai mon an...</div>';

    try {
        const mons = unwrapApiData(await fetch('/api/monan').then(r => r.json()));
        if (!Array.isArray(mons) || !mons.length) {
            content.innerHTML = '<div class="admin-empty">Chua co mon an nao.</div>';
            return;
        }

        content.innerHTML = `
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Mon an</th>
                        <th>Gia</th>
                        <th>Trang thai</th>
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
                            <td><button class="admin-danger" onclick="xoaMonAnAdmin(${mon.monID})">Xoa</button></td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>`;
    } catch {
        content.innerHTML = '<div class="admin-empty">Khong tai duoc danh sach mon an.</div>';
    }
}

async function themMonAnAdmin() {
    const phanLoai = document.getElementById('monPhanLoai').value;
    const payload = {
        tenMon: document.getElementById('monTen').value.trim(),
        gia: Number(document.getElementById('monGia').value || 0),
        menuID: phanLoai === 'douong' ? 2 : 1,
        phanLoai: phanLoai,
        trangThai: document.getElementById('monTrangThai').value,
        loai: document.getElementById('monLoai').value.trim(),
        hinhAnh: document.getElementById('monHinhAnh').value.trim(),
        moTa: document.getElementById('monMoTa').value.trim()
    };

    if (!payload.tenMon || !payload.gia || !payload.menuID) {
        alert('Vui long nhap ten mon, gia va menu.');
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
        alert('Khong them duoc mon an. Kiem tra du lieu hoac server.');
    }
}

async function xoaMonAnAdmin(id) {
    if (!confirm('Ban co chac muon xoa mon nay khong?')) return;
    try {
        const res = await fetch(`/api/monan/${id}`, { method: 'DELETE' });
        if (!res.ok) throw new Error();
        await loadMonAnAdmin();
        await loadStats();
    } catch {
        alert('Khong xoa duoc mon nay. Co the mon da nam trong don hang.');
    }
}

async function loadGenericManager(api) {
    const content = document.getElementById('managerContent');
    content.innerHTML = '<div class="admin-loading">Dang tai du lieu...</div>';

    try {
        const data = unwrapApiData(await fetch(api).then(r => r.json()));
        const rows = Array.isArray(data) ? data : [data].filter(Boolean);
        if (!rows.length) {
            content.innerHTML = '<div class="admin-empty">Chua co du lieu.</div>';
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
        content.innerHTML = '<div class="admin-empty">Khong tai duoc du lieu.</div>';
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

function removeVietnameseMarks(value) {
    return value.normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '')
        .replace(/đ/g, 'd')
        .replace(/Đ/g, 'D');
}
