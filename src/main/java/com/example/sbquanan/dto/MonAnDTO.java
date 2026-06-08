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

    // Chỉ có ở đồ uống: S / M / L (null nếu là đồ ăn)
    public String size;

    // Tên gốc để group các size lại với nhau ở frontend
    // VD: "Trà Xanh" thay vì "Trà Xanh S" / "Trà Xanh M" / "Trà Xanh L"
    public String loaiTen;

    public static MonAnDTO from(MonAn m) {
        MonAnDTO dto = new MonAnDTO();
        dto.monID     = m.getMonID();
        dto.tenMon    = m.getTenMon();
        dto.gia       = m.getGia();
        dto.moTa      = m.getMoTa();
        dto.hinhAnh   = m.getHinhAnh();
        dto.trangThai = m.getTrangThai() != null ? m.getTrangThai().name() : null;
        dto.phanLoai  = (m instanceof DoUong) ? "douong" : "doan";

        if (m instanceof DoUong doUong) {
            dto.size    = doUong.getSize() != null ? doUong.getSize().name() : null;
            // Tách suffix size ra khỏi tên để lấy tên gốc dùng để group
            // VD: "Trà Xanh S" → "Trà Xanh", hoặc giữ nguyên tenMon nếu không có suffix
            dto.loaiTen = stripSizeSuffix(m.getTenMon());
        } else {
            dto.loaiTen = m.getTenMon();
        }

        return dto;
    }

    /**
     * Tách suffix " S" / " M" / " L" cuối tên nếu có.
     * VD: "Trà Xanh S" → "Trà Xanh", "Matcha Latte" → "Matcha Latte"
     */
    private static String stripSizeSuffix(String name) {
        if (name == null) return null;
        String trimmed = name.trim();
        if (trimmed.matches("(?i).*\\s[SML]$")) {
            return trimmed.substring(0, trimmed.length() - 2).trim();
        }
        return trimmed;
    }
}