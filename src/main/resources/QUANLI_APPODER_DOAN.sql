-- ============================================================
--  CHON CHE DO:
--  1. Neu muon TAO MOI DATABASE tu dau → bo comment phan A, comment phan B
--  2. Neu DB DA TON TAI, chi muon PATCH them column Role → bo comment phan B, comment phan A
-- ============================================================

-- ============================================================
--  PHAN A: TAO MOI DATABASE (comment lai neu chi muon patch)
-- ============================================================
USE master;
GO

IF DB_ID('QUANLI_APPODER_DOAN') IS NOT NULL
BEGIN
    ALTER DATABASE QUANLI_APPODER_DOAN SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE QUANLI_APPODER_DOAN;
END
GO

CREATE DATABASE QUANLI_APPODER_DOAN;
GO

USE QUANLI_APPODER_DOAN;
GO

--  TAO BANG
-- 1. MENU
CREATE TABLE Menu (
                      MenuID      INT           IDENTITY(1,1) PRIMARY KEY,
                      TenMenu     NVARCHAR(50)  NOT NULL UNIQUE,
                      MoTa        NVARCHAR(255),
                      TrangThai   NVARCHAR(50)  DEFAULT N'ACTIVE'
                    CHECK (TrangThai IN (N'ACTIVE', N'INACTIVE')),
                      NgayTao     DATETIME      DEFAULT GETDATE(),
                      NgayCapNhat DATETIME      DEFAULT GETDATE()
);

-- 2. BAN AN
CREATE TABLE Ban (
                     BanID       INT           IDENTITY(1,1) PRIMARY KEY,
                     TenBan      NVARCHAR(50)  NOT NULL UNIQUE,
                     ViTri       NVARCHAR(100),
                     SoChoNgoi   INT           NOT NULL CHECK (SoChoNgoi >= 1),
                     TrangThai   NVARCHAR(30)  DEFAULT N'Trong',
                     LoaiBan     NVARCHAR(50)  DEFAULT N'Ban thuong',
                     GhiChu      NVARCHAR(100),
                     NgayTao     DATETIME      DEFAULT GETDATE(),
                     NgayCapNhat DATETIME      DEFAULT GETDATE()
);

-- 3. MON AN
-- SINGLE_TABLE inheritance: PhanLoai = DO_AN | DO_UONG
CREATE TABLE MonAn (
                       MonID       INT            IDENTITY(1,1) PRIMARY KEY,
                       TenMon      NVARCHAR(100)  NOT NULL,
                       Gia         DECIMAL(10,2)  NOT NULL CHECK (Gia >= 0),
                       MoTa        NVARCHAR(500),
                       HinhAnh     NVARCHAR(255),
                       TrangThai   NVARCHAR(20)   NOT NULL DEFAULT N'CON_HANG'
                    CHECK (TrangThai IN (N'CON_HANG', N'HET_HANG', N'NGUNG_BAN')),
                       PhanLoai    NVARCHAR(20)   NOT NULL
                    CHECK (PhanLoai IN (N'DO_AN', N'DO_UONG')),
                       Loai        NVARCHAR(50),
                       Size        NVARCHAR(5)    NULL
                    CHECK (Size IN (N'S', N'M', N'L') OR Size IS NULL),
                       DonViTinh   NVARCHAR(20)   DEFAULT N'Phan',
                       MenuID      INT            NOT NULL,
                       NgayTao     DATETIME       DEFAULT GETDATE(),
                       NgayCapNhat DATETIME       DEFAULT GETDATE(),
                       FOREIGN KEY (MenuID) REFERENCES Menu(MenuID)
);

-- 4. KHACH HANG
-- Java: @AttributeOverride hoTen -> TenKhach, LoaiKhachHang -> LoaiKhachHang
CREATE TABLE KhachHang (
                           KhachHangID   INT           IDENTITY(1,1) PRIMARY KEY,
                           TenKhach      NVARCHAR(100) NOT NULL,
                           SDT           VARCHAR(15)   UNIQUE,
                           DiaChi        NVARCHAR(255),
                           Email         NVARCHAR(100),
                           DiemTichLuy   INT           DEFAULT 0 CHECK (DiemTichLuy >= 0),
                           LoaiKhachHang NVARCHAR(20)  DEFAULT N'DONG'
                      CHECK (LoaiKhachHang IN (N'DONG', N'BAC', N'VANG', N'KIM_CUONG')),
                           NgayTao       DATETIME      DEFAULT GETDATE(),
                           NgayCapNhat   DATETIME      DEFAULT GETDATE()
);

-- 5. NHAN VIEN (co column Role)
CREATE TABLE NhanVien (
                          NhanVienID  INT            IDENTITY(1,1) PRIMARY KEY,
                          TenNhanVien NVARCHAR(100)  NOT NULL,
                          SDT         VARCHAR(15)    UNIQUE,
                          DiaChi      NVARCHAR(255),
                          Email       NVARCHAR(100)  UNIQUE NOT NULL,
                          ChucVu      NVARCHAR(50)   NOT NULL,
                          Luong       DECIMAL(10,2)  CHECK (Luong >= 0),
                          TrangThai   BIT            DEFAULT 1,
                          Password    NVARCHAR(255)  NOT NULL,
                          Role        NVARCHAR(20)   DEFAULT 'NHAN_VIEN',
                          NgayTao     DATETIME       DEFAULT GETDATE(),
                          NgayCapNhat DATETIME       DEFAULT GETDATE()
);

-- 6. CA LAM VIEC
CREATE TABLE CaLamViec (
                           CaID       INT          IDENTITY(1,1) PRIMARY KEY,
                           TenCa      NVARCHAR(50) NOT NULL,
                           GioBatDau  TIME         NOT NULL,
                           GioKetThuc TIME         NOT NULL,
                           NgayTao    DATETIME     DEFAULT GETDATE()
);

-- 7. DON HANG
-- TrangThai khop enum TrangThaiDonHang.java
-- DEFAULT = CHO_XAC_NHAN khop Java entity default
CREATE TABLE DonHang (
                         DonHangID   INT            IDENTITY(1,1) PRIMARY KEY,
                         NgayDat     DATETIME       DEFAULT GETDATE(),
                         TrangThai   NVARCHAR(50)   NOT NULL DEFAULT N'CHO_XAC_NHAN'
                    CHECK (TrangThai IN (
                        N'CHO_XAC_NHAN',
                        N'DANG_NAU',
                        N'DA_PHUC_VU',
                        N'HOAN_THANH',
                        N'DA_HUY'
                    )),
                         TongTien    DECIMAL(10,2)  DEFAULT 0 CHECK (TongTien >= 0),
                         GhiChu      NVARCHAR(255),
                         KhachHangID INT            NULL,
                         NhanVienID  INT            NULL,
                         BanID       INT            NULL,
                         NgayCapNhat DATETIME       DEFAULT GETDATE(),
                         FOREIGN KEY (KhachHangID) REFERENCES KhachHang(KhachHangID),
                         FOREIGN KEY (NhanVienID)  REFERENCES NhanVien(NhanVienID),
                         FOREIGN KEY (BanID)       REFERENCES Ban(BanID)
);

-- 8. CHI TIET DON HANG
-- Java: DonGia -> giaBan, ThanhTien -> tongTien (computed, insertable=false)
CREATE TABLE ChiTietDonHang (
                                CTDH_ID   INT            IDENTITY(1,1) PRIMARY KEY,
                                DonHangID INT            NOT NULL,
                                MonID     INT            NOT NULL,
                                SoLuong   INT            NOT NULL CHECK (SoLuong > 0),
                                DonGia    DECIMAL(10,2)  NOT NULL CHECK (DonGia >= 0),
                                ThanhTien AS (SoLuong * DonGia) PERSISTED,
                                GhiChu    NVARCHAR(100),
                                FOREIGN KEY (DonHangID) REFERENCES DonHang(DonHangID),
                                FOREIGN KEY (MonID)     REFERENCES MonAn(MonID)
);

-- 9. KHUYEN MAI
-- FIX: NgayBatDau/NgayKetThuc phai la DATETIME (khop LocalDateTime trong Java)
-- FIX: LoaiKhuyenMai chi co PHAN_TRAM | GIAM_TIEN_MAT (xoa SO_TIEN)
CREATE TABLE KhuyenMai (
                           KhuyenMaiID   INT            IDENTITY(1,1) PRIMARY KEY,
                           TenKhuyenMai  NVARCHAR(100)  NOT NULL,
                           MaKhuyenMai   VARCHAR(50)    NULL UNIQUE,
                           LoaiKhuyenMai NVARCHAR(20)   NOT NULL
                      CHECK (LoaiKhuyenMai IN (N'PHAN_TRAM', N'GIAM_TIEN_MAT')),
                           GiaTri        DECIMAL(10,2)  NOT NULL CHECK (GiaTri > 0),
                           DiemToiThieu  INT            DEFAULT 0 CHECK (DiemToiThieu >= 0),
                           TongTienToiThieu DECIMAL(10,2) DEFAULT 0 CHECK (TongTienToiThieu >= 0),
                           LoaiKhachHangToiThieu NVARCHAR(20) NULL
                                CHECK (LoaiKhachHangToiThieu IN (N'DONG', N'BAC', N'VANG', N'KIM_CUONG')),
                           NgayBatDau    DATETIME       NOT NULL,
                           NgayKetThuc   DATETIME       NOT NULL,
                           TrangThai     BIT            DEFAULT 1,
                           MoTa          NVARCHAR(255),
                           CHECK (NgayKetThuc >= NgayBatDau)
);

-- 10. HOA DON
CREATE TABLE HoaDon (
                        HoaDonID    INT            IDENTITY(1,1) PRIMARY KEY,
                        DonHangID   INT            UNIQUE NOT NULL,
                        NgayLap     DATETIME       DEFAULT GETDATE(),
                        TongTien    DECIMAL(10,2)  NOT NULL CHECK (TongTien >= 0),
                        GiamGia     DECIMAL(10,2)  DEFAULT 0 CHECK (GiamGia >= 0),
                        ThanhTien   AS (TongTien - GiamGia) PERSISTED,
                        KhuyenMaiID INT            NULL,
                        FOREIGN KEY (DonHangID)   REFERENCES DonHang(DonHangID),
                        FOREIGN KEY (KhuyenMaiID) REFERENCES KhuyenMai(KhuyenMaiID)
);

-- 11. THANH TOAN
-- FIX: TrangThai DEFAULT = CHO_XU_LY khop Java entity default
-- FIX: CHECK khop enum TrangThaiThanhToan.java
-- FIX: PhuongThuc khop enum PhuongThucThanhToan.java
CREATE TABLE ThanhToan (
                           ThanhToanID INT            IDENTITY(1,1) PRIMARY KEY,
                           HoaDonID    INT            NOT NULL,
                           PhuongThuc  NVARCHAR(50)   NOT NULL,
                           SoTien      DECIMAL(10,2)  NOT NULL CHECK (SoTien > 0),
                           ThoiGian    DATETIME       DEFAULT GETDATE(),
                           TrangThai   NVARCHAR(30)   DEFAULT N'CHO_XU_LY'
                    CHECK (TrangThai IN (N'THANH_CONG', N'THAT_BAI', N'CHO_XU_LY')),
                           FOREIGN KEY (HoaDonID) REFERENCES HoaDon(HoaDonID)
);

-- 12. KET CA
CREATE TABLE KetCa (
                       KetCaID         INT            IDENTITY(1,1) PRIMARY KEY,
                       CaID            INT            NOT NULL,
                       NhanVienID      INT            NOT NULL,
                       tgBatDau        DATETIME       NOT NULL,
                       tgKetThuc       DATETIME       NULL,
                       TongDoanhThu    DECIMAL(10,2)  DEFAULT 0,
                       TienMat         DECIMAL(10,2)  DEFAULT 0,
                       TienChuyenKhoan DECIMAL(10,2)  DEFAULT 0,
                       GhiChu          NVARCHAR(255),
                       FOREIGN KEY (CaID)       REFERENCES CaLamViec(CaID),
                       FOREIGN KEY (NhanVienID) REFERENCES NhanVien(NhanVienID)
);
GO

-- ============================================================
--  INSERT DU LIEU
-- ============================================================

-- NHAN VIEN (co Role)
INSERT INTO NhanVien (TenNhanVien, SDT, DiaChi, Email, ChucVu, Luong, TrangThai, Password, Role)
VALUES
    (N'Nguyen Ngoc Nga',      '0901111111', N'HCM', 'nga@gmail.com',         N'QUAN_LY',      15000000, 1, '12345',  'NHAN_VIEN'),
    (N'Nguyen Thi Huong',     '0909999999', N'HCM', 'chuquan@example.com',   N'QUAN_LY',      25000000, 1, '123456', 'NHAN_VIEN'),
    (N'Pham Van Chef',        '0907777777', N'HCM', 'chef1@gmail.com',        N'DAU_BEP',      18000000, 1, '12345',  'NHAN_VIEN'),
    (N'Le Thi Kim Chi',       '0908888888', N'HCM', 'chef2@gmail.com',        N'DAU_BEP',      17000000, 1, '12345',  'NHAN_VIEN'),
    (N'Tran Van Phu',         '0911112222', N'HCM', 'phubep1@gmail.com',      N'PHU_BEP',      11000000, 1, '12345',  'NHAN_VIEN'),
    (N'Hoang Minh Tam',       '0911113333', N'HCM', 'phubep2@gmail.com',      N'PHU_BEP',      10500000, 1, '12345',  'NHAN_VIEN'),
    (N'Nguyen Thi Phuc Vu',   '0912223344', N'HCM', 'phucvu1@gmail.com',      N'PHUC_VU',       8500000, 1, '12345',  'NHAN_VIEN'),
    (N'Le Van An',            '0912224455', N'HCM', 'phucvu2@gmail.com',      N'PHUC_VU',       8200000, 1, '12345',  'NHAN_VIEN'),
    (N'Tran Thi Mai',         '0912225566', N'HCM', 'phucvu3@gmail.com',      N'PHUC_VU',       8000000, 1, '12345',  'NHAN_VIEN'),
    (N'Tran Thao Nuong',      '0902222222', N'HCM', 'nuong@gmail.com',        N'KY_THUAT_VIEN', 12000000, 1, '12345',  'NHAN_VIEN'),
    (N'Pham Ngoc Tu',         '0903333333', N'HCM', 'ngoctus@gmail.com',      N'KY_THUAT_VIEN', 12000000, 1, '12345',  'NHAN_VIEN'),
    (N'Tran Minh Tu',         '0904444444', N'HCM', 'minhtu94@gmail.com',     N'NGUOI_DUNG',     9000000, 1, '12345',  'NHAN_VIEN'),
    (N'Nguyen Van Admin',     '0905555555', N'HCM', 'admin@example.com',      N'QUAN_LY',      10000000, 1, '123456', 'ADMIN');
GO

-- KHACH HANG
INSERT INTO KhachHang (TenKhach, SDT, Email, DiemTichLuy, LoaiKhachHang)
VALUES
    (N'Nguyễn Minh Tú',  '0901234567', 'tu@gmail.com',   150,  N'BAC'),
    (N'Lê Thị Hoa',      '0912345678', 'hoa@gmail.com',  520,  N'VANG'),
    (N'Trần Văn Hưng',   '0923456789', 'hung@gmail.com',   0,  N'DONG'),
    (N'Phạm Ngọc Lan',   '0934567890', 'lan@gmail.com',  1200, N'KIM_CUONG');
GO

-- BAN AN
INSERT INTO Ban (TenBan, ViTri, SoChoNgoi, TrangThai, LoaiBan, GhiChu)
VALUES
    (N'Bàn 1',    N'Tầng 1 - Gần cửa', 4, N'Đang dùng', N'Bàn thường', N''),
    (N'Bàn 2',    N'Tầng 1 - Gần cửa', 4, N'Trống', N'Bàn thường', N''),
    (N'Bàn 3',    N'Tầng 1 - Giữa',    6, N'Trống', N'Bàn thường', N''),
    (N'Bàn 4',    N'Tầng 1 - Góc',     2, N'Đặt trước', N'Bàn đôi',    N''),
    (N'Bàn 5',    N'Tầng 2',           8, N'Trống', N'Bàn lớn',    N'Phù hợp nhóm'),
    (N'Bàn 6',    N'Tầng 2',           4, N'Trống', N'Bàn thường', N''),
    (N'Bàn VIP 1',N'Tầng 2',           6, N'Trống', N'Bàn VIP',    N'Không gian riêng');
GO

-- CA LAM VIEC
INSERT INTO CaLamViec (TenCa, GioBatDau, GioKetThuc)
VALUES
    (N'Ca sáng',  '06:00', '14:00'),
    (N'Ca chiều', '14:00', '22:00'),
    (N'Ca tối',   '22:00', '06:00');
GO

-- KHUYEN MAI
INSERT INTO KhuyenMai (TenKhuyenMai, MaKhuyenMai, LoaiKhuyenMai, GiaTri, DiemToiThieu, TongTienToiThieu, LoaiKhachHangToiThieu, NgayBatDau, NgayKetThuc, TrangThai, MoTa)
VALUES
    (N'Giam 10% cuoi tuan',        'WEEKEND10', N'PHAN_TRAM',     10,    0,   0,      NULL,     '2025-01-01 00:00:00', '2026-12-31 23:59:59', 1, N'Giam 10% cho don hang cuoi tuan'),
    (N'Tang 50k don tren 300k',    'BILL300K',  N'GIAM_TIEN_MAT', 50000, 0,   300000, NULL,     '2025-05-01 00:00:00', '2026-12-31 23:59:59', 1, N'Tang 50k cho don tu 300k'),
    (N'Uu dai thanh vien Vang',    'VANG15',    N'PHAN_TRAM',     15,    500, 0,      N'VANG',  '2025-01-01 00:00:00', '2026-12-31 23:59:59', 1, N'Giam 15% cho khach tu 500 diem'),
    (N'Giam 20% cho thanh vien',   'MEMBER20',  N'PHAN_TRAM',     20,    200, 185000, NULL,     '2025-01-01 00:00:00', '2026-12-31 23:59:59', 1, N'Giam 20% cho khach tu 200 diem, don toi thieu 185k'),
    (N'Tang 30k khach than thiet', 'LOYAL30K',  N'GIAM_TIEN_MAT', 30000, 350, 250000, NULL,     '2025-01-01 00:00:00', '2026-12-31 23:59:59', 1, N'Giam 30k cho khach tu 350 diem, don toi thieu 250k');
GO

-- MENU
INSERT INTO Menu (TenMenu, MoTa, TrangThai)
VALUES
    (N'Đồ ăn',   N'Menu các món ăn',         N'ACTIVE'),
    (N'Đồ uống', N'Menu các món đồ uống',    N'ACTIVE');
GO

-- MÓN ĂN & ĐỒ UỐNG
DECLARE @doAn   INT = (SELECT MenuID FROM Menu WHERE TenMenu = N'Đồ ăn');
DECLARE @doUong INT = (SELECT MenuID FROM Menu WHERE TenMenu = N'Đồ uống');

INSERT INTO MonAn (PhanLoai, TenMon, Gia, MoTa, HinhAnh, TrangThai, Loai, DonViTinh, MenuID, Size)
VALUES
(N'DO_AN', N'Com phan',      45000,  N'Com trang an kem topping tuy chon.',    N'/img/comphan.jpg',       N'CON_HANG', N'Com',   N'Phan', @doAn, NULL),
(N'DO_AN', N'omurice',       35000,  N'Com chien boc trung mem, an kem sot ca chua.',        N'/img/comtrung.jpg',      N'CON_HANG', N'Com',   N'Phan', @doAn, NULL),
(N'DO_AN', N'Com ca ri',     50000,  N'Com ca ri ga',           N'/img/com-cari.jpg',      N'CON_HANG', N'Com',   N'Phan', @doAn, NULL),
(N'DO_AN', N'donburi',       55000,  N'Com trang an kem thit bo, ga hoac hai san.',         N'/img/com-suon.jpg',      N'CON_HANG', N'Com',   N'Phan', @doAn, NULL),
(N'DO_AN', N'Com hop',       40000,  N'Com hop van phong',      N'/img/comhop.jpg',        N'CON_HANG', N'Com',   N'Phan', @doAn, NULL),
(N'DO_AN', N'Ramen',         65000,  N'Mi nuoc voi nuoc dung dam da, thit heo mem.',      N'/img/ramen.jpg',         N'CON_HANG', N'Mi',    N'To',   @doAn, NULL),
(N'DO_AN', N'Udon',          60000,  N'Mi soi day, mem, an voi nuoc dung thanh nhe.',                N'/img/udon.jpg',          N'CON_HANG', N'Mi',    N'To',   @doAn, NULL),
(N'DO_AN', N'Yakisoba',      55000,  N'Mi xao voi thit, rau va sot ngot man.',         N'/img/mi-xao.jpg',        N'CON_HANG', N'Mi',    N'To',   @doAn, NULL),
(N'DO_AN', N'soba',          50000,  N'Mi lam tu kieu mach, co the an nong hoac lanh.',             N'/img/mi-nuoc.jpg',       N'CON_HANG', N'Mi',    N'To',   @doAn, NULL),
(N'DO_AN', N'Sushi',         85000,  N'Com tron giam ket hop hai san tuoi.',      N'/img/sushi.jpg',         N'CON_HANG', N'Nhat',  N'Set',  @doAn, NULL),
(N'DO_AN', N'Sashimi',       95000,  N'Hai san tuoi song cat lat mong, an kem wasabi.',         N'/img/sashimi.jpg',       N'CON_HANG', N'Nhat',  N'Set',  @doAn, NULL),
(N'DO_AN', N'Tempura',       70000,  N'Hai san va rau cu tam bot chien gion nhe.',     N'/img/tempura.jpg',       N'CON_HANG', N'Nhat',  N'Phan', @doAn, NULL),
(N'DO_AN', N'Takoyaki',      55000,  N'Banh tron nhan bach tuoc, phu sot mayo.',     N'/img/takoyaki.jpg',      N'CON_HANG', N'Nhat',  N'Dia',  @doAn, NULL),
(N'DO_AN', N'sukiyaki',      150000, N'Lau ngot voi thit bo, trung song va rau.',           N'/img/laubo.jpg',         N'CON_HANG', N'Lau',   N'Noi',  @doAn, NULL),
(N'DO_AN', N'shabu-shabu',   120000, N'Lau nhung thit bo, rau, an kem nuoc cham me.',           N'/img/lauheo.jpg',        N'CON_HANG', N'Lau',   N'Noi',  @doAn, NULL),
(N'DO_AN', N'Okonomiyaki',   45000,  N'Banh xeo Nhat voi bap cai, thit, hai san.',      N'/img/banhxeo.jpg',       N'CON_HANG', N'Viet',  N'Cai',  @doAn, NULL),
(N'DO_AN', N'yakitori',      75000,  N'Ga xien nuong than, phu sot ngot man.',         N'/img/thit-nuong.jpg',    N'CON_HANG', N'Nuong', N'Phan', @doAn, NULL),
(N'DO_AN', N'Tonkatsu',      65000,  N'Thit heo chien xu',      N'/img/thit-chien-xu.jpg', N'CON_HANG', N'Chien', N'Phan', @doAn, NULL),
(N'DO_AN', N'Karaage',       60000,  N'Thit ga chien gion',     N'/img/thit-chien.jpg',    N'CON_HANG', N'Chien', N'Phan', @doAn, NULL),
(N'DO_AN', N'Sup miso',      25000,  N'Canh dau nanh voi rong bien, dau hu mem.',       N'/img/miso.jpg',          N'CON_HANG', N'Sup',   N'Bat',  @doAn, NULL),
(N'DO_AN', N'Chawamushi',    20000,  N'Trung hap mem nau voi tom hoac thit',           N'/img/canh.jpg',          N'CON_HANG', N'Viet',  N'Bat',  @doAn, NULL),
(N'DO_AN', N'Oden',          55000,  N'Cac nguyen lieu nhu trung, cu cai, cha ca nau trong nuoc dung dashi.',          N'/img/oden.jpg',          N'CON_HANG', N'Nhat',  N'Phan', @doAn, NULL),
(N'DO_AN', N'Onigiri',       40000,  N'Com nam hinh tam giac, ben trong co ca, rong bien hoac trung.',        N'/img/onigiri.jfif',      N'CON_HANG', N'Com',   N'Cai',  @doAn, NULL),
(N'DO_AN', N'Bingsu',        125000, N'Da bao min kieu Han Quoc, phu trai cay, dau do, siro hoac kem.',    N'/img/bingsu.jpg',        N'CON_HANG', N'Trang miem', N'Phan', @doAn, NULL),
(N'DO_AN', N'Dango',         50000,  N'Banh tron xien que lam tu bot gao, an kem sot ngot mitarashi.',       N'/img/dango.jpg',         N'CON_HANG', N'Trang miem', N'Cai',  @doAn, NULL),
(N'DO_AN', N'Dorayaki',      50000,  N'Banh kep nhan dau do ngot, mem xop.',       N'/img/dorayaki.jpg',      N'CON_HANG', N'Trang miem', N'Cai',  @doAn, NULL),
(N'DO_AN', N'Mochi',         50000,  N'Banh deo nhan dau do hoac kem lanh, vo min mem.',  N'/img/mochi.jpg',        N'CON_HANG', N'Trang miem', N'Cai',  @doAn, NULL),

(N'DO_UONG', N'Tra xanh Nhat',  45000,  N'Tra xanh truyen thong Nhat Ban, vi thanh nhe.',   N'/img/tra-xanh-nhat.jpg',   N'CON_HANG', N'Tra',    N'Ly', @doUong, NULL),
(N'DO_UONG', N'Matcha Latte',   55000,  N'Bot matcha nguyen chat ket hop sua tuoi.',   N'/img/macha-latte.jpg', N'CON_HANG', N'Tra',  N'Ly', @doUong, NULL),
(N'DO_UONG', N'Tra Atiso',      40000,  N'Tra thao moc thanh mat tu hoa atiso.',               N'/img/tra-atttiso.jpg',  N'CON_HANG', N'Tra',    N'Ly', @doUong, NULL),
(N'DO_UONG', N'Ramune',         35000,  N'Nuoc soda Nhat noi tieng voi chai bi dac trung.',       N'/img/ramune.jpg',    N'CON_HANG', N'Nuoc',   N'Ly', @doUong, NULL),
(N'DO_UONG', N'Sake',           95000,  N'Ruou gao truyen thong Nhat Ban, huong thom diu.',     N'/img/ruou-sake.jpg',      N'CON_HANG', N'Ruou',   N'Ly', @doUong, NULL),
(N'DO_UONG', N'Bia Nhat',       35000,  N'Bia lager Nhat nhe, tuoi mat, hau vi sach.',      N'/img/bia-nhat.jpg',   N'CON_HANG', N'Bia',    N'Ly', @doUong, NULL),
(N'DO_UONG', N'Tra Dao',        40000,  N'Tra den ket hop dao ngot tu nhien.',               N'/img/tra-dao.jpg',      N'CON_HANG', N'Tra',    N'Ly', @doUong, NULL),
(N'DO_UONG', N'Cola',           20000,  N'Nuoc ngot co ga vi cola quen thuoc.',     N'/img/coca.jpg',      N'CON_HANG', N'Nuoc',   N'Lon', @doUong, NULL),
(N'DO_UONG', N'Pepsi',          20000,  N'Nuoc ngot co ga vi cola dam da.',     N'/img/pepsi.jpg',     N'CON_HANG', N'Nuoc',   N'Lon', @doUong, NULL),
(N'DO_UONG', N'Nuoc suoi',      15000,  N'Nuoc tinh khiet dong chai.',                   N'/img/nuoc-suoi.jpg',  N'CON_HANG', N'Nuoc',   N'Chai', @doUong, NULL),
(N'DO_UONG', N'Tra Trai Cay',   45000,  N'Tra ket hop nhieu loai trai cay nhiet doi.',      N'/img/tra-trai-cay-nhiet-doi.jpg', N'CON_HANG', N'Tra',   N'Ly', @doUong, NULL),
(N'DO_UONG', N'Tra Sua TT',     45000,  N'Tra den pha cung sua va duong, huong vi beo nhe.',               N'/img/trasua.jpg',  N'CON_HANG', N'Tra',    N'Ly', @doUong, NULL)
GO

-- ============================================================
--  PHAN B: PATCH DB DA TON TAI (comment lai neu dung Phan A)
-- ============================================================
-- Neu DB da co san, chi can chay phan nay thay vi toan bo file
-- Xoa comment (--) o 3 dong duoi de kich hoat:

-- USE QUANLI_APPODER_DOAN;
-- GO
-- IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'NhanVien' AND COLUMN_NAME = 'Role')
-- BEGIN
--     ALTER TABLE NhanVien ADD Role NVARCHAR(20) DEFAULT 'NHAN_VIEN';
--     UPDATE NhanVien SET Role = 'ADMIN'      WHERE Email = 'admin@example.com';
--     UPDATE NhanVien SET Role = 'NHAN_VIEN'  WHERE Role IS NULL;
--     PRINT 'Da them column Role thanh cong.';
-- END
-- ELSE
--     PRINT 'Column Role da ton tai, bo qua.';
-- GO

-- ============================================================
--  KIEM TRA
-- ============================================================
SELECT N'NhanVien'  AS Bang, COUNT(*) AS SoBanGhi FROM NhanVien  UNION ALL
SELECT N'KhachHang',         COUNT(*) FROM KhachHang UNION ALL
SELECT N'Ban',               COUNT(*) FROM Ban        UNION ALL
SELECT N'Menu',              COUNT(*) FROM Menu       UNION ALL
SELECT N'MonAn',             COUNT(*) FROM MonAn      UNION ALL
SELECT N'KhuyenMai',         COUNT(*) FROM KhuyenMai;

SELECT KhuyenMaiID, TenKhuyenMai, LoaiKhuyenMai, GiaTri, NgayKetThuc FROM KhuyenMai;

PRINT N'Done! Chay app xong goi POST /api/migrate/hash-passwords de hash password BCrypt.';
GO
