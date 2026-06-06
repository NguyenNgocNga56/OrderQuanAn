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

-- 5. NHAN VIEN
-- Java: @AttributeOverride hoTen -> TenNhanVien
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
    LoaiKhuyenMai NVARCHAR(20)   NOT NULL
                      CHECK (LoaiKhuyenMai IN (N'PHAN_TRAM', N'GIAM_TIEN_MAT')),
    GiaTri        DECIMAL(10,2)  NOT NULL CHECK (GiaTri > 0),
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

-- NHAN VIEN
-- Password plaintext, sau khi chay app goi POST /api/migrate/hash-passwords
-- de hash BCrypt, xong xoa MigrateController.java
INSERT INTO NhanVien (TenNhanVien, SDT, DiaChi, Email, ChucVu, Luong, TrangThai, Password)
VALUES
    (N'Nguyen Ngoc Nga',  '0901111111', N'HCM', 'nga@gmail.com',       N'QUAN_LY',       15000000, 1, '12345'),
    (N'Tran Thao Nuong',  '0902222222', N'HCM', 'nuong@gmail.com',     N'KY_THUAT_VIEN', 12000000, 1, '12345'),
    (N'Pham Ngoc Tu',     '0903333333', N'HCM', 'ngoctus@gmail.com',   N'KY_THUAT_VIEN', 12000000, 1, '12345'),
    (N'Tran Minh Tu',     '0904444444', N'HCM', 'minhtu94@gmail.com',  N'NGUOI_DUNG',     9000000, 1, '12345'),
    (N'Nguyen Van Admin', '0905555555', N'HCM', 'admin@example.com',   N'QUAN_LY',       10000000, 1, '123456'),
    (N'Nguyen Thi Huong', '0909999999', N'HCM', 'chuquan@example.com', N'QUAN_LY',       25000000, 1, '123456');
GO

-- KHACH HANG
INSERT INTO KhachHang (TenKhach, SDT, Email, DiemTichLuy, LoaiKhachHang)
VALUES
    (N'Nguyen Minh Tu', '0901234567', 'tu@gmail.com',    150,  N'BAC'),
    (N'Le Thi Hoa',     '0912345678', 'hoa@gmail.com',   520,  N'VANG'),
    (N'Tran Van Hung',  '0923456789', 'hung@gmail.com',    0,  N'DONG'),
    (N'Pham Ngoc Lan',  '0934567890', 'lan@gmail.com',  1200,  N'KIM_CUONG');
GO

-- BAN AN
INSERT INTO Ban (TenBan, ViTri, SoChoNgoi, TrangThai, LoaiBan, GhiChu)
VALUES
    (N'Ban 1',    N'Tang 1 - Gan cua', 4, N'Trong', N'Ban thuong', N''),
    (N'Ban 2',    N'Tang 1 - Gan cua', 4, N'Trong', N'Ban thuong', N''),
    (N'Ban 3',    N'Tang 1 - Giua',    6, N'Trong', N'Ban thuong', N''),
    (N'Ban 4',    N'Tang 1 - Goc',     2, N'Trong', N'Ban doi',    N''),
    (N'Ban 5',    N'Tang 2',           8, N'Trong', N'Ban lon',    N'Phu hop nhom'),
    (N'Ban 6',    N'Tang 2',           4, N'Trong', N'Ban thuong', N''),
    (N'Ban VIP 1',N'Tang 2',           6, N'Trong', N'Ban VIP',    N'Khong gian rieng');
GO

-- CA LAM VIEC
INSERT INTO CaLamViec (TenCa, GioBatDau, GioKetThuc)
VALUES
    (N'Ca sang',  '06:00', '14:00'),
    (N'Ca chieu', '14:00', '22:00'),
    (N'Ca toi',   '22:00', '06:00');
GO

-- KHUYEN MAI
-- FIX: LoaiKhuyenMai chi PHAN_TRAM | GIAM_TIEN_MAT
-- FIX: kieu DATETIME, NgayKetThuc = 2026 de con hieu luc khi demo
-- FIX: xoa SO_TIEN
INSERT INTO KhuyenMai (TenKhuyenMai, LoaiKhuyenMai, GiaTri, NgayBatDau, NgayKetThuc, TrangThai, MoTa)
VALUES
    (N'Giảm 10% cuối tuần',      N'PHAN_TRAM', 10,    '2025-01-01', '2025-12-31', N'Giảm 10% cho đơn hàng cuối tuần'),
    (N'Tặng 50k đơn trên 300k',  N'SO_TIEN',   50000, '2025-05-01', '2025-06-30', N'Tặng 50k cho đơn từ 300k'),
    (N'Ưu đãi thành viên Vàng',  N'PHAN_TRAM', 15,    '2025-01-01', '2025-12-31', N'Giảm 15% cho hội viên Vàng');
GO

-- MENU
INSERT INTO Menu (TenMenu, MoTa, TrangThai)
VALUES
    (N'Đồ ăn',   N'Menu các món ăn',         N'ACTIVE'),
    (N'Đồ uống', N'Menu các món đồ uống',    N'ACTIVE');
GO

-- MON AN
DECLARE @doAn   INT = (SELECT MenuID FROM Menu WHERE TenMenu = N'Do an');
DECLARE @doUong INT = (SELECT MenuID FROM Menu WHERE TenMenu = N'Do uong');
-- MÓN ĂN & ĐỒ UỐNG
DECLARE @doAn   INT = (SELECT MenuID FROM Menu WHERE TenMenu = N'Đồ ăn');
DECLARE @doUong INT = (SELECT MenuID FROM Menu WHERE TenMenu = N'Đồ uống');

INSERT INTO MonAn (PhanLoai, TenMon, Gia, MoTa, HinhAnh, TrangThai, Loai, DonViTinh, MenuID, Size)
VALUES
-- Đồ ăn
('DO_AN', N'Cơm phần',      45000,  N'Cơm trắng + topping',    N'/img/comphan.jpg',       N'CON_HANG', N'Cơm',   @doAn, NULL),
('DO_AN', N'Cơm trứng',     35000,  N'Cơm chiên trứng',        N'/img/comtrung.jpg',       N'CON_HANG', N'Cơm',   @doAn, NULL),
('DO_AN', N'Cơm cà ri',     50000,  N'Cơm cà ri gà',           N'/img/com-cari.jpg',       N'CON_HANG', N'Cơm',   @doAn, NULL),
('DO_AN', N'Cơm sườn',      55000,  N'Cơm sườn nướng',         N'/img/com-suon.jpg',       N'CON_HANG', N'Cơm',   @doAn, NULL),
('DO_AN', N'Cơm hộp',       40000,  N'Cơm hộp văn phòng',      N'/img/comhop.jpg',         N'CON_HANG', N'Cơm',   @doAn, NULL),
('DO_AN', N'Ramen',         65000,  N'Mì ramen Nhật Bản',      N'/img/ramen.jpg',          N'CON_HANG', N'Mì',    @doAn, NULL),
('DO_AN', N'Udon',          60000,  N'Mì udon',                N'/img/udon.jpg',            N'CON_HANG', N'Mì',    @doAn, NULL),
('DO_AN', N'Mì xào',        55000,  N'Mì xào hải sản',         N'/img/mi-xao.jpg',         N'CON_HANG', N'Mì',    @doAn, NULL),
('DO_AN', N'Mì nước',       50000,  N'Mì nước bò',             N'/img/mi-nuoc.jpg',        N'CON_HANG', N'Mì',    @doAn, NULL),
('DO_AN', N'Sushi',         85000,  N'Set sushi 8 miếng',      N'/img/sushi.jpg',          N'CON_HANG', N'Nhật',  @doAn, NULL),
('DO_AN', N'Sashimi',       95000,  N'Sashimi cá hồi',         N'/img/sashimi.jpg',        N'CON_HANG', N'Nhật',  @doAn, NULL),
('DO_AN', N'Tempura',       70000,  N'Tempura tôm rau củ',     N'/img/tempura.jpg',        N'CON_HANG', N'Nhật',  @doAn, NULL),
('DO_AN', N'Takoyaki',      55000,  N'Takoyaki bạch tuộc',     N'/img/takoyaki.jpg',       N'CON_HANG', N'Nhật',  @doAn, NULL),
('DO_AN', N'Lẩu bò',       150000,  N'Lẩu bò nhúng',           N'/img/laubo.jpg',          N'CON_HANG', N'Lẩu',   @doAn, NULL),
('DO_AN', N'Lẩu heo',      120000,  N'Lẩu heo thái',           N'/img/lauheo.jpg',         N'CON_HANG', N'Lẩu',   @doAn, NULL),
('DO_AN', N'Bánh xèo',      45000,  N'Bánh xèo miền Nam',      N'/img/banhxeo.jpg',        N'CON_HANG', N'Việt',  @doAn, NULL),
('DO_AN', N'Thịt nướng',    75000,  N'Thịt nướng BBQ',         N'/img/thit-nuong.jpg',     N'CON_HANG', N'Nướng', @doAn, NULL),
('DO_AN', N'Thịt chiên xù', 65000,  N'Thịt heo chiên xù',      N'/img/thit-chien-xu.jpg',  N'CON_HANG', N'Chiên', @doAn, NULL),
('DO_AN', N'Thịt chiên',    60000,  N'Thịt gà chiên giòn',     N'/img/thit-chien.jpg',     N'CON_HANG', N'Chiên', @doAn, NULL),
('DO_AN', N'Súp miso',      25000,  N'Súp miso đậu phụ',       N'/img/miso.jpg',           N'CON_HANG', N'Súp',   @doAn, NULL),
('DO_AN', N'Canh chua',     20000,  N'Canh chua cá',           N'/img/canh.jpg',            N'CON_HANG', N'Súp',   @doAn, NULL),
('DO_AN', N'Oden',          55000,  N'Oden Nhật Bản',          N'/img/oden.jpg',            N'CON_HANG', N'Nhật',  @doAn, NULL),
-- Đồ uống
('DO_UONG', N'Trà sữa',  35000, N'Trà sữa Size S',    N'/img/trasua.jpg',   N'CON_HANG', N'Trà sữa',  @doUong, 'S'),
('DO_UONG', N'Trà sữa',  45000, N'Trà sữa Size M',    N'/img/trasua.jpg',   N'CON_HANG', N'Trà sữa',  @doUong, 'M'),
('DO_UONG', N'Trà sữa',  55000, N'Trà sữa Size L',    N'/img/trasua.jpg',   N'CON_HANG', N'Trà sữa',  @doUong, 'L'),
('DO_UONG', N'Bingsu',   55000, N'Bingsu Size S',      N'/img/bingsu.jpg',   N'CON_HANG', N'Bingsu',   @doUong, 'S'),
('DO_UONG', N'Bingsu',   65000, N'Bingsu Size M',      N'/img/bingsu.jpg',   N'CON_HANG', N'Bingsu',   @doUong, 'M'),
('DO_UONG', N'Bingsu',   75000, N'Bingsu Size L',      N'/img/bingsu.jpg',   N'CON_HANG', N'Bingsu',   @doUong, 'L'),
('DO_UONG', N'Mochi',    25000, N'Mochi Size S',        N'/img/mochi.jpg',    N'CON_HANG', N'Mochi',    @doUong, 'S'),
('DO_UONG', N'Mochi',    30000, N'Mochi Size M',        N'/img/mochi.jpg',    N'CON_HANG', N'Mochi',    @doUong, 'M'),
('DO_UONG', N'Mochi',    35000, N'Mochi Size L',        N'/img/mochi.jpg',    N'CON_HANG', N'Mochi',    @doUong, 'L'),
('DO_UONG', N'Dango',    20000, N'Dango Size S',        N'/img/dango.jpg',    N'CON_HANG', N'Dango',    @doUong, 'S'),
('DO_UONG', N'Dango',    25000, N'Dango Size M',        N'/img/dango.jpg',    N'CON_HANG', N'Dango',    @doUong, 'M'),
('DO_UONG', N'Dango',    30000, N'Dango Size L',        N'/img/dango.jpg',    N'CON_HANG', N'Dango',    @doUong, 'L'),
('DO_UONG', N'Dorayaki', 20000, N'Dorayaki Size S',     N'/img/dorayaki.jpg', N'CON_HANG', N'Dorayaki', @doUong, 'S'),
('DO_UONG', N'Dorayaki', 25000, N'Dorayaki Size M',     N'/img/dorayaki.jpg', N'CON_HANG', N'Dorayaki', @doUong, 'M'),
('DO_UONG', N'Dorayaki', 30000, N'Dorayaki Size L',     N'/img/dorayaki.jpg', N'CON_HANG', N'Dorayaki', @doUong, 'L');
GO

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
--  DỌN DẸP EMAIL TRÙNG
--  Xóa nhân viên trùng email, chỉ giữ bản ghi ID nhỏ nhất

PRINT N'Done! Chay app xong goi POST /api/migrate/hash-passwords de hash password BCrypt.';
DELETE FROM NhanVien
WHERE NhanVienID NOT IN (
    SELECT MIN(NhanVienID) FROM NhanVien GROUP BY Email
);
GO

PRINT N'Done! Chay app xong goi POST /api/migrate/hash-passwords de hash password BCrypt.';
GO