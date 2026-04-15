package backend.entity;

import backend.enums.LoaiKhuyenMai;

import java.time.LocalDateTime;

public class KhuyenMai {
    private String idKhuyenMai;
    private String tenKhuyenMai;
    private LoaiKhuyenMai loaiKhuyenMai;  //tạm thời để string nhé, chứ t tính dùng enum :Đ
    private double giaTri;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private boolean dangKichHoat;

    // ================ CONSTRUCTOR =================
    public KhuyenMai() {}

    public KhuyenMai(String idKhuyenMai, String tenKhuyenMai, LoaiKhuyenMai loaiKhuyenMai, double giaTri, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, boolean dangKichHoat) {
        this.idKhuyenMai = idKhuyenMai;
        this.tenKhuyenMai = tenKhuyenMai;
        this.loaiKhuyenMai = loaiKhuyenMai;
        this.giaTri = giaTri;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.dangKichHoat = dangKichHoat;
    }

    // ================ HÀM CHỨC NĂNG =================
    // Check xem mã còn hạn dùng k
    public boolean conHieuLuc() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(ngayBatDau) && now.isBefore(ngayKetThuc);
    }
    public boolean hopLe() {
        LocalDateTime now = LocalDateTime.now();
        boolean trongThoiHan = now.isAfter(ngayBatDau) && now.isBefore(ngayKetThuc);
        return dangKichHoat && trongThoiHan;
    }

    // ================ GETTER =================
    public String getIdKhuyenMai() {
        return idKhuyenMai;
    }

    public String getTenKhuyenMai() {
        return tenKhuyenMai;
    }

    public LoaiKhuyenMai getLoaiKhuyenMai() {
        return loaiKhuyenMai;
    }

    public double getGiaTri() {
        return giaTri;
    }

    public LocalDateTime getNgayBatDau() {
        return ngayBatDau;
    }

    public LocalDateTime getNgayKetThuc() {
        return ngayKetThuc;
    }

    public boolean isDangKichHoat() {
        return dangKichHoat;
    }


    // ================ SETTER =================
    public void setIdKhuyenMai(String idKhuyenMai) {
        this.idKhuyenMai = idKhuyenMai;
    }

    public void setTenKhuyenMai(String tenKhuyenMai) {
        this.tenKhuyenMai = tenKhuyenMai;
    }

    public void setLoaiKhuyenMai(LoaiKhuyenMai loaiKhuyenMai) {
        this.loaiKhuyenMai = loaiKhuyenMai;
    }

    public void setGiaTri(double giaTri) {
        this.giaTri = giaTri;
    }

    public void setNgayBatDau(LocalDateTime ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public void setNgayKetThuc(LocalDateTime ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public void setDangKichHoat(boolean dangKichHoat) {
        this.dangKichHoat = dangKichHoat;
    }
}