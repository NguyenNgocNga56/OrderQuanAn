package com.example.sbquanan.enums;

public enum TrangThaiMenu {
    DANG_HOAT_DONG("Đang hoạt động"),
    NGUNG_AP_DUNG("Ngưng áp dụng"),
    ACTIVE("Đang hoạt động");

    private final String display;
    TrangThaiMenu(String display) { this.display = display; }
    public String getDisplay() { return display; }
}