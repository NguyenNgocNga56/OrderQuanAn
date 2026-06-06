package com.example.sbquanan.enums;

public enum TrangThaiMonAn {
    CON_HANG("Còn hàng"),
    HET_HANG("Hết hàng"),
    NGUNG_BAN("Ngừng bán");

    private final String displayValue;
    TrangThaiMonAn(String displayValue) { this.displayValue = displayValue; }
    public String getDisplayValue() { return displayValue; }
}
