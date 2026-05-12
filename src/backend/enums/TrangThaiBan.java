package backend.enums;

public enum TrangThaiBan {
    TRONG("Trống"),
    DAY("Đầy"),
    DEP_BAN("Dẹp bàn");

    private final String displayValue;

    TrangThaiBan(String displayValue) {this.displayValue = displayValue; }

    public String getDisplayValue() {
        return displayValue;
    }
}
