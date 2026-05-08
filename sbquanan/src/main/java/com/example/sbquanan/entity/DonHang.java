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
    private Integer donHangID;

    @Enumerated(EnumType.STRING)
    @Column(name = "TrangThai", length = 50)
    private TrangThaiDonHang trangThai;

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

    // Bỏ final để Hibernate không bị lỗi khi mapping
    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChiTietDonHang> chiTietDonHangs = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (ngayDat == null) ngayDat = LocalDateTime.now();
    }

    // Đã sửa thành getThanhTien() để khớp với file ChiTietDonHang mình sửa lúc nãy
    public void tuDongCapNhatTongTien() {
        if (chiTietDonHangs != null) {
            this.tongTien = chiTietDonHangs.stream()
                    .mapToDouble(ChiTietDonHang::getThanhTien)
                    .sum();
        }
    }
}
