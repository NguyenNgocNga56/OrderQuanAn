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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MonID", nullable = false)
    private MonAn monAn;

    @Column(name = "SoLuong")
    private int soLuong;

    // FIX: giaBan được set từ service (sau khi load monAn xong) thay vì trong @PrePersist
    @Column(name = "GiaBan")
    private double giaBan;

    @Column(name = "TongTien")
    private double tongTien;

    // FIX: tách tinhToan() thành method thường, gọi từ Service sau khi set đủ dữ liệu
    public void tinhToan() {
        if (monAn != null) {
            this.giaBan = monAn.giaBan();
            this.tongTien = this.soLuong * this.giaBan;
        }
    }

    @PrePersist
    @PreUpdate
    public void prePersistUpdate() {
        // Chỉ tính tongTien từ giaBan đã được set sẵn, không gọi lazy load
        this.tongTien = this.soLuong * this.giaBan;
    }
}
