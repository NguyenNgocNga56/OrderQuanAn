package com.example.sbquanan.entity;

import com.example.sbquanan.enums.PhuongThucThanhToan;
import com.example.sbquanan.enums.TrangThaiThanhToan;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "ThanhToan")
@Data
@NoArgsConstructor
public class ThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ThanhToanID")
    private Long thanhToanID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HoaDonID", nullable = false)
    private HoaDon hoaDon;

    @Enumerated(EnumType.STRING)
    @Column(name = "PhuongThuc", length = 50)
    private PhuongThucThanhToan phuongThuc;

    @Column(name = "SoTien")
    private double soTien;

    @Column(name = "ThoiGian")
    private LocalDateTime thoiGian;

    @Enumerated(EnumType.STRING)
    @Column(name = "TrangThai", length = 30)
    private TrangThaiThanhToan trangThai;

    @PrePersist
    public void prePersist() {
        if (thoiGian == null) thoiGian = LocalDateTime.now();
    }
}
