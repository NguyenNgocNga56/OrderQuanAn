﻿USE master;
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

--  TẠO BẢNG

-- 1. MENU
CREATE TABLE Menu (
                      MenuID       INT           IDENTITY(1,1) PRIMARY KEY,
                      TenMenu      NVARCHAR(50)  NOT NULL UNIQUE,
                      MoTa         NVARCHAR(255),
                      TrangThai    NVARCHAR(50)  DEFAULT N'Hoạt động',
                      NgayTao      DATETIME      DEFAULT GETDATE(),
                      NgayCapNhat  DATETIME      DEFAULT GETDATE()
);

-- 2. BÀN ĂN
CREATE TABLE Ban (
                     BanID       INT           IDENTITY(1,1) PRIMARY KEY,
                     TenBan      NVARCHAR(50)  NOT NULL UNIQUE,
                     ViTri       NVARCHAR(100),
                     SoChoNgoi   INT           NOT NULL CHECK (SoChoNgoi >= 1),
                     TrangThai   NVARCHAR(30)  DEFAULT N'Trống'
                         CHECK (TrangThai IN (N'Trống', N'Đang dùng', N'Đặt trước', N'Bảo trì')),
                     LoaiBan     NVARCHAR(50)  DEFAULT N'Bàn thường',
                     GhiChu      NVARCHAR(100),
                     NgayTao     DATETIME      DEFAULT GETDATE(),
                     NgayCapNhat DATETIME      DEFAULT GETDATE()
);

-- 3. MÓN ĂN
CREATE TABLE MonAn (
                       MonID       INT            IDENTITY(1,1) PRIMARY KEY,
                       TenMon      NVARCHAR(100)  NOT NULL,
                       Gia         DECIMAL(10,2)  NOT NULL CHECK (Gia >= 0),
                       MoTa        NVARCHAR(500),
                       HinhAnh     NVARCHAR(255),
                       TrangThai   NVARCHAR(50)   DEFAULT N'CON_HANG'
                           CHECK (TrangThai IN (N'CON_HANG', N'HET_HANG', N'NGUNG_BAN')),
                       PhanLoai    NVARCHAR(20)   NOT NULL
                           CHECK (PhanLoai IN (N'DO_AN', N'DO_UONG')),
                       Loai        NVARCHAR(50),
                       Size        NVARCHAR(10)   NULL
                           CHECK (Size IN (N'S', N'M', N'L') OR Size IS NULL),
                       DonViTinh   NVARCHAR(20)   DEFAULT N'Phần',
                       MenuID      INT            NOT NULL,
                       NgayTao     DATETIME       DEFAULT GETDATE(),
                       NgayCapNhat DATETIME       DEFAULT GETDATE(),
                       FOREIGN KEY (MenuID) REFERENCES Menu(MenuID)
);

-- 4. KHÁCH HÀNG
CREATE TABLE KhachHang (
                           KhachHangID    INT           IDENTITY(1,1) PRIMARY KEY,
                           TenKhach       NVARCHAR(100) NOT NULL,
                           SDT            VARCHAR(15)   UNIQUE,
                           DiaChi         NVARCHAR(255),
                           Email          NVARCHAR(100),
                           DiemTichLuy    INT           DEFAULT 0 CHECK (DiemTichLuy >= 0),
                           LoaiKhachHang  NVARCHAR(20)  DEFAULT N'DONG'
                               CHECK (LoaiKhachHang IN (N'DONG', N'BAC', N'VANG', N'KIM_CUONG')),
                           NgayTao        DATETIME      DEFAULT GETDATE(),
                           NgayCapNhat    DATETIME      DEFAULT GETDATE()
);

-- 5. NHÂN VIÊN
CREATE TABLE NhanVien (
                          NhanVienID   INT            IDENTITY(1,1) PRIMARY KEY,
                          TenNhanVien  NVARCHAR(100)  NOT NULL,
                          SDT          VARCHAR(15)    UNIQUE,
                          DiaChi       NVARCHAR(255),
                          Email        NVARCHAR(100)  UNIQUE,
                          ChucVu       NVARCHAR(50)   NOT NULL,
                          Luong        DECIMAL(10,2)  CHECK (Luong >= 0),
                          TrangThai    BIT            DEFAULT 1,
                          Password     NVARCHAR(255)  NOT NULL,
                          NgayTao      DATETIME       DEFAULT GETDATE(),
                          NgayCapNhat  DATETIME       DEFAULT GETDATE()
);

-- 6. CA LÀM VIỆC
CREATE TABLE CaLamViec (
                           CaID        INT          IDENTITY(1,1) PRIMARY KEY,
                           TenCa       NVARCHAR(50) NOT NULL,
                           GioBatDau   TIME         NOT NULL,
                           GioKetThuc  TIME         NOT NULL,
                           NgayTao     DATETIME     DEFAULT GETDATE()
);

-- 7. ĐƠN HÀNG
CREATE TABLE DonHang (
                         DonHangID   INT            IDENTITY(1,1) PRIMARY KEY,
                         NgayDat     DATETIME       DEFAULT GETDATE(),
                         TrangThai   NVARCHAR(50)   NOT NULL DEFAULT N'Chờ xử lý',
                         TongTien    DECIMAL(10,2)  CHECK (TongTien >= 0),
                         GhiChu      NVARCHAR(255),
                         KhachHangID INT            NULL,
                         NhanVienID  INT            NULL,
                         BanID       INT            NULL,
                         NgayCapNhat DATETIME       DEFAULT GETDATE(),
                         FOREIGN KEY (KhachHangID) REFERENCES KhachHang(KhachHangID),
                         FOREIGN KEY (NhanVienID)  REFERENCES NhanVien(NhanVienID),
                         FOREIGN KEY (BanID)       REFERENCES Ban(BanID)
);

-- 8. CHI TIẾT ĐƠN HÀNG
CREATE TABLE ChiTietDonHang (
                                CTDH_ID    INT            IDENTITY(1,1) PRIMARY KEY,
                                DonHangID  INT            NOT NULL,
                                MonID      INT            NOT NULL,
                                SoLuong    INT            NOT NULL CHECK (SoLuong > 0),
                                DonGia     DECIMAL(10,2)  NOT NULL CHECK (DonGia >= 0),
                                ThanhTien  AS (SoLuong * DonGia) PERSISTED,
                                GhiChu     NVARCHAR(100),
                                FOREIGN KEY (DonHangID) REFERENCES DonHang(DonHangID),
                                FOREIGN KEY (MonID)     REFERENCES MonAn(MonID)
);

-- 9. KHUYẾN MÃI
CREATE TABLE KhuyenMai (
                           KhuyenMaiID   INT            IDENTITY(1,1) PRIMARY KEY,
                           TenKhuyenMai  NVARCHAR(100)  NOT NULL,
                           LoaiKhuyenMai NVARCHAR(50)   NOT NULL,
                           GiaTri        DECIMAL(10,2)  NOT NULL,
                           NgayBatDau    DATE           NOT NULL,
                           NgayKetThuc   DATE           NOT NULL,
                           TrangThai     BIT            DEFAULT 1,
                           MoTa          NVARCHAR(255),
                           CHECK (NgayKetThuc >= NgayBatDau)
);

-- 10. HÓA ĐƠN
CREATE TABLE HoaDon (
                        HoaDonID    INT            IDENTITY(1,1) PRIMARY KEY,
                        DonHangID   INT            UNIQUE NOT NULL,
                        NgayLap     DATETIME       DEFAULT GETDATE(),
                        TongTien    DECIMAL(10,2)  NOT NULL,
                        GiamGia     DECIMAL(10,2)  DEFAULT 0 CHECK (GiamGia >= 0),
                        ThanhTien   AS (TongTien - GiamGia) PERSISTED,
                        KhuyenMaiID INT            NULL,
                        FOREIGN KEY (DonHangID)   REFERENCES DonHang(DonHangID),
                        FOREIGN KEY (KhuyenMaiID) REFERENCES KhuyenMai(KhuyenMaiID)
);

-- 11. THANH TOÁN
CREATE TABLE ThanhToan (
                           ThanhToanID  INT            IDENTITY(1,1) PRIMARY KEY,
                           HoaDonID     INT            NOT NULL,
                           PhuongThuc   NVARCHAR(50)   NOT NULL,
                           SoTien       DECIMAL(10,2)  NOT NULL CHECK (SoTien > 0),
                           ThoiGian     DATETIME       DEFAULT GETDATE(),
                           TrangThai    NVARCHAR(50)   DEFAULT N'THANH_CONG',
                           FOREIGN KEY (HoaDonID) REFERENCES HoaDon(HoaDonID)
);

-- 12. KẾT CA
CREATE TABLE KetCa (
                       KetCaID          INT            IDENTITY(1,1) PRIMARY KEY,
                       CaID             INT            NOT NULL,
                       NhanVienID       INT            NOT NULL,
                       tgBatDau         DATETIME       NOT NULL,
                       tgKetThuc        DATETIME       NULL,
                       TongDoanhThu     DECIMAL(10,2)  DEFAULT 0,
                       TienMat          DECIMAL(10,2)  DEFAULT 0,
                       TienChuyenKhoan  DECIMAL(10,2)  DEFAULT 0,
                       GhiChu           NVARCHAR(255),
                       FOREIGN KEY (CaID)       REFERENCES CaLamViec(CaID),
                       FOREIGN KEY (NhanVienID) REFERENCES NhanVien(NhanVienID)
);
GO

--  INSERT DỮ LIỆU

-- NHÂN VIÊN

INSERT INTO NhanVien (TenNhanVien, SDT, DiaChi, Email, ChucVu, Luong, TrangThai, Password)
VALUES
    (N'Nguyễn Ngọc Ngà',    '0901111111', N'HCM', 'nga@gmail.com',         N'QUAN_LY',         15000000, 1, '12345'),
    (N'Trần Thảo Nương',    '0902222222', N'HCM', 'nuong@gmail.com',        N'KY_THUAT_VIEN',   12000000, 1, '12345'),
    (N'Phạm Ngọc Tú',       '0903333333', N'HCM', 'ngoctus@gmail.com',      N'KY_THUAT_VIEN',   12000000, 1, '12345'),
    (N'Trần Minh Tú',       '0904444444', N'HCM', 'minhtu94@gmail.com',     N'NGUOI_DUNG',       9000000, 1, '12345'),
    (N'Nguyễn Văn Admin',   '0905555555', N'HCM', 'admin@example.com',      N'Quản lý',         10000000, 1, '123456'),
    (N'Nguyễn Thị Hương',   '0909999999', N'HCM', 'chuquan@example.com',    N'Chủ quán',        25000000, 1, '123456'),
    (N'Trần Thảo Nương (PH)', '0922000000', NULL,  'admin1@example.com',    N'Quản lý',           800000, 1, '123456'),
    (N'Phạm Ngọc Tú (PH)',    '0933000000', NULL,  'admin2@example.com',    N'Quản lý',           800000, 1, '123456'),
    (N'Trần Minh Tú (PH)',    '0944000000', NULL,  'admin3@example.com',    N'Quản lý',           800000, 1, '123456');
GO

-- KHÁCH HÀNG
INSERT INTO KhachHang (TenKhach, SDT, Email, DiemTichLuy, LoaiKhachHang)
VALUES
    (N'Nguyễn Minh Tú',  '0901234567', 'tu@gmail.com',   150,  N'BAC'),
    (N'Lê Thị Hoa',      '0912345678', 'hoa@gmail.com',  520,  N'VANG'),
    (N'Trần Văn Hưng',   '0923456789', 'hung@gmail.com',   0,  N'DONG'),
    (N'Phạm Ngọc Lan',   '0934567890', 'lan@gmail.com',  1200, N'KIM_CUONG');
GO

-- BÀN ĂN
INSERT INTO Ban (TenBan, ViTri, SoChoNgoi, TrangThai, LoaiBan, GhiChu)
VALUES
    (N'Bàn 1',    N'Tầng 1 - Gần cửa', 4, N'Trống', N'Bàn thường', N''),
    (N'Bàn 2',    N'Tầng 1 - Gần cửa', 4, N'Trống', N'Bàn thường', N''),
    (N'Bàn 3',    N'Tầng 1 - Giữa',    6, N'Trống', N'Bàn thường', N''),
    (N'Bàn 4',    N'Tầng 1 - Góc',     2, N'Trống', N'Bàn đôi',    N''),
    (N'Bàn 5',    N'Tầng 2',           8, N'Trống', N'Bàn lớn',    N'Phù hợp nhóm'),
    (N'Bàn 6',    N'Tầng 2',           4, N'Trống', N'Bàn thường', N''),
    (N'Bàn VIP 1',N'Tầng 2',           6, N'Trống', N'Bàn VIP',    N'Không gian riêng');
GO

-- CA LÀM VIỆC
INSERT INTO CaLamViec (TenCa, GioBatDau, GioKetThuc)
VALUES
    (N'Ca sáng',  '06:00', '14:00'),
    (N'Ca chiều', '14:00', '22:00'),
    (N'Ca tối',   '22:00', '06:00');
GO

-- KHUYẾN MÃI
INSERT INTO KhuyenMai (TenKhuyenMai, LoaiKhuyenMai, GiaTri, NgayBatDau, NgayKetThuc, MoTa)
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

-- MÓN ĂN & ĐỒ UỐNG
DECLARE @doAn   INT = (SELECT MenuID FROM Menu WHERE TenMenu = N'Đồ ăn');
DECLARE @doUong INT = (SELECT MenuID FROM Menu WHERE TenMenu = N'Đồ uống');

INSERT INTO MonAn (PhanLoai, TenMon, Gia, MoTa, HinhAnh, TrangThai, Loai, MenuID, Size)
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


--  DỌN DẸP EMAIL TRÙNG
--  Xóa nhân viên trùng email, chỉ giữ bản ghi ID nhỏ nhất

DELETE FROM NhanVien
WHERE NhanVienID NOT IN (
    SELECT MIN(NhanVienID) FROM NhanVien GROUP BY Email
);
GO

PRINT N'Khởi tạo database QUANLI_APPODER_DOAN hoàn tất!';
