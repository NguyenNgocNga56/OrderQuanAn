package com.example.sbquanan.entity;

import com.example.sbquanan.enums.LoaiKhuyenMai;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "KhuyenMai")
@Data
@NoArgsConstructor
public class KhuyenMai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "KhuyenMaiID")
    private Long khuyenMaiID;

    @Column(name = "TenKhuyenMai", length = 100)
    private String tenKhuyenMai;

    @Column(name = "MaKhuyenMai", length = 50)
    private String maKhuyenMai;

    @Enumerated(EnumType.STRING)
    @Column(name = "LoaiKhuyenMai", length = 50)
    private LoaiKhuyenMai loaiKhuyenMai;

    @Column(name = "GiaTri")
    private double giaTri;

    @Column(name = "DiemToiThieu")
    private int diemToiThieu = 0;

    @Column(name = "TongTienToiThieu")
    private double tongTienToiThieu = 0;

    @Column(name = "NgayBatDau")
    private LocalDateTime ngayBatDau;

    @Column(name = "NgayKetThuc")
    private LocalDateTime ngayKetThuc;

    @Column(name = "TrangThai")
    private Boolean trangThai = true;

    @Column(name = "MoTa", length = 255)
    private String moTa;

    public boolean hopLe() {
        LocalDateTime now = LocalDateTime.now();
        boolean dangBat = trangThai == null || trangThai;
        return dangBat
                && ngayBatDau != null && ngayKetThuc != null
                && !now.isBefore(ngayBatDau) && !now.isAfter(ngayKetThuc);
    }

    public boolean duDieuKien(double tongTien, KhachHang khachHang) {
        if (!hopLe()) return false;
        if (tongTien < tongTienToiThieu) return false;
        return diemToiThieu <= 0
                || (khachHang != null && khachHang.getDiemTichLuy() >= diemToiThieu);
    }

    public double tinhTienGiam(double tongTien) {
        double tienGiam = 0;
        switch (loaiKhuyenMai) {
            case PHAN_TRAM:
                tienGiam = tongTien * giaTri / 100;
                break;
            case GIAM_TIEN_MAT:
                tienGiam = giaTri;
                break;
        }
        return Math.min(Math.max(tienGiam, 0), tongTien);
    }
}
