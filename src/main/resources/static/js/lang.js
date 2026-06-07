// ===== i18next — 3 NGÔN NGỮ =====

const translations = {
    vi: {
        // NAV - INDEX
        "nav.intro":        "Giới thiệu",
        "nav.features":     "Tính năng",
        "nav.contact":      "Liên hệ",
        "nav.customer":     "Khách hàng",
        "nav.admin.login":  "Đăng nhập Admin",
        // HERO
        "hero.label":       "Hệ thống quản lý nhà hàng",
        "hero.title":       "Quán ăn tiện lợi & hiệu quả",
        "hero.desc":        "Quản lý bàn, đơn hàng, hóa đơn, nhân viên và khuyến mãi — tất cả trong một nền tảng duy nhất.",
        "hero.btn.admin":   "Vào trang quản trị",
        "hero.btn.features":"Xem tính năng",
        // ORBIT CARDS
        "fv.card1": "Thực đơn 30+ món",
        "fv.card2": "Đặt bàn online",
        "fv.card3": "Giao hàng tận nơi",
        // FEATURES
        "features.title":  "Tính năng nổi bật",
        "features.sub":    "Mọi thứ bạn cần để vận hành quán ăn chuyên nghiệp",
        "feat1.title": "Quản lý bàn",
        "feat1.desc":  "Theo dõi trạng thái từng bàn theo thời gian thực: trống, đang dùng, đã đặt trước.",
        "feat2.title": "Đơn hàng & Menu",
        "feat2.desc":  "Tạo và cập nhật đơn hàng nhanh chóng, quản lý thực đơn linh hoạt.",
        "feat3.title": "Hóa đơn & Thanh toán",
        "feat3.desc":  "Xuất hóa đơn tự động, hỗ trợ nhiều phương thức thanh toán.",
        "feat4.title": "Quản lý nhân viên",
        "feat4.desc":  "Phân ca làm việc, quản lý thông tin và lương nhân viên dễ dàng.",
        "feat5.title": "Khuyến mãi",
        "feat5.desc":  "Tạo và áp dụng chương trình khuyến mãi theo phần trăm hoặc giảm giá trực tiếp.",
        "feat6.title": "Khách hàng thân thiết",
        "feat6.desc":  "Lưu thông tin khách hàng, phân loại và chăm sóc khách hàng VIP.",
        // FOOTER
        "footer.copy": "© 2026 QuanAn67 — Hệ thống quản lý nhà hàng",
        // MODAL LOGIN
        "modal.login.title":    "Đăng nhập Admin",
        "modal.login.sub":      "Nhập tài khoản quản trị viên",
        "modal.login.email":    "Email",
        "modal.login.password": "Mật khẩu",
        "modal.login.btn":      "Đăng nhập",
        // ADMIN
        "admin.welcome.title": "Chào mừng trở lại!",
        "admin.welcome.sub":   "Tổng quan hệ thống quán ăn của bạn hôm nay.",
        "admin.logout":        "Đăng xuất",
        "stat.ban":       "Bàn đang phục vụ",
        "stat.donhang":   "Đơn hàng hôm nay",
        "stat.khachhang": "Khách hàng",
        "stat.nhanvien":  "Nhân viên",
        "quick.title":    "Quản lý nhanh",
        "lc.ban":         "Bàn",        "lc.ban.sub":     "Quản lý bàn",
        "lc.mon":         "Món ăn",     "lc.mon.sub":     "Thực đơn",
        "lc.donhang":     "Đơn hàng",  "lc.donhang.sub": "Xem đơn hàng",
        "lc.hoadon":      "Hóa đơn",   "lc.hoadon.sub":  "Danh sách hóa đơn",
        "lc.khach":       "Khách hàng","lc.khach.sub":    "Quản lý khách",
        "lc.nv":          "Nhân viên", "lc.nv.sub":      "Quản lý nhân viên",
        "lc.km":          "Khuyến mãi","lc.km.sub":       "Chương trình ưu đãi",
        "lc.tt":          "Thanh toán","lc.tt.sub":       "Lịch sử giao dịch",
        // KHÁCH HÀNG
        "kh.nav.home":     "Trang chủ",
        "kh.nav.menu":     "Thực đơn",
        "kh.nav.promo":    "Khuyến mãi",
        "kh.nav.cart":     "Giỏ hàng",
        "kh.nav.register": "Đăng ký / Tra cứu",
        "kh.hero.label":   "Chương trình thành viên",
        "kh.hero.title":   "Tích điểm – Nhận ưu đãi",
        "kh.hero.desc":    "Đăng ký thành viên miễn phí, tích điểm mỗi lần đặt món và nhận ưu đãi độc quyền theo hạng.",
        "kh.btn.register": "Đăng ký ngay",
        "kh.btn.lookup":   "Tra cứu điểm",
        "kh.hang.dong":      "Đồng",     "kh.hang.dong.diem": "0 – 99 điểm",   "kh.hang.dong.uu": "Ưu đãi sinh nhật",
        "kh.hang.bac":       "Bạc",      "kh.hang.bac.diem":  "100 – 499 điểm","kh.hang.bac.uu":  "Giảm 5% mỗi đơn",
        "kh.hang.vang":      "Vàng",     "kh.hang.vang.diem": "500 – 999 điểm","kh.hang.vang.uu": "Giảm 10% + ưu tiên",
        "kh.hang.kim":       "Kim Cương","kh.hang.kim.diem":  "1000+ điểm",    "kh.hang.kim.uu":  "Giảm 15% + VIP",
        "kh.promo.title":  "Khuyến mãi đang diễn ra",
        "kh.promo.sub":    "Ưu đãi hấp dẫn dành riêng cho khách hàng thành viên",
        "kh.menu.title":   "Thực đơn hôm nay",
        "kh.menu.sub":     "Chọn món yêu thích và thêm vào giỏ hàng",
        "kh.tab.food":     "Đồ ăn",
        "kh.tab.drink":    "Đồ uống",
        "kh.cart.title":   "Giỏ hàng",
        "kh.cart.empty":   "Giỏ hàng trống",
        "kh.cart.total":   "Tổng cộng:",
        "kh.cart.order":   "Đặt món →",
        "kh.modal.reg.title":   "Đăng ký thành viên",
        "kh.modal.reg.sub":     "Tạo tài khoản miễn phí",
        "kh.modal.reg.name":    "Họ tên",
        "kh.modal.reg.phone":   "Số điện thoại",
        "kh.modal.reg.email":   "Email",
        "kh.modal.reg.address": "Địa chỉ",
        "kh.modal.reg.btn":     "Đăng ký ngay",
        "kh.modal.lookup.title": "Tra cứu điểm",
        "kh.modal.lookup.sub":   "Nhập SĐT để xem điểm & hạng",
        "kh.modal.lookup.phone": "Số điện thoại",
        "kh.modal.lookup.btn":   "Tra cứu",
        "kh.tc.diem": "Điểm tích lũy:",
        "kh.tc.hang": "Hạng:"
    },

    en: {
        "nav.intro":        "About",
        "nav.features":     "Features",
        "nav.contact":      "Contact",
        "nav.customer":     "Customers",
        "nav.admin.login":  "Admin Login",
        "hero.label":       "Restaurant Management System",
        "hero.title":       "Smart & Efficient Restaurant",
        "hero.desc":        "Manage tables, orders, invoices, staff and promotions — all in one platform.",
        "hero.btn.admin":   "Go to Admin",
        "hero.btn.features":"See Features",
        "fv.card1": "30+ dishes available",
        "fv.card2": "Online reservation",
        "fv.card3": "Home delivery",
        "features.title":  "Key Features",
        "features.sub":    "Everything you need to run a professional restaurant",
        "feat1.title": "Table Management",
        "feat1.desc":  "Track each table status in real time: empty, occupied, reserved.",
        "feat2.title": "Orders & Menu",
        "feat2.desc":  "Create and update orders quickly, manage food and drink menus flexibly.",
        "feat3.title": "Invoice & Payment",
        "feat3.desc":  "Auto-generate invoices, support multiple payment methods.",
        "feat4.title": "Staff Management",
        "feat4.desc":  "Schedule shifts, manage staff info and payroll easily.",
        "feat5.title": "Promotions",
        "feat5.desc":  "Create and apply promotions by percentage or direct discount.",
        "feat6.title": "Loyalty Customers",
        "feat6.desc":  "Save customer info, classify and care for VIP customers.",
        "footer.copy": "© 2026 QuanAn67 — Restaurant Management System",
        "modal.login.title":    "Admin Login",
        "modal.login.sub":      "Enter admin credentials",
        "modal.login.email":    "Email",
        "modal.login.password": "Password",
        "modal.login.btn":      "Login",
        "admin.welcome.title": "Welcome back!",
        "admin.welcome.sub":   "Today's overview of your restaurant system.",
        "admin.logout":        "Logout",
        "stat.ban":       "Tables in service",
        "stat.donhang":   "Today's orders",
        "stat.khachhang": "Customers",
        "stat.nhanvien":  "Staff",
        "quick.title":    "Quick Access",
        "lc.ban":         "Tables",      "lc.ban.sub":     "Manage tables",
        "lc.mon":         "Food",        "lc.mon.sub":     "Menu",
        "lc.donhang":     "Orders",      "lc.donhang.sub": "View orders",
        "lc.hoadon":      "Invoices",    "lc.hoadon.sub":  "Invoice list",
        "lc.khach":       "Customers",   "lc.khach.sub":   "Manage customers",
        "lc.nv":          "Staff",       "lc.nv.sub":      "Manage staff",
        "lc.km":          "Promotions",  "lc.km.sub":      "Promo programs",
        "lc.tt":          "Payments",    "lc.tt.sub":      "Transaction history",
        "kh.nav.home":     "Home",
        "kh.nav.menu":     "Menu",
        "kh.nav.promo":    "Promotions",
        "kh.nav.cart":     "Cart",
        "kh.nav.register": "Register / Lookup",
        "kh.hero.label":   "Membership Program",
        "kh.hero.title":   "Earn Points – Get Rewards",
        "kh.hero.desc":    "Join for free, earn points every order, and unlock exclusive tier rewards.",
        "kh.btn.register": "Register Now",
        "kh.btn.lookup":   "Check Points",
        "kh.hang.dong":      "Bronze",   "kh.hang.dong.diem": "0 – 99 pts",    "kh.hang.dong.uu": "Birthday reward",
        "kh.hang.bac":       "Silver",   "kh.hang.bac.diem":  "100 – 499 pts", "kh.hang.bac.uu":  "5% off per order",
        "kh.hang.vang":      "Gold",     "kh.hang.vang.diem": "500 – 999 pts", "kh.hang.vang.uu": "10% off + priority",
        "kh.hang.kim":       "Diamond",  "kh.hang.kim.diem":  "1000+ pts",     "kh.hang.kim.uu":  "15% off + VIP",
        "kh.promo.title":  "Active Promotions",
        "kh.promo.sub":    "Exclusive deals for our members",
        "kh.menu.title":   "Today's Menu",
        "kh.menu.sub":     "Pick your favorites and add to cart",
        "kh.tab.food":     "Food",
        "kh.tab.drink":    "Drinks",
        "kh.cart.title":   "Cart",
        "kh.cart.empty":   "Your cart is empty",
        "kh.cart.total":   "Total:",
        "kh.cart.order":   "Place Order →",
        "kh.modal.reg.title":   "Join Membership",
        "kh.modal.reg.sub":     "Create a free account",
        "kh.modal.reg.name":    "Full Name",
        "kh.modal.reg.phone":   "Phone Number",
        "kh.modal.reg.email":   "Email",
        "kh.modal.reg.address": "Address",
        "kh.modal.reg.btn":     "Register Now",
        "kh.modal.lookup.title": "Check Points",
        "kh.modal.lookup.sub":   "Enter phone number to view points & tier",
        "kh.modal.lookup.phone": "Phone Number",
        "kh.modal.lookup.btn":   "Search",
        "kh.tc.diem": "Accumulated points:",
        "kh.tc.hang": "Tier:"
    },

    ja: {
        "nav.intro":        "紹介",
        "nav.features":     "機能",
        "nav.contact":      "お問い合わせ",
        "nav.customer":     "お客様",
        "nav.admin.login":  "管理者ログイン",
        "hero.label":       "レストラン管理システム",
        "hero.title":       "便利で効率的なレストラン",
        "hero.desc":        "テーブル、注文、請求書、スタッフ、プロモーションをすべて一つのプラットフォームで管理。",
        "hero.btn.admin":   "管理画面へ",
        "hero.btn.features":"機能を見る",
        "fv.card1": "30種以上のメニュー",
        "fv.card2": "オンライン予約",
        "fv.card3": "デリバリー対応",
        "features.title":  "主な機能",
        "features.sub":    "プロのレストラン運営に必要なすべて",
        "feat1.title": "テーブル管理",
        "feat1.desc":  "各テーブルの状態をリアルタイムで追跡：空席、使用中、予約済み。",
        "feat2.title": "注文とメニュー",
        "feat2.desc":  "注文を素早く作成・更新し、メニューを柔軟に管理。",
        "feat3.title": "請求書と支払い",
        "feat3.desc":  "請求書を自動生成、現金・振込など多様な支払い方法に対応。",
        "feat4.title": "スタッフ管理",
        "feat4.desc":  "シフト管理、スタッフ情報・給与管理を簡単に。",
        "feat5.title": "プロモーション",
        "feat5.desc":  "割引率または直接割引でプロモーションを作成・適用。",
        "feat6.title": "ロイヤルカスタマー",
        "feat6.desc":  "顧客情報を保存し、VIP顧客を分類・ケア。",
        "footer.copy": "© 2026 QuanAn67 — レストラン管理システム",
        "modal.login.title":    "管理者ログイン",
        "modal.login.sub":      "管理者アカウントを入力",
        "modal.login.email":    "メール",
        "modal.login.password": "パスワード",
        "modal.login.btn":      "ログイン",
        "admin.welcome.title": "おかえりなさい！",
        "admin.welcome.sub":   "本日のレストランシステムの概要。",
        "admin.logout":        "ログアウト",
        "stat.ban":       "対応中テーブル",
        "stat.donhang":   "本日の注文",
        "stat.khachhang": "お客様",
        "stat.nhanvien":  "スタッフ",
        "quick.title":    "クイックアクセス",
        "lc.ban":         "テーブル",    "lc.ban.sub":     "テーブル管理",
        "lc.mon":         "料理",        "lc.mon.sub":     "メニュー",
        "lc.donhang":     "注文",        "lc.donhang.sub": "注文を見る",
        "lc.hoadon":      "請求書",      "lc.hoadon.sub":  "請求書一覧",
        "lc.khach":       "お客様",      "lc.khach.sub":   "顧客管理",
        "lc.nv":          "スタッフ",    "lc.nv.sub":      "スタッフ管理",
        "lc.km":          "プロモーション","lc.km.sub":     "特典プログラム",
        "lc.tt":          "支払い",      "lc.tt.sub":      "取引履歴",
        "kh.nav.home":     "ホーム",
        "kh.nav.menu":     "メニュー",
        "kh.nav.promo":    "プロモーション",
        "kh.nav.cart":     "カート",
        "kh.nav.register": "登録 / 照会",
        "kh.hero.label":   "会員プログラム",
        "kh.hero.title":   "ポイントを貯めて – 特典をゲット",
        "kh.hero.desc":    "無料で会員登録し、注文ごとにポイントを貯めて限定特典をゲット。",
        "kh.btn.register": "今すぐ登録",
        "kh.btn.lookup":   "ポイント確認",
        "kh.hang.dong":      "ブロンズ",  "kh.hang.dong.diem": "0〜99pt",    "kh.hang.dong.uu": "誕生日特典",
        "kh.hang.bac":       "シルバー",  "kh.hang.bac.diem":  "100〜499pt", "kh.hang.bac.uu":  "5%割引",
        "kh.hang.vang":      "ゴールド",  "kh.hang.vang.diem": "500〜999pt", "kh.hang.vang.uu": "10%割引+優先",
        "kh.hang.kim":       "ダイヤモンド","kh.hang.kim.diem": "1000pt以上", "kh.hang.kim.uu":  "15%割引+VIP",
        "kh.promo.title":  "開催中のプロモーション",
        "kh.promo.sub":    "会員限定のお得な特典",
        "kh.menu.title":   "本日のメニュー",
        "kh.menu.sub":     "お好みの料理をカートに追加",
        "kh.tab.food":     "料理",
        "kh.tab.drink":    "ドリンク",
        "kh.cart.title":   "カート",
        "kh.cart.empty":   "カートは空です",
        "kh.cart.total":   "合計：",
        "kh.cart.order":   "注文する →",
        "kh.modal.reg.title":   "会員登録",
        "kh.modal.reg.sub":     "無料アカウントを作成",
        "kh.modal.reg.name":    "氏名",
        "kh.modal.reg.phone":   "電話番号",
        "kh.modal.reg.email":   "メール",
        "kh.modal.reg.address": "住所",
        "kh.modal.reg.btn":     "今すぐ登録",
        "kh.modal.lookup.title": "ポイント照会",
        "kh.modal.lookup.sub":   "電話番号を入力してポイントと会員ランクを確認",
        "kh.modal.lookup.phone": "電話番号",
        "kh.modal.lookup.btn":   "検索",
        "kh.tc.diem": "累計ポイント：",
        "kh.tc.hang": "ランク："
    }
};

// ── KHỞI TẠO i18next ──
i18next.init({
    lng: localStorage.getItem('lang') || 'vi',
    resources: {
        vi: { translation: translations.vi },
        en: { translation: translations.en },
        ja: { translation: translations.ja }
    },
    interpolation: { escapeValue: false }
}, function() {
    applyTranslations();
    highlightActiveLangBtn();
});

// Áp dụng bản dịch lên tất cả element có data-i18n
function applyTranslations() {
    document.querySelectorAll('[data-i18n]').forEach(function(el) {
        var key = el.getAttribute('data-i18n');
        var translated = i18next.t(key);
        // placeholder thì dùng attr, còn lại innerHTML
        if (el.hasAttribute('placeholder')) {
            el.setAttribute('placeholder', translated);
        } else {
            el.innerHTML = translated;
        }
    });
}

// Chuyển ngôn ngữ khi bấm nút
function switchLang(lang, btn) {
    localStorage.setItem('lang', lang);
    i18next.changeLanguage(lang, function() {
        applyTranslations();
    });
    document.querySelectorAll('.lang-btn').forEach(function(b) {
        b.classList.remove('active');
    });
    if (btn) btn.classList.add('active');
}

// Highlight nút đúng với ngôn ngữ hiện tại khi load trang
function highlightActiveLangBtn() {
    var currentLang = localStorage.getItem('lang') || 'vi';
    document.querySelectorAll('.lang-btn').forEach(function(btn) {
        var onclick = btn.getAttribute('onclick') || '';
        if (onclick.includes("'" + currentLang + "'")) {
            btn.classList.add('active');
        } else {
            btn.classList.remove('active');
        }
    });
}