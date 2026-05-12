package com.example.sbquanan.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "KetCa")
@Data
@NoArgsConstructor
public class KetCa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "KetCaID")
    private Integer ketCaID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CaID", nullable = false)
    private CaLamViec caLamViec;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NhanVienID", nullable = false)
    private NhanVien nhanVien;

    // Sửa lại tên cột cho khớp chính xác với chữ thường/hoa trong SQL (tgBatDau)
    @Column(name = "tgBatDau")
    private LocalDateTime tgBatDau;

    @Column(name = "tgKetThuc")
    private LocalDateTime tgKetThuc;

    // BẮT BUỘC: Bỏ 'final' để Hibernate có thể map dữ liệu
    @Column(name = "TongDoanhThu")
    private double tongDoanhThu;
}
