package backend.enums;

public enum TrangThaiThanhToan {
    CHO_XU_LY("Chờ xử lý"),
    THANH_CONG("Thành công"),
    THAT_BAI("Thất bại");

    private final String hienThi;
    TrangThaiThanhToan(String hienThi) { this.hienThi = hienThi; }
    public String getHienThi() { return hienThi; }
}