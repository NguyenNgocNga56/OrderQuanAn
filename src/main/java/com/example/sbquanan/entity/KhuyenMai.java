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

    @Enumerated(EnumType.STRING)
    @Column(name = "LoaiKhuyenMai", length = 50)
    private LoaiKhuyenMai loaiKhuyenMai;

    @Column(name = "GiaTri")
    private double giaTri;

    @Column(name = "NgayBatDau")
    private LocalDateTime ngayBatDau;

    @Column(name = "NgayKetThuc")
    private LocalDateTime ngayKetThuc;

    @Column(name = "DangKichHoat")
    private boolean dangKichHoat;

    public boolean hopLe() {
        LocalDateTime now = LocalDateTime.now();
        return dangKichHoat && now.isAfter(ngayBatDau) && now.isBefore(ngayKetThuc);
    }
}
