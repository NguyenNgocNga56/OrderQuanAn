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
    @Column(name = "CTDH_ID") // Sửa lại cho khớp SQL
    private Integer ctdhID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DonHangID", nullable = false)
    private DonHang donHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MonID", nullable = false)
    private MonAn monAn;

    @Column(name = "SoLuong")
    private int soLuong;

    @Column(name = "DonGia") // Sửa từ giaBan thành DonGia
    private double donGia;

    // Cột này SQL tự tính nên ta để insertable và updatable = false
    @Column(name = "ThanhTien", insertable = false, updatable = false)
    private double thanhTien;

    // Logic này vẫn giữ để dùng trong Java nếu cần,
    // nhưng khi lưu xuống DB, SQL sẽ tự tính lại theo công thức của nó.
    @PrePersist
    @PreUpdate
    public void tinhToan() {
        if (monAn != null) {
            // Lưu ý: Đảm bảo trong MonAn có field 'gia' hoặc 'donGia' tương ứng
            this.donGia = monAn.getGia();
        }
    }
}
