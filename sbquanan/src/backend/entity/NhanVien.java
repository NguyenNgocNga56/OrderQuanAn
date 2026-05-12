package com.example.sbquanan.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "NhanVien")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "NhanVienID")),
        @AttributeOverride(name = "hoTen", column = @Column(name = "TenNhanVien"))
})
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class NhanVien extends ConNguoi {
    @Column(name = "Luong")
    private double luong;

    @Column(name = "ChucVu", length = 50)
    private String chucVu;

    // BẮT BUỘC: Bỏ 'final' để Hibernate không báo lỗi
    @Column(name = "TrangThai")
    private boolean trangThai = true;
}