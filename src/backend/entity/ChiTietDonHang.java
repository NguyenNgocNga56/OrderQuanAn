package backend.entity;

public class ChiTietDonHang {
    private String idChiTiet;
    private String idDonHang;
    private MonAn monAn;
    private int soLuong;
    private double giaBan;    // Giá tại thời điểm bán (lấy từ monAn.giaBan())
    private double tongTien; // = soLuong * giaBan


    // ================ CONSTRUCTOR =================
    public ChiTietDonHang() {}

    public ChiTietDonHang(String idChiTiet, String idDonHang, MonAn monAn, int soLuong) {
        this.idChiTiet = idChiTiet;
        this.idDonHang = idDonHang;
        this.monAn = monAn;
        this.soLuong = soLuong;

        // Khi khởi tạo, lấy luôn giá bán từ đối tượng MonAn
        if (monAn != null) {
            this.giaBan = monAn.giaBan();
            this.tongTien = this.soLuong * this.giaBan;
        }
    }


    // ================ GETTER =================

    public String getIdChiTiet() {
        return idChiTiet;
    }

    public String getIdDonHang() {
        return idDonHang;
    }

    public MonAn getMonAn() {
        return monAn;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public double gettongTien() {
        return tongTien;
    }

    // ================ SETTER =================

    public void setIdChiTiet(String idChiTiet) {
        this.idChiTiet = idChiTiet;
    }

    public void setIdDonHang(String idDonHang) {
        this.idDonHang = idDonHang;
    }

    public void setMonAn(MonAn monAn) {
        this.monAn = monAn;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public void settongTien(double tongTien) {
        this.tongTien = tongTien;
    }
}