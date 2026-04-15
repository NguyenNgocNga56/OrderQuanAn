package backend.entity;

import java.time.LocalDateTime;

public class HoaDon {
    private String idHoaDon;
    private String idDonHang;
    private LocalDateTime ngayLap;
    private double tongTien; // Lấy từ DonHang sang
    private double giamGia;  // Tiền được giảm (từ KhuyenMai hoặc Member)
    private double thanhTien; // = tongTien - giamGia

    // ================ CONSTRUCTOR =================
    public HoaDon() {}

    public HoaDon(String idHoaDon, String idDonHang, double tongTien, double giamGia) {
        this.idHoaDon = idHoaDon;
        this.idDonHang = idDonHang;
        this.ngayLap = LocalDateTime.now();
        this.tongTien = tongTien;
        this.giamGia = giamGia;
        // Tính toán thành tiền ngay khi khởi tạo
        this.thanhTien = this.tongTien - this.giamGia;
    }

    // ================ HÀM HỖ TRỢ =================
    // Auto update thành tiền khi giá trị đổi
    private void updateThanhTien() {
        this.thanhTien = this.tongTien - this.giamGia;
    }

    // ================ GETTER =================


    public String getIdHoaDon() {
        return idHoaDon;
    }

    public String getIdDonHang() {
        return idDonHang;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public double getTongTien() {
        return tongTien;
    }

    public double getGiamGia() {
        return giamGia;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    // ================ SETTER =================
    public void setIdHoaDon(String idHoaDon) {
        this.idHoaDon = idHoaDon;
    }

    public void setIdDonHang(String idDonHang) {
        this.idDonHang = idDonHang;
    }

    public void setNgayLap(LocalDateTime ngayLap) {
        this.ngayLap = ngayLap;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
        updateThanhTien();
    }

    public void setGiamGia(double giamGia) {
        this.giamGia = giamGia;
        updateThanhTien();
    }

}