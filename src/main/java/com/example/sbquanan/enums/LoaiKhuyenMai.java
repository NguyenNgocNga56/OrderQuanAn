package com.example.sbquanan.enums;

public enum LoaiKhuyenMai {
    PHAN_TRAM("Phần Trăm"),
    GIAM_TIEN_MAT("Tiền mặt"),
    SO_TIEN("Số tiền");

    private final String display;
    LoaiKhuyenMai(String display) { this.display = display; }
    public String getDisplay() { return display; }
}