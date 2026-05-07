package com.example.sbquanan.entity;

import com.example.sbquanan.enums.LoaiKhachHang;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "KhachHang")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class KhachHang extends ConNguoi {
    @Column(name = "DiemTichLuy")
    private int diemTichLuy = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "LoaiKhachHang", length = 20)
    private LoaiKhachHang loaiKhachHang = LoaiKhachHang.DONG;

    public void capNhatHangKhachHang() {
        if (diemTichLuy >= 1000) loaiKhachHang = LoaiKhachHang.KIM_CUONG;
        else if (diemTichLuy >= 500) loaiKhachHang = LoaiKhachHang.VANG;
        else if (diemTichLuy >= 100) loaiKhachHang = LoaiKhachHang.BAC;
        else loaiKhachHang = LoaiKhachHang.DONG;
    }
}
