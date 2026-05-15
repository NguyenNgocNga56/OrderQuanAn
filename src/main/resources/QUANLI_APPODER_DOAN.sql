USE master;
GO

IF DB_ID('QUANLI_APPODER_DOAN') IS NOT NULL
BEGIN
    ALTER DATABASE QUANLI_APPODER_DOAN 
    SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE QUANLI_APPODER_DOAN;
END
GO

CREATE DATABASE QUANLI_APPODER_DOAN;
GO
USE QUANLI_APPODER_DOAN;
GO

-- ============================================================
-- TABLES
-- ============================================================

CREATE TABLE Menu(
    MenuID    INT IDENTITY(1,1) PRIMARY KEY,
    TenMenu   NVARCHAR(50)  NOT NULL UNIQUE,
    MoTa      NVARCHAR(255),
    TrangThai NVARCHAR(50),
    NgayTao   DATE DEFAULT GETDATE()
);

CREATE TABLE MonAn (
    MonID     INT IDENTITY(1,1) PRIMARY KEY,
    TenMon    NVARCHAR(100)  NOT NULL,
    Gia       DECIMAL(10,2)  NOT NULL CHECK (Gia >= 0),
    MoTa      NVARCHAR(255),
    HinhAnh   NVARCHAR(255),
    TrangThai NVARCHAR(50)   DEFAULT N'CON_HANG',
    PhanLoai  NVARCHAR(20)   NOT NULL,
    Loai      NVARCHAR(50),
    Size      NVARCHAR(5) NULL,
    DonViTinh NVARCHAR(20),
    MenuID    INT NOT NULL,
    FOREIGN KEY (MenuID) REFERENCES Menu(MenuID)
);

CREATE TABLE KhachHang (
    KhachHangID   INT IDENTITY(1,1) PRIMARY KEY,
    HoTen         NVARCHAR(100) NOT NULL,
    SDT           VARCHAR(15)   UNIQUE,
    DiaChi        NVARCHAR(255),
    Email         NVARCHAR(100),
    DiemTichLuy   INT           DEFAULT 0 CHECK (DiemTichLuy >= 0),
    LoaiKhachHang NVARCHAR(20)  DEFAULT N'DONG'
        CHECK (LoaiKhachHang IN (N'DONG', N'BAC', N'VANG', N'KIM_CUONG'))
);

CREATE TABLE NhanVien (
    NhanVienID  INT IDENTITY(1,1) PRIMARY KEY,
    HoTen       NVARCHAR(100) NOT NULL,
    SDT         VARCHAR(15),
    DiaChi      NVARCHAR(255),
    Email       NVARCHAR(100),
    ChucVu      NVARCHAR(50),
    Luong       DECIMAL(10,2) CHECK (Luong >= 0),
    TrangThai   BIT           DEFAULT 1
);

CREATE TABLE DonHang (
    DonHangID   INT IDENTITY(1,1) PRIMARY KEY,
    NgayDat     DATETIME      DEFAULT GETDATE(),
    TrangThai   NVARCHAR(50),
    TongTien    DECIMAL(10,2) CHECK (TongTien >= 0),
    KhachHangID INT NULL,
    NhanVienID  INT NULL,
    FOREIGN KEY (KhachHangID) REFERENCES KhachHang(KhachHangID),
    FOREIGN KEY (NhanVienID)  REFERENCES NhanVien(NhanVienID)
);

CREATE TABLE ChiTietDonHang (
    CTDH_ID   INT IDENTITY(1,1) PRIMARY KEY,
    DonHangID INT NOT NULL,
    MonID     INT NOT NULL,
    SoLuong   INT           CHECK (SoLuong > 0),
    DonGia    DECIMAL(10,2) CHECK (DonGia >= 0),
    ThanhTien AS (SoLuong * DonGia) PERSISTED,
    FOREIGN KEY (DonHangID) REFERENCES DonHang(DonHangID),
    FOREIGN KEY (MonID)     REFERENCES MonAn(MonID)
);

CREATE TABLE KhuyenMai (
    KhuyenMaiID   INT IDENTITY(1,1) PRIMARY KEY,
    TenKhuyenMai  NVARCHAR(100),
    LoaiKhuyenMai NVARCHAR(50),
    GiaTri        DECIMAL(10,2),
    NgayBatDau    DATE,
    NgayKetThuc   DATE,
    CHECK (NgayKetThuc >= NgayBatDau)
);

CREATE TABLE HoaDon (
    HoaDonID    INT IDENTITY(1,1) PRIMARY KEY,
    DonHangID   INT UNIQUE NOT NULL,
    NgayLap     DATETIME      DEFAULT GETDATE(),
    TongTien    DECIMAL(10,2),
    GiamGia     DECIMAL(10,2) DEFAULT 0,
    ThanhTien   AS (TongTien - GiamGia) PERSISTED,
    KhuyenMaiID INT,
    FOREIGN KEY (DonHangID)   REFERENCES DonHang(DonHangID),
    FOREIGN KEY (KhuyenMaiID) REFERENCES KhuyenMai(KhuyenMaiID)
);

CREATE TABLE ThanhToan (
    ThanhToanID INT IDENTITY(1,1) PRIMARY KEY,
    HoaDonID    INT NOT NULL,
    PhuongThuc  NVARCHAR(50),
    SoTien      DECIMAL(10,2) CHECK (SoTien > 0),
    ThoiGian    DATETIME      DEFAULT GETDATE(),
    TrangThai   NVARCHAR(50),
    FOREIGN KEY (HoaDonID) REFERENCES HoaDon(HoaDonID)
);

CREATE TABLE CaLamViec (
    CaID       INT IDENTITY(1,1) PRIMARY KEY,
    TenCa      NVARCHAR(50),
    GioBatDau  TIME,
    GioKetThuc TIME
);

CREATE TABLE KetCa (
    KetCaID      INT IDENTITY(1,1) PRIMARY KEY,
    CaID         INT NOT NULL,
    NhanVienID   INT NOT NULL,
    tgBatDau     DATETIME,
    tgKetThuc    DATETIME,
    TongDoanhThu DECIMAL(10,2),
    FOREIGN KEY (CaID)       REFERENCES CaLamViec(CaID),
    FOREIGN KEY (NhanVienID) REFERENCES NhanVien(NhanVienID)
);

-- NHAN VIEN
INSERT INTO NhanVien (HoTen, SDT, ChucVu, Luong, TrangThai) VALUES
(N'Nguyen Van An',  '0901111111', N'Quan ly',    15000000, 1),
(N'Tran Thi Binh',  '0902222222', N'Phuc vu',     8000000, 1),
(N'Le Van Cuong',   '0903333333', N'Bep truong', 12000000, 1),
(N'Tran Thi Dung',  '0904444444', N'Phuc vu',     8000000, 1),
(N'Hoang Van Em',   '0905555555', N'Thu ngan',    9000000, 1);

-- KHACH HANG
INSERT INTO KhachHang (HoTen, SDT, Email, DiemTichLuy, LoaiKhachHang) VALUES
(N'Nguyen Minh Tu', '0901234567', 'tu@gmail.com',   150, N'BAC'),
(N'Le Thi Hoa',     '0912345678', 'hoa@gmail.com',  520, N'VANG'),
(N'Tran Van Hung',  '0923456789', 'hung@gmail.com',   0, N'DONG'),
(N'Pham Ngoc Lan',  '0934567890', 'lan@gmail.com', 1200, N'KIM_CUONG');

-- CA LAM VIEC
INSERT INTO CaLamViec (TenCa, GioBatDau, GioKetThuc) VALUES
(N'Ca sang',  '06:00', '14:00'),
(N'Ca chieu', '14:00', '22:00'),
(N'Ca toi',   '22:00', '06:00');

-- KHUYEN MAI
INSERT INTO KhuyenMai (TenKhuyenMai, LoaiKhuyenMai, GiaTri, NgayBatDau, NgayKetThuc) VALUES
(N'Giam 10% cuoi tuan',     N'PHAN_TRAM', 10,    '2025-01-01', '2025-12-31'),
(N'Tang 50k don tren 300k', N'SO_TIEN',   50000, '2025-05-01', '2025-06-30'),
(N'Uu dai thanh vien Vang', N'PHAN_TRAM', 15,    '2025-01-01', '2025-12-31');

PRINT 'Khoi tao QUANLI_APPODER_DOAN thanh cong!';
PRINT '27 mon an (22 do an + 5 do uong)';
GO
-- =========================================================
-- FIX DB + INSERT MENU + INSERT MON AN (CHUNG 1 BATCH)
-- =========================================================

-- Thêm cột Size nếu chưa có
IF NOT EXISTS (SELECT * FROM sys.columns 
               WHERE Name = 'Size' AND Object_ID = Object_ID('MonAn'))

-- Drop constraint cũ nếu tồn tại
IF EXISTS (SELECT * FROM sys.check_constraints WHERE name = 'CK_MonAn_Size')
    ALTER TABLE MonAn DROP CONSTRAINT CK_MonAn_Size;

-- Tạo lại constraint (SỬA THÀNH KHÔNG DẤU)
ALTER TABLE MonAn
ADD CONSTRAINT CK_MonAn_PhanLoai CHECK (PhanLoai IN ('DO_AN', 'DO_UONG'));

ALTER TABLE MonAn
ADD CONSTRAINT CK_MonAn_Size CHECK (Size IN ('S','M','L') OR Size IS NULL);

-- ================= MENU =================
INSERT INTO Menu (TenMenu, MoTa, TrangThai)
VALUES
(N'Đồ ăn',  N'Menu đồ ăn',  N'ACTIVE'),
(N'Đồ uống',N'Menu đồ uống',N'ACTIVE');

-- ⚠️ BIẾN PHẢI NẰM CÙNG BATCH INSERT
DECLARE @doAn   INT = (SELECT MenuID FROM Menu WHERE TenMenu = N'Đồ ăn');
DECLARE @doUong INT = (SELECT MenuID FROM Menu WHERE TenMenu = N'Đồ uống');

--INSERT 22 MON AN (SỬA 'DO_AN')
INSERT INTO MonAn(PhanLoai,TenMon,Gia,MoTa,HinhAnh,TrangThai,Loai,MenuID,Size) VALUES
('DO_AN',N'Cơm phần',45000,N'Cơm trắng',N'/img/comphan.jpg',N'CON_HANG',N'Cơm',@doAn,NULL),
('DO_AN',N'Cơm trứng',35000,N'Cơm chiên trứng',N'/img/comtrung.jpg',N'CON_HANG',N'Cơm',@doAn,NULL),
('DO_AN',N'Cơm cà ri',50000,N'Cơm cà ri',N'/img/com-cari.jpg',N'CON_HANG',N'Cơm',@doAn,NULL),
('DO_AN',N'Cơm sườn',55000,N'Cơm sườn',N'/img/com-suon.jpg',N'CON_HANG',N'Cơm',@doAn,NULL),
('DO_AN',N'Cơm hộp',40000,N'Cơm hộp',N'/img/comhop.jpg',N'CON_HANG',N'Cơm',@doAn,NULL),

('DO_AN',N'Ramen',65000,N'Mì ramen',N'/img/ramen.jpg',N'CON_HANG',N'Mì',@doAn,NULL),
('DO_AN',N'Udon',60000,N'Mì udon',N'/img/udon.jpg',N'CON_HANG',N'Mì',@doAn,NULL),
('DO_AN',N'Mì xào',55000,N'Mì xào',N'/img/mi-xao.jpg',N'CON_HANG',N'Mì',@doAn,NULL),
('DO_AN',N'Mì nước',50000,N'Mì nước',N'/img/mi-nuoc.jpg',N'CON_HANG',N'Mì',@doAn,NULL),

('DO_AN',N'Sushi',85000,N'Sushi',N'/img/sushi.jpg',N'CON_HANG',N'Nhật',@doAn,NULL),
('DO_AN',N'Sashimi',95000,N'Sashimi',N'/img/sashimi.jpg',N'CON_HANG',N'Nhật',@doAn,NULL),
('DO_AN',N'Tempura',70000,N'Tempura',N'/img/tempura.jpg',N'CON_HANG',N'Nhật',@doAn,NULL),
('DO_AN',N'Takoyaki',55000,N'Takoyaki',N'/img/takoyaki.jpg',N'CON_HANG',N'Nhật',@doAn,NULL),

('DO_AN',N'Lẩu bò',150000,N'Lẩu bò',N'/img/laubo.jpg',N'CON_HANG',N'Lẩu',@doAn,NULL),
('DO_AN',N'Lẩu heo',120000,N'Lẩu heo',N'/img/lauheo.jpg',N'CON_HANG',N'Lẩu',@doAn,NULL),

('DO_AN',N'Bánh xèo',45000,N'Bánh xèo',N'/img/banhxeo.jpg',N'CON_HANG',N'Việt',@doAn,NULL),
('DO_AN',N'Thịt nướng',75000,N'Thịt nướng',N'/img/thit-nuong.jpg',N'CON_HANG',N'Nướng',@doAn,NULL),
('DO_AN',N'Thịt chiên xù',65000,N'Thịt chiên xù',N'/img/thit-chien-xu.jpg',N'CON_HANG',N'Chiên',@doAn,NULL),
('DO_AN',N'Thịt chiên',60000,N'Thịt chiên',N'/img/thit-chien.jpg',N'CON_HANG',N'Chiên',@doAn,NULL),

('DO_AN',N'Súp miso',25000,N'Súp miso',N'/img/miso.jpg',N'CON_HANG',N'Súp',@doAn,NULL),
('DO_AN',N'Canh',20000,N'Canh',N'/img/canh.jpg',N'CON_HANG',N'Súp',@doAn,NULL),
('DO_AN',N'Oden',55000,N'Oden',N'/img/oden.jpg',N'CON_HANG',N'Nhật',@doAn,NULL);

--INSERT 5 MON UONG X3 SIZE (SỬA 'DO_UONG')
INSERT INTO MonAn(PhanLoai,TenMon,Gia,MoTa,HinhAnh,TrangThai,Loai,MenuID,Size) VALUES
('DO_UONG',N'Trà sữa',35000,N'Size S',N'/img/trasua.jpg',N'CON_HANG',N'Trà sữa',@doUong,'S'),
('DO_UONG',N'Trà sữa',45000,N'Size M',N'/img/trasua.jpg',N'CON_HANG',N'Trà sữa',@doUong,'M'),
('DO_UONG',N'Trà sữa',55000,N'Size L',N'/img/trasua.jpg',N'CON_HANG',N'Trà sữa',@doUong,'L'),

('DO_UONG',N'Bingsu',55000,N'Size S',N'/img/bingsu.jpg',N'CON_HANG',N'Bingsu',@doUong,'S'),
('DO_UONG',N'Bingsu',65000,N'Size M',N'/img/bingsu.jpg',N'CON_HANG',N'Bingsu',@doUong,'M'),
('DO_UONG',N'Bingsu',75000,N'Size L',N'/img/bingsu.jpg',N'CON_HANG',N'Bingsu',@doUong,'L'),

('DO_UONG',N'Mochi',25000,N'Size S',N'/img/mochi.jpg',N'CON_HANG',N'Mochi',@doUong,'S'),
('DO_UONG',N'Mochi',30000,N'Size M',N'/img/mochi.jpg',N'CON_HANG',N'Mochi',@doUong,'M'),
('DO_UONG',N'Mochi',35000,N'Size L',N'/img/mochi.jpg',N'CON_HANG',N'Mochi',@doUong,'L'),

('DO_UONG',N'Dango',20000,N'Size S',N'/img/dango.jpg',N'CON_HANG',N'Dango',@doUong,'S'),
('DO_UONG',N'Dango',25000,N'Size M',N'/img/dango.jpg',N'CON_HANG',N'Dango',@doUong,'M'),
('DO_UONG',N'Dango',30000,N'Size L',N'/img/dango.jpg',N'CON_HANG',N'Dango',@doUong,'L'),

('DO_UONG',N'Dorayaki',20000,N'Size S',N'/img/dorayaki.jpg',N'CON_HANG',N'Dorayaki',@doUong,'S'),
('DO_UONG',N'Dorayaki',25000,N'Size M',N'/img/dorayaki.jpg',N'CON_HANG',N'Dorayaki',@doUong,'M'),
('DO_UONG',N'Dorayaki',30000,N'Size L',N'/img/dorayaki.jpg',N'CON_HANG',N'Dorayaki',@doUong,'L');