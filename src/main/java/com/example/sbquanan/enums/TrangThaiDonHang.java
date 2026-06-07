package com.example.sbquanan.enums;

public enum TrangThaiDonHang {
    CHO_XAC_NHAN("Chờ xác nhận"),
    DANG_NAU("Đang nấu"),
    DA_PHUC_VU("Đã phục vụ"),
    HOAN_THANH("Hoàn thành"),
    DA_HUY("Đã hủy");

    private final String displayValue;
    TrangThaiDonHang(String displayValue) { this.displayValue = displayValue; }
    public String getDisplayValue() { return displayValue; }
}