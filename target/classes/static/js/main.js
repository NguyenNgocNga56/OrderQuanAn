// MODAL ĐĂNG NHẬP
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

// TOGGLE HIỆN/ẨN MẬT KHẨU
function togglePassword() {
    const input = document.getElementById('loginPassword');
    const btn = document.querySelector('.toggle-pw');
    if (input.type === 'password') {
        input.type = 'text';
        btn.textContent = 'X';
    } else {
        input.type = 'password';
        btn.textContent = '👁';
    }
}

// XỬ LÝ ĐĂNG NHẬP

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
            setLoginMsg(' Đăng nhập thành công!', 'success');
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

//   FOOD VISUAL ORBIT — Hero animation
(function initFoodOrbit() {
    var orbitEl = document.getElementById('fvOrbit');
    if (!orbitEl) return;

    var foods = [
        {
            name: 'Cơm cà ri',
            src: '/img/com-cari.jpg',
            mainSrc: '/img/com-cari.jpg'
        },
        {
            name: 'Lẩu bò',
            src: '/img/laubo.jpg',
            mainSrc: '/img/laubo.jpg'
        },
        {
            name: 'Ramen',
            src: '/img/ramen.jpg',
            mainSrc: '/img/ramen.jpg'
        },
        {
            name: 'Sashimi',
            src: '/img/sashimi.jpg',
            mainSrc: '/img/sashimi.jpg'
        },
        {
            name: 'Takoyaki',
            src: '/img/takoyaki.jpg',
            mainSrc: '/img/takoyaki.jpg'
        },
        {
            name: 'Bingsu',
            src: '/img/bingsu.jpg',
            mainSrc: '/img/bingsu.jpg'
        }
    ];

    var RADIUS = 158;   // khoảng cách tâm → ảnh orbit (px)
    var SPEED  = 0.15;  // tốc độ xoay (độ/frame) — tăng số này để nhanh hơn

    var items = [];
    var angle = 0;

    // Tạo DOM cho từng món
    foods.forEach(function(food, i) {
        var el = document.createElement('div');
        el.className = 'fv-item';
        el.innerHTML =
            '<img src="' + food.src + '" alt="' + food.name + '" loading="lazy">' +
            '<div class="fv-tooltip">' + food.name + '</div>';

        // Click → đổi ảnh chính giữa
        el.addEventListener('click', function() {
            var mainImg   = document.getElementById('fvMainImg');
            var mainLabel = document.getElementById('fvMainLabel');
            if (mainImg)   mainImg.src = food.mainSrc;
            if (mainLabel) mainLabel.textContent = food.name;
        });

        orbitEl.appendChild(el);
        items.push({ el: el, baseAngle: (360 / foods.length) * i });
    });

    // Click ảnh giữa → reset về mặc định
    var DEFAULT_SRC   = document.getElementById('fvMainImg')  ? document.getElementById('fvMainImg').src   : '';
    var DEFAULT_LABEL = document.getElementById('fvMainLabel') ? document.getElementById('fvMainLabel').textContent : '';

    var centerEl = document.getElementById('fvCenter');
    if (centerEl) {
        centerEl.style.cursor = 'pointer';
        centerEl.title = 'Nhấn để quay lại mặc định';
        centerEl.addEventListener('click', function() {
            var mainImg   = document.getElementById('fvMainImg');
            var mainLabel = document.getElementById('fvMainLabel');
            if (mainImg)   mainImg.src = DEFAULT_SRC;
            if (mainLabel) mainLabel.textContent = DEFAULT_LABEL;
        });
    }

    // Animation loop
    function tick() {
        angle += SPEED;
        items.forEach(function(item) {
            var rad = ((item.baseAngle + angle) * Math.PI) / 180;
            var tx  = Math.cos(rad) * RADIUS;
            var ty  = Math.sin(rad) * RADIUS;
            item.el.style.transform =
                'translate(calc(' + tx + 'px - 50px), calc(' + ty + 'px - 50px))';
        });
        raf = requestAnimationFrame(tick);
    }
    tick();
})();