package backend.enums;

public enum TrangThaiDonHang {
    DA_NHAN_DON_HANG("Đã nhận đơn hàng"),
    DA_HUY_DON_HANG("Đã hủy đơn hàng");

    private final String displayValue;

    TrangThaiDonHang(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}