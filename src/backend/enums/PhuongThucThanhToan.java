package backend.enums;

public enum PhuongThucThanhToan {
    TIEN_MAT("Tiền mặt"),
    CHUYEN_KHOAN("Chuyển khoản"),
    THE_NGAN_HANG("Thẻ ngân hàng"),
    VI_DIEN_TU("Ví điện tử");

    private final String hienThi;
    PhuongThucThanhToan(String hienThi) { this.hienThi = hienThi; }
    public String getHienThi() { return hienThi; }

}