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
    @Column(name = "CTDH_ID")
    private Long ctdhID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DonHangID", nullable = false)
    private DonHang donHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MonID", nullable = false)
    private MonAn monAn;

    @Column(name = "SoLuong")
    private int soLuong;

    @Column(name = "DonGia")
    private double giaBan;

    @Column(name = "ThanhTien", insertable = false, updatable = false)
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
