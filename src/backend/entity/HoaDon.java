package backend.entity;

import java.time.LocalDateTime;

public class HoaDon {
    private String HoaDonID;
    private String DonHangID;
    private LocalDateTime NgayLap;
    private double TongTien; // Lấy từ DonHang sang
    private double GiamGia;  // Tiền được giảm (từ KhuyenMai hoặc Member)
    private double ThanhTien; // = tongTien - giamGia

    // ================ CONSTRUCTOR =================
    public HoaDon() {}

    public HoaDon(String HoaDonID, String DonHangID, double TongTien, double GiamGia) {
        this.HoaDonID = HoaDonID;
        this.DonHangID = DonHangID;
        this.NgayLap = LocalDateTime.now();
        this.TongTien = TongTien;
        this.GiamGia = GiamGia;
        // Tính toán thành tiền ngay khi khởi tạo
        this.ThanhTien = this.TongTien - this.GiamGia;
    }

    // ================ HÀM HỖ TRỢ =================
    // Auto update thành tiền khi giá trị đổi
    private void updateThanhTien() {
        this.ThanhTien = this.TongTien - this.GiamGia;
    }

    // ================ GETTER =================


    public String getIdHoaDon() {
        return HoaDonID;
    }

    public String getIdDonHang() {
        return DonHangID;
    }

    public LocalDateTime getNgayLap() {
        return NgayLap;
    }

    public double getTongTien() {
        return TongTien;
    }

    public double getGiamGia() {
        return GiamGia;
    }

    public double getThanhTien() {
        return ThanhTien;
    }

    // ================ SETTER =================
    public void setIdHoaDon(String HoaDonID) {
        this.HoaDonID = HoaDonID;
    }

    public void setIdDonHang(String DonHangID) {
        this.DonHangID = DonHangID;
    }

    public void setNgayLap(LocalDateTime NgayLap) {
        this.NgayLap = NgayLap;
    }

    public void setTongTien(double TongTien) {
        this.TongTien = TongTien;
        updateThanhTien();
    }

    public void setGiamGia(double GiamGia) {
        this.GiamGia = GiamGia;
        updateThanhTien();
    }
}
