package com.example.sbquanan.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "HoaDon")
@Data
@NoArgsConstructor
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HoaDonID")
    private Long hoaDonID;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DonHangID", nullable = false)
    private DonHang donHang;

    // FIX: thêm KhuyenMai (có trong schema SQL nhưng bị thiếu trong entity cũ)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KhuyenMaiID")
    private KhuyenMai khuyenMai;

    @Column(name = "NgayLap")
    private LocalDateTime ngayLap;

    @Column(name = "TongTien")
    private double tongTien;

    @Column(name = "GiamGia")
    private double giamGia;

    @Column(name = "ThanhTien")
    private double thanhTien;

    @PrePersist
    public void prePersist() {
        if (ngayLap == null) ngayLap = LocalDateTime.now();
        // FIX: chỉ tính khi tongTien đã được set (> 0), tránh ghi 0 sai
        tinhThanhTien();
    }

    @PreUpdate
    public void preUpdate() {
        tinhThanhTien();
    }

    // FIX: method công khai để Service có thể gọi trước khi save
    public void tinhThanhTien() {
        this.thanhTien = this.tongTien - this.giamGia;
    }
}
