package com.example.sbquanan.enums;

public enum TrangThaiDonHang {
    CHO_XAC_NHAN("Chờ xác nhận"),
    DANG_XU_LY("Đang xử lý"),
    DANG_GIAO("Đang giao"),
    HOAN_THANH("Hoàn thành"),
    DA_HUY("Đã hủy");

    private final String displayValue;
    TrangThaiDonHang(String displayValue) { this.displayValue = displayValue; }
    public String getDisplayValue() { return displayValue; }
}
