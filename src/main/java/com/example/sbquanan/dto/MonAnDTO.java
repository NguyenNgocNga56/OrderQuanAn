package com.example.sbquanan.dto;

import com.example.sbquanan.entity.MonAn;
import com.example.sbquanan.entity.DoUong;

public class MonAnDTO {
    public Long monID;
    public String tenMon;
    public double gia;
    public String moTa;
    public String hinhAnh;
    public String trangThai;
    public String phanLoai;

    public static MonAnDTO from(MonAn m) {
        MonAnDTO dto = new MonAnDTO();
        dto.monID    = m.getMonID();
        dto.tenMon   = m.getTenMon();
        dto.gia      = m.getGia();
        dto.moTa     = m.getMoTa();
        dto.hinhAnh  = m.getHinhAnh();
        dto.trangThai = m.getTrangThai() != null ? m.getTrangThai().name() : null;
        dto.phanLoai  = (m instanceof DoUong) ? "douong" : "doan";
        return dto;
    }
}