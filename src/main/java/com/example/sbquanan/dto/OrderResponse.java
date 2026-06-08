package com.example.sbquanan.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {

    private Long donHangID;
    private Long hoaDonID;
    private String trangThai;
    private double tongTien;
    private double giamGia;
    private LocalDateTime ngayDat;

    private String tenBan;
    private String tenKhachHang;
    private String tenNhanVien;

    private List<ItemDetail> chiTiet;

    @Data
    public static class ItemDetail {
        private String tenMon;
        private String hinhAnh;
        private int    soLuong;
        private double donGia;
        private double thanhTien;
    }
}