package com.example.sbquanan.entity;

import com.example.sbquanan.enums.Role;        
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "NhanVien")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "NhanVienID")),
        @AttributeOverride(name = "hoTen", column = @Column(name = "TenNhanVien", nullable = false, length = 100))
})
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class NhanVien extends ConNguoi {
    @Column(name = "Luong")
    private double luong;

    @Column(name = "ChucVu", length = 50)
    private String chucVu;

    @Column(name = "Password", nullable = false, length = 255)
    private String password;

    @Column(name = "TrangThai")
    private boolean trangThai = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role", length = 20)
    private Role role = Role.NHAN_VIEN;
}