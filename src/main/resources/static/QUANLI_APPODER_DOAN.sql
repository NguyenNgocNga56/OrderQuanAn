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

DROP TABLE IF EXISTS KetCa;
DROP TABLE IF EXISTS ThanhToan;
DROP TABLE IF EXISTS HoaDon;
DROP TABLE IF EXISTS ChiTietDonHang;
DROP TABLE IF EXISTS DonHang;
DROP TABLE IF EXISTS KhuyenMai;
DROP TABLE IF EXISTS CaLamViec;
DROP TABLE IF EXISTS NhanVien;
DROP TABLE IF EXISTS KhachHang;
DROP TABLE IF EXISTS MonAn;
DROP TABLE IF EXISTS Menu;

SELECT COLUMN_NAME
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'NhanVien';

-- MENU
CREATE TABLE Menu(
    MenuID INT IDENTITY(1,1) PRIMARY KEY,
    TenMenu NVARCHAR(50) NOT NULL UNIQUE,
    MoTa NVARCHAR(255),
    TrangThai NVARCHAR(50),
    NgayTao DATE DEFAULT GETDATE()
);

-- MÓN ĂN
CREATE TABLE MonAn (
    MonID INT IDENTITY(1,1) PRIMARY KEY,
    TenMon NVARCHAR(100) NOT NULL,
    Gia DECIMAL(10,2) NOT NULL CHECK (Gia >= 0),
    MoTa NVARCHAR(255),
    HinhAnh NVARCHAR(255),
    TrangThai NVARCHAR(50) DEFAULT 'CON_HANG',
    PhanLoai NVARCHAR(20) NOT NULL CHECK (PhanLoai IN (N'Đồ ăn', N'Đồ uống')),
    Loai NVARCHAR(50),                   
    DonViTinh NVARCHAR(20),               
    MenuID INT NOT NULL,

    FOREIGN KEY (MenuID) REFERENCES Menu(MenuID)
);
-- KHÁCH HÀNG
CREATE TABLE KhachHang (
    KhachHangID INT IDENTITY(1,1) PRIMARY KEY,
    TenKhach NVARCHAR(100) NOT NULL,
    SDT VARCHAR(15) UNIQUE,
    DiaChi NVARCHAR(255),
    Email NVARCHAR(100),
    DiemTichLuy INT DEFAULT 0 CHECK (DiemTichLuy >= 0),
    LoaiKhachHang NVARCHAR(20) 
        CHECK (LoaiKhachHang IN (N'Đồng', N'Bạc', N'Vàng', N'Kim cương')) 
        DEFAULT N'Đồng'
);

-- NHÂN VIÊN
CREATE TABLE NhanVien (
    NhanVienID INT IDENTITY(1,1) PRIMARY KEY,
    TenNhanVien NVARCHAR(100) NOT NULL,
    SDT VARCHAR(15),
    DiaChi NVARCHAR(255),
    Email NVARCHAR(100),
    ChucVu NVARCHAR(50),
    Luong DECIMAL(10,2) CHECK (Luong >= 0),
    TrangThai BIT DEFAULT 1 CHECK (TrangThai IN (0,1)),
    password NVARCHAR(255) NOT NULL
);

-- ĐƠN HÀNG
CREATE TABLE DonHang (
    DonHangID INT IDENTITY(1,1) PRIMARY KEY,
    NgayDat DATETIME DEFAULT GETDATE(),
    TrangThai NVARCHAR(50),
    TongTien DECIMAL(10,2) CHECK (TongTien >= 0),

    KhachHangID INT NULL,
    NhanVienID INT NULL,

    FOREIGN KEY (KhachHangID) REFERENCES KhachHang(KhachHangID),
    FOREIGN KEY (NhanVienID) REFERENCES NhanVien(NhanVienID)
);

-- CHI TIẾT ĐƠN HÀNG
CREATE TABLE ChiTietDonHang (
    CTDH_ID INT IDENTITY(1,1) PRIMARY KEY,
    DonHangID INT NOT NULL,
    MonID INT NOT NULL,

    SoLuong INT CHECK (SoLuong > 0),
    DonGia DECIMAL(10,2) CHECK (DonGia >= 0),

    ThanhTien AS (SoLuong * DonGia) PERSISTED,

    FOREIGN KEY (DonHangID) REFERENCES DonHang(DonHangID),
    FOREIGN KEY (MonID) REFERENCES MonAn(MonID)
);

-- KHUYẾN MÃI
CREATE TABLE KhuyenMai (
    KhuyenMaiID INT IDENTITY(1,1) PRIMARY KEY,
    TenKhuyenMai NVARCHAR(100),
    LoaiKhuyenMai NVARCHAR(50),
    GiaTri DECIMAL(10,2),

    NgayBatDau DATE,
    NgayKetThuc DATE,

    CHECK (NgayKetThuc >= NgayBatDau)
);

-- HÓA ĐƠN
CREATE TABLE HoaDon (
    HoaDonID INT IDENTITY(1,1) PRIMARY KEY,
    DonHangID INT UNIQUE NOT NULL,

    NgayLap DATETIME DEFAULT GETDATE(),

    TongTien DECIMAL(10,2),
    GiamGia DECIMAL(10,2) DEFAULT 0,

    ThanhTien AS (TongTien - GiamGia) PERSISTED,

    KhuyenMaiID INT,

    FOREIGN KEY (DonHangID) REFERENCES DonHang(DonHangID),
    FOREIGN KEY (KhuyenMaiID) REFERENCES KhuyenMai(KhuyenMaiID)
);

-- THANH TOÁN
CREATE TABLE ThanhToan (
    ThanhToanID INT IDENTITY(1,1) PRIMARY KEY,
    HoaDonID INT NOT NULL,

    PhuongThuc NVARCHAR(50),
    SoTien DECIMAL(10,2) CHECK (SoTien > 0),
    ThoiGian DATETIME DEFAULT GETDATE(),
    TrangThai NVARCHAR(50),

    FOREIGN KEY (HoaDonID) REFERENCES HoaDon(HoaDonID)
);

-- CA LÀM
CREATE TABLE CaLamViec (
    CaID INT IDENTITY(1,1) PRIMARY KEY,
    TenCa NVARCHAR(50),
    GioBatDau TIME,
    GioKetThuc TIME
);

-- KẾT CA
CREATE TABLE KetCa (
    KetCaID INT IDENTITY(1,1) PRIMARY KEY,

    CaID INT NOT NULL,
    NhanVienID INT NOT NULL,

    tgBatDau DATETIME,
    tgKetThuc DATETIME,

    TongDoanhThu DECIMAL(10,2),

    FOREIGN KEY (CaID) REFERENCES CaLamViec(CaID),
    FOREIGN KEY (NhanVienID) REFERENCES NhanVien(NhanVienID)
);


INSERT INTO NhanVien 
(TenNhanVien, ChucVu, DiaChi, Email, password, SDT, Luong, TrangThai)
VALUES
(N'Nguyễn Văn An',  N'QUAN_LY',      N'HCM', 'an@gmail.com',   '123', '0901111111', 15000000, '0'),
(N'Trần Bình',      N'KY_THUAT_VIEN',N'HCM', 'binh@gmail.com', '123', '0902222222', 12000000, '1'),
(N'Lê Cường',       N'KY_THUAT_VIEN',N'HCM', 'cuong@gmail.com','123', '0903333333', 12000000, '0'),
(N'Phạm Dũng',      N'NGUOI_DUNG',   N'HCM', 'dung@gmail.com', '123', '0904444444',  9000000, '1');