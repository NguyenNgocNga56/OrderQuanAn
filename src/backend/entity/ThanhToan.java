package backend.entity;

import backend.enums.PhuongThucThanhToan;
import backend.enums.TrangThaiThanhToan;
import java.time.LocalDateTime;

public class ThanhToan {
    private String idThanhToan;
    private String idHoaDon;
    private PhuongThucThanhToan phuongThuc;
    private double soTien;
    private LocalDateTime thoiGian;
    private TrangThaiThanhToan trangThai;

    // ================ CONSTRUCTOR =================
    public ThanhToan() {}

    public ThanhToan(String idThanhToan, String idHoaDon, PhuongThucThanhToan phuongThuc, double soTien, TrangThaiThanhToan trangThai) {
        this.idThanhToan = idThanhToan;
        this.idHoaDon = idHoaDon;
        this.phuongThuc = phuongThuc;
        this.soTien = soTien;
        this.trangThai = trangThai;
        this.thoiGian = LocalDateTime.now();
    }

    // ================ GETTER =================
    public String getIdThanhToan() {
        return idThanhToan;
    }

    public String getIdHoaDon() {
        return idHoaDon;
    }

    public PhuongThucThanhToan getPhuongThuc() {
        return phuongThuc;
    }

    public double getSoTien() {
        return soTien;
    }

    public LocalDateTime getThoiGian() {
        return thoiGian;
    }

    public TrangThaiThanhToan getTrangThai() {
        return trangThai;
    }

    // ================ SETTER =================
    public void setIdThanhToan(String idThanhToan) {
        this.idThanhToan = idThanhToan;
    }

    public void setIdHoaDon(String idHoaDon) {
        this.idHoaDon = idHoaDon;
    }

    public void setPhuongThuc(PhuongThucThanhToan phuongThuc) {
        this.phuongThuc = phuongThuc;
    }

    public void setSoTien(double soTien) {
        this.soTien = soTien;
    }

    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
    }

    public void setTrangThai(TrangThaiThanhToan trangThai) {
        this.trangThai = trangThai;
    }
}