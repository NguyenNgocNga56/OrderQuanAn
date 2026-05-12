package backend.enums;

public enum TrangThaiMenu {
    DANG_HOAT_DONG("Đang hoạt động"),
    NGUNG_AP_DUNG("Ngưng áp dụng");

    private final String display;
    TrangThaiMenu(String display) { this.display = display; }
    public String getDisplay() { return display; }
}