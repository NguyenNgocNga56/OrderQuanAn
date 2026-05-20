# Hệ Thống Quản Lý Quán Ăn — Restaurant Order & Management System

> Ứng dụng backend quản lý toàn diện cho quán ăn: từ thực đơn, đặt bàn, gọi món đến thanh toán và ca làm việc nhân viên.

---

## Giới Thiệu

**sbquanan** (Spring Boot Quản Ăn) là một REST API backend được xây dựng bằng **Spring Boot 3**, phục vụ cho bài toán quản lý quán ăn hiện đại. Hệ thống cho phép:

- **Nhân viên** ghi nhận đơn hàng, quản lý bàn ăn theo thời gian thực
- **Quản lý thực đơn** linh hoạt với đồ ăn và đồ uống (kèm size)
- **Xuất hóa đơn** tự động, áp dụng khuyến mãi
- **Thanh toán** đa phương thức
- **Theo dõi ca làm việc** và kết ca cuối ngày
- **Phân loại khách hàng** (thường, VIP, thành viên...)

Dù bạn là sinh viên tìm hiểu Spring Boot hay lập trình viên muốn tham khảo một hệ thống quản lý nhà hàng hoàn chỉnh — đây là project phù hợp để học hỏi và mở rộng.

---

## Kiến Trúc & Công Nghệ

```
sbquanan/
├── controller/     # REST API endpoints
├── entity/         # JPA entities (ánh xạ bảng CSDL)
├── repository/     # Spring Data JPA repositories
├── enums/          # Các trạng thái hệ thống
└── resources/
    └── static/     # Frontend HTML/CSS/JS (admin + client)
```

| Thành phần | Công nghệ |
|---|---|
| **Backend** | Spring Boot 3.3.5, Spring Data JPA |
| **Database** | Microsoft SQL Server |
| **ORM** | Hibernate |
| **Build tool** | Maven |
| **Java version** | Java 17 |
| **Tiện ích** | Lombok |
| **Frontend** | HTML, CSS, JavaScript thuần |

---

## ️ Các Module Chính

###  Quản lý Bàn (`Ban`)
Theo dõi trạng thái bàn: `TRONG` / `CO_KHACH` / `DAT_TRUOC`. Mỗi bàn có thể liên kết với đơn hàng đang hoạt động.

### ️ Thực Đơn & Món Ăn (`MonAn`, `Menu`)
- `MonAn` là class cha (abstract) dùng kiểu thừa kế `SINGLE_TABLE`
- `DoAn` — đồ ăn thông thường
- `DoUong` — đồ uống có thêm thuộc tính `size` (S/M/L)
- Menu tổng hợp nhiều món, có trạng thái hoạt động/ẩn

###  Đơn Hàng (`DonHang`, `ChiTietDonHang`)
Khách gọi món → tạo đơn hàng → từng dòng chi tiết đơn lưu số lượng + đơn giá. Trạng thái đơn: `CHO_XAC_NHAN` → `DANG_NAU` → `DA_PHUC_VU` → `DA_HUY`.

###  Hóa Đơn & Thanh Toán (`HoaDon`, `ThanhToan`)
Sau khi phục vụ xong, hệ thống tự tính `tongTien`, áp dụng `giamGia` (từ khuyến mãi), xuất `thanhTien`. Hỗ trợ: tiền mặt, chuyển khoản, thẻ.

###  Khuyến Mãi (`KhuyenMai`)
Hai loại: giảm theo **phần trăm** hoặc giảm **số tiền cố định**.

### Nhân Viên & Ca Làm Việc (`NhanVien`, `CaLamViec`, `KetCa`)
Ghi nhận giờ vào/ra ca, kết ca cuối ngày với tổng doanh thu, số đơn xử lý.

###  Khách Hàng (`KhachHang`)
Phân loại: `THUONG`, `THANH_VIEN`, `VIP` — cơ sở để áp dụng ưu đãi.

---

##  Hướng Dẫn Cài Đặt & Chạy

### Yêu cầu hệ thống
-  Java 17+
- ️ SQL Server (local hoặc Docker)
-  Maven 3.x

### Bước 1 — Clone project
```bash
git clone <repository-url>
cd sbquanan
```

### Bước 2 — Tạo database
Chạy file SQL có sẵn trong project:
```
src/main/resources/static/QUANLI_APPODER_DOAN.sql
```
Mở SQL Server Management Studio (SSMS) hoặc Azure Data Studio, kết nối vào SQL Server của bạn và chạy file này.

### Bước 3 — Cấu hình kết nối
Tạo file `src/main/resources/application.properties` từ mẫu:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```
Chỉnh sửa thông tin kết nối:
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=QUANLI_APPODER_DOAN;encrypt=true;trustServerCertificate=true
spring.datasource.username=<your_username>
spring.datasource.password=<your_password>
```

> ️ **Lưu ý:** Không commit file `application.properties` lên Git — nó đã được thêm vào `.gitignore`.

### Bước 4 — Build & chạy
```bash
# Chạy bằng Maven Wrapper 
./mvnw spring-boot:run

# Hoặc trên Windows
mvnw.cmd spring-boot:run
```

### Bước 5 — Truy cập ứng dụng
| Endpoint | Mô tả |
|---|---|
| `http://localhost:8080` | Giao diện khách hàng |
| `http://localhost:8080/admin.html` | Giao diện quản trị |
| `http://localhost:8080/api/...` | REST API |

---

##  API Endpoints Nổi Bật

| Method | Endpoint | Mô tả |
|---|---|---|
| `GET` | `/api/monan` | Lấy toàn bộ món ăn |
| `GET` | `/api/monan/menu/{menuId}` | Món theo menu |
| `GET` | `/api/ban` | Danh sách bàn & trạng thái |
| `POST` | `/api/donhang` | Tạo đơn hàng mới |
| `PUT` | `/api/donhang/{id}` | Cập nhật trạng thái đơn |
| `POST` | `/api/hoadon` | Xuất hóa đơn |
| `POST` | `/api/thanhtoan` | Ghi nhận thanh toán |
| `GET` | `/api/khuyenmai` | Danh sách khuyến mãi |
| `POST` | `/api/calv` | Tạo ca làm việc |
| `POST` | `/api/ketca` | Kết thúc ca |

---

##  Ghi Chú Phát Triển

- Project chưa có tầng **Service** — business logic hiện nằm trực tiếp trong Controller. Refactor sang Service layer là bước cải thiện tiếp theo.
- Chưa có **Spring Security / JWT** — nên bổ sung nếu deploy production.
- Frontend được nhúng trực tiếp vào `src/main/resources/static/` — có thể tách ra thành project riêng (React/Vue) khi mở rộng.

---

##  Nhóm SixeVEn

- **Nguyễn Ngọc Ngà** — Branch: `NguyenNgocNga`
- **Trần Thảo Nương** — Branch: `TranThaoNuong`
- **Phạm Ngọc Tú** — Branch: `PhamNgocTu`
- **Trần Minh Tú** — Branch: `MinhTu94`

---
