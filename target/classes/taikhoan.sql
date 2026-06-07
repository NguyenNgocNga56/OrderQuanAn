create table NhanVien
(
    TenNhanVien varchar not null,
    Email       varchar not null,
    SDT         varchar not null,
    Luong       int     not null,
    ChucVu      varchar not null,
    TrangThai   int     not null,
    Password    varchar not null
);

INSERT INTO NhanVien 
(TenNhanVien, Email, SDT, Luong, ChucVu, TrangThai, Password)
VALUES
(N'Nguyễn Ngọc Ngà', 'ngngaplbl@gmail.com', '0911000000', 1000000, N'Quản lý', 1, '123456'),
(N'Trần Thảo Nương', 'admin1@gmail.com', '0922000000', 800000, N'Quản lý', 1, '123456'),
(N'Phạm Ngọc Tú', 'admin2@gmail.com', '0933000000', 800000, N'Quản lý', 1, '123456'),
(N'Trần Minh Tú', 'admin3@gmail.com', '0944000000', 800000, N'Quản lý', 1, '123456');