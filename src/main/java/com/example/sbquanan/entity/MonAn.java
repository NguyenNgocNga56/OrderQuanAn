package com.example.sbquanan.entity;

import com.example.sbquanan.enums.TrangThaiMonAn;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MonAn")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PhanLoai", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
public abstract class MonAn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MonID")
    private Long monID;

    @Column(name = "TenMon", nullable = false, length = 100)
    private String tenMon;

    @Column(name = "Gia")
    private double gia;

    @Column(name = "MoTa", length = 255)
    private String moTa;

    @Column(name = "HinhAnh", length = 255)
    private String hinhAnh;

    @Enumerated(EnumType.STRING)
    @Column(name = "TrangThai", length = 50)
    private TrangThaiMonAn trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MenuID", nullable = false)
    private Menu menu;

    public abstract double giaBan();
}
