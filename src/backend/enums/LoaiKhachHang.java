package backend.enums;

public enum LoaiKhachHang {
    DONG("Đồng", 0),
    BAC("Bạc", 0.05),
    VANG("Vàng", 0.1),
    KIM_CUONG("Kim cương", 0.15);

    private final String tenHienThi;
    private final double phanTramGiamGia;

    LoaiKhachHang(String tenHienThi, double phanTramGiamGia) {
        this.tenHienThi = tenHienThi;
        this.phanTramGiamGia = phanTramGiamGia;
    }

    public String getTenHienThi() { return tenHienThi; }
    public double getPhanTramGiamGia() { return phanTramGiamGia; }
}