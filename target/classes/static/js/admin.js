// =============================================
// KIỂM TRA SESSION KHI VÀO TRANG ADMIN
// =============================================
(function() {
    const token = sessionStorage.getItem('adminToken');
    if (!token) {
        // Chưa đăng nhập → về trang chủ
        window.location.href = '/index.html';
    }
})();

// =============================================
// HIỂN THỊ TÊN ADMIN
// =============================================
window.addEventListener('DOMContentLoaded', function() {
    const name = sessionStorage.getItem('adminName') || 'Admin';
    const el = document.getElementById('adminName');
    if (el) el.textContent = '👤 ' + name;

    // Load thống kê
    loadStats();
});

// =============================================
// LOAD THỐNG KÊ TỪ API
// =============================================
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
                const data = await res.json();
                const el = document.getElementById(api.id);
                if (el) el.textContent = Array.isArray(data) ? data.length : '—';
            }
        } catch (e) {
            // Lỗi kết nối → giữ nguyên "—"
        }
    }
}

// =============================================
// ĐĂNG XUẤT
// =============================================
function logout() {
    if (confirm('Bạn có chắc muốn đăng xuất không?')) {
        sessionStorage.removeItem('adminToken');
        sessionStorage.removeItem('adminName');
        window.location.href = '/index.html';
    }
}
