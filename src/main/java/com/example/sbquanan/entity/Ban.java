package com.example.sbquanan.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Ban")
@Data
@NoArgsConstructor
public class Ban {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BanID")
    private Long banID;

    @Column(name = "TenBan", nullable = false, length = 50)
    private String tenBan;

    @Column(name = "ViTri", length = 100)
    private String viTri;

    @Column(name = "SoChoNgoi", nullable = false)
    private int soChoNgoi;

    @Column(name = "TrangThai", length = 30)
    private String trangThai = "TRONG";

    @Column(name = "LoaiBan", length = 50)
    private String loaiBan = "Ban thuong";

    @Column(name = "GhiChu", length = 100)
    private String ghiChu;
}