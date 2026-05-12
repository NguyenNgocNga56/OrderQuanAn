package com.example.sbquanan.entity;

import com.example.sbquanan.enums.TrangThaiMonAn;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("Đồ ăn")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DoAn extends MonAn {

    @Column(name = "DonViTinh", length = 20) // Đổi từ "Loai" sang "DonViTinh" cho khớp SQL
    private String donViTinh;

    // Constructor để bạn khởi tạo nhanh trong code
    public DoAn(String tenMon, double gia, String moTa, String hinhAnh,
                TrangThaiMonAn trangThai, Menu menu, String donViTinh) {
        setTenMon(tenMon); setGia(gia); setMoTa(moTa);
        setHinhAnh(hinhAnh); setTrangThai(trangThai); setMenu(menu);
        this.donViTinh = donViTinh;
    }

    @Override
    public double giaBan() {
        return this.getGia();
    }
}