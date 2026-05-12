package com.example.sbquanan.entity;

import com.example.sbquanan.enums.SizeDoUong;
import com.example.sbquanan.enums.TrangThaiMonAn;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("Đồ uống")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor

public class DoUong extends MonAn {

    @Enumerated(EnumType.STRING)
    @Column(name = "Loai", length = 50) // Đồ uống dùng cột Loai để lưu Size
    private SizeDoUong size;

    public DoUong(String tenMon, double gia, String moTa, TrangThaiMonAn trangThai,
                  Menu menu, SizeDoUong size) {
        setTenMon(tenMon); setGia(gia); setMoTa(moTa);
        setTrangThai(trangThai); setMenu(menu);
        this.size = size;
    }

    @Override
    public double giaBan() {
        return this.getGia() + (size != null ? size.getPhuPhi() : 0);
    }
}