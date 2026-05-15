package com.example.sbquanan.entity;

import com.example.sbquanan.enums.TrangThaiDonHang;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DonHang")
@Data
@NoArgsConstructor
public class DonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DonHangID")
    private Long donHangID;

    @Enumerated(EnumType.STRING)
    @Column(name = "TrangThai", length = 50)
    private TrangThaiDonHang trangThai = TrangThaiDonHang.CHO_XAC_NHAN;

    @Column(name = "TongTien")
    private double tongTien;

    @Column(name = "NgayDat")
    private LocalDateTime ngayDat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KhachHangID")
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NhanVienID")
    private NhanVien nhanVien;

    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChiTietDonHang> chiTietDonHangs = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (ngayDat == null) ngayDat = LocalDateTime.now();
    }

    public void tuDongCapNhatTongTien() {
        this.tongTien = chiTietDonHangs.stream()
                .mapToDouble(ChiTietDonHang::getTongTien).sum();
    }
}
