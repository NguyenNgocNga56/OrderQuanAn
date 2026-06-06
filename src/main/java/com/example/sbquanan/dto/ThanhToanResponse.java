package com.example.sbquanan.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ThanhToanResponse {
    private Long thanhToanID;
    private Long hoaDonID;
    private Long donHangID;
    private String phuongThuc;
    private String trangThai;
    private double tongTien;
    private double giamGia;
    private double soTien;
    private int diemCong;
    private LocalDateTime thoiGian;
}
