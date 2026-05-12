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
    private Integer hoaDonID;

    // Quan hệ 1-1 với Đơn Hàng (Unique trong SQL)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DonHangID", nullable = false, unique = true)
    private DonHang donHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KhuyenMaiID")
    private KhuyenMai khuyenMai;

    @Column(name = "NgayLap")
    private LocalDateTime ngayLap;

    @Column(name = "TongTien")
    private double tongTien;

    @Column(name = "GiamGia")
    private double giamGia;

    @Column(name = "ThanhTien", insertable = false, updatable = false)
    private double thanhTien;

    @PrePersist
    public void prePersist() {
        if (ngayLap == null) {
            ngayLap = LocalDateTime.now();
        }
    }
}
