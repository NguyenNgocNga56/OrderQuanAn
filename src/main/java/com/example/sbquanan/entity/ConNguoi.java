package com.example.sbquanan.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Data
@NoArgsConstructor
public abstract class ConNguoi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Họ tên không được để trống")
    @Column(name = "HoTen", nullable = false, length = 100)
    private String hoTen;

    @Pattern(regexp = "^0[0-9]{9}$", message = "Số điện thoại không hợp lệ (VD: 0912345678)")
    @Column(name = "SDT", length = 15)
    private String sdt;

    @Column(name = "DiaChi", length = 255)
    private String diaChi;

    @Email(message = "Email không hợp lệ")
    @Column(name = "Email", length = 100)
    private String email;
}