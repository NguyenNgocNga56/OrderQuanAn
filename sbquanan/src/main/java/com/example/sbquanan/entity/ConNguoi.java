package com.example.sbquanan.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Data
@NoArgsConstructor
public abstract class ConNguoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "HoTen", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "SDT", length = 15)
    private String sdt;

    @Column(name = "DiaChi", length = 255)
    private String diaChi;

    @Column(name = "Email", length = 100)
    private String email;
}
