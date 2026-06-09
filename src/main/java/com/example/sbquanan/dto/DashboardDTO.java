package com.example.sbquanan.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DashboardDTO {

    private TongQuan tongQuan;
    private List<DonHangDTO> donHangs;

    @Data
    public static class TongQuan {
        private long tongDon;
        private long donChoXacNhan;
        private long donDangNau;
        private long donDaPhucVu;
        private long donHoanThanh;
        private long donDaHuy;
        private double doanhThuHomNay;
    }

    @Data
    public static class DonHangDTO {
        private Long donHangID;
        private String trangThai;
        private String tenKhachHang;
        private String sdtKhach;
        private String tenNhanVien;
        private String tenBan;
        private double tongTien;
        private double giamGia;
        private double thanhTien;
        private LocalDateTime ngayDat;
        private List<MonDTO> chiTiet;
    }

    @Data
    public static class MonDTO {
        private String tenMon;
        private int soLuong;
        private double donGia;
        private double thanhTien;
    }
}