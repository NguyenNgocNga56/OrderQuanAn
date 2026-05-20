// =============================================
// MODAL ĐĂNG NHẬP
// =============================================

function openLoginModal() {
    document.getElementById('loginModal').classList.add('active');
    document.body.style.overflow = 'hidden';
    setTimeout(() => document.getElementById('loginEmail').focus(), 100);
}

function closeLoginModal(event) {
    if (event && event.target !== event.currentTarget) return;
    document.getElementById('loginModal').classList.remove('active');
    document.body.style.overflow = '';
    clearLoginForm();
}

function clearLoginForm() {
    document.getElementById('loginEmail').value = '';
    document.getElementById('loginPassword').value = '';
    setLoginMsg('', '');
}

// Đóng modal bằng phím ESC
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') closeLoginModal();
});

// =============================================
// TOGGLE HIỆN/ẨN MẬT KHẨU
// =============================================

function togglePassword() {
    const input = document.getElementById('loginPassword');
    const btn = document.querySelector('.toggle-pw');
    if (input.type === 'password') {
        input.type = 'text';
        btn.textContent = '🙈';
    } else {
        input.type = 'password';
        btn.textContent = '👁';
    }
}

// =============================================
// XỬ LÝ ĐĂNG NHẬP
// =============================================

function setLoginMsg(msg, type) {
    const el = document.getElementById('loginMsg');
    el.textContent = msg;
    el.className = 'form-msg ' + type;
}

async function handleLogin(event) {
    event.preventDefault();

    const email    = document.getElementById('loginEmail').value.trim();
    const password = document.getElementById('loginPassword').value;
    const btn      = document.getElementById('submitBtn');

    if (!email || !password) {
        setLoginMsg('Vui lòng nhập đầy đủ thông tin.', 'error');
        return;
    }

    btn.disabled = true;
    btn.textContent = 'Đang kiểm tra...';
    setLoginMsg('', '');

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        const data = await response.json();

        if (response.ok && data.success) {
            setLoginMsg('✅ Đăng nhập thành công!', 'success');
            // Lưu token/session
            if (data.token) sessionStorage.setItem('adminToken', data.token);
            if (data.hoTen) sessionStorage.setItem('adminName', data.hoTen);
            setTimeout(() => {
                window.location.href = '/admin.html';
            }, 800);
        } else {
            setLoginMsg(data.message || 'Email hoặc mật khẩu không đúng.', 'error');
            btn.disabled = false;
            btn.textContent = 'Đăng nhập';
        }

    } catch (err) {
        setLoginMsg('Lỗi kết nối máy chủ. Vui lòng thử lại.', 'error');
        btn.disabled = false;
        btn.textContent = 'Đăng nhập';
    }
}
