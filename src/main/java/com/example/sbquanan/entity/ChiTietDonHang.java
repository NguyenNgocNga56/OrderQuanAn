package com.example.sbquanan.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ChiTietDonHang")
@Data
@NoArgsConstructor
public class ChiTietDonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CTDHID")
    private Long ctdhID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DonHangID", nullable = false)
    private DonHang donHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MonID", nullable = false)
    private MonAn monAn;

    @Column(name = "SoLuong")
    private int soLuong;

    @Column(name = "GiaBan")
    private double giaBan;

    @Column(name = "TongTien")
    private double tongTien;

    @PrePersist
    @PreUpdate
    public void tinhToan() {
        if (monAn != null) {
            this.giaBan = monAn.giaBan();
            this.tongTien = this.soLuong * this.giaBan;
        }
    }

}
