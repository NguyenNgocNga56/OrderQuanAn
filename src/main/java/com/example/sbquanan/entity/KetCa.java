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
    private Long ketCaID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CaID")
    private CaLamViec caLamViec;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NhanVienID")
    private NhanVien nhanVien;

    @Column(name = "tgBatDau")
    private LocalDateTime tgBatDau;

    @Column(name = "tgKetThuc")
    private LocalDateTime tgKetThuc;

    @Column(name = "TongDoanhThu")
    private double tongDoanhThu = 0;
}
