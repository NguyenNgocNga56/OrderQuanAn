package com.example.sbquanan.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "NhanVien")
@Data
@NoArgsConstructor
public class NhanVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NhanVienID")
    private Long id;

    @Column(name = "TenNhanVien", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "SDT", length = 15)
    private String sdt;

    @Column(name = "DiaChi", length = 255)
    private String diaChi;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "Luong")
    private double luong;

    @Column(name = "ChucVu", length = 50)
    private String chucVu;

    @Column(name = "TrangThai")
    private boolean trangThai = true;

    @Column(name = "Password", length = 255)
    private String password;
}