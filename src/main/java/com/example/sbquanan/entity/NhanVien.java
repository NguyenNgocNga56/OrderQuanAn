package com.example.sbquanan.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "NhanVien")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class NhanVien extends ConNguoi {
    @Column(name = "Luong")
    private double luong;

    @Column(name = "ChucVu", length = 50)
    private String chucVu;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "TrangThai")
    private boolean trangThai = true;
}
