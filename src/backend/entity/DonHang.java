package backend.entity;

import backend.enums.TrangThaiDonHang;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class DonHang {
    private String idDonHang;
    private TrangThaiDonHang trangThai;
    private double tongTien;
    private LocalDateTime ngayDat;
    private String idKhachHang;
    private String idNhanVien;

    private Map<String, ChiTietDonHang> items = new LinkedHashMap<>(); // Lưu các chi tiết đơn hàng

    // ================ CONSTRUCTOR =================
    public DonHang(){}

    public DonHang(String idDonHang, TrangThaiDonHang trangThai, double tongTien, LocalDateTime ngayDat, String idKhachHang, String idNhanVien) {
        this.idDonHang = idDonHang;
        this.trangThai = trangThai;
        this.tongTien = tongTien;
        this.ngayDat = ngayDat;
        this.idKhachHang = idKhachHang;
        this.idNhanVien = idNhanVien;
    }

    // ================ HÀM CHỨC NĂNG =================
    // Auto update tổng tiền
    public void tuDongCapNhatTongTien() {
        double temp = 0;
        for (ChiTietDonHang ct : items.values()) {
            temp += ct.gettongTien(); // Lấy tổng tiền từng dòng cộng lại
        }
        this.tongTien = temp;
    }

    // ================ GETTER =================

    public String getIdDonHang() {
        return idDonHang;
    }

    public TrangThaiDonHang getTrangThai() {
        return trangThai;
    }

    public double getTongTien() {
        return tongTien;
    }

    public LocalDateTime getNgayDat() {
        return ngayDat;
    }

    public String getIdKhachHang() {
        return idKhachHang;
    }

    public String getIdNhanVien() {
        return idNhanVien;
    }

    // ================ SETTER =================
    public void setIdDonHang(String idDonHang) {
        this.idDonHang = idDonHang;
    }

    public void setTrangThai(TrangThaiDonHang trangThai) {
        this.trangThai = trangThai;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public void setNgayDat(LocalDateTime ngayDat) {
        this.ngayDat = ngayDat;
    }

    public void setIdKhachHang(String idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    public void setIdNhanVien(String idNhanVien) {
        this.idNhanVien = idNhanVien;
    }
}
