package backend.entity;

import backend.enums.TrangThaiMonAn;

public abstract class MonAn {
    private String idMon;
    private String tenMon;
    private double gia;
    private String moTa;
    private TrangThaiMonAn trangThai;
    private String menuId;

    // ================ CONSTRUCTOR =================
    public MonAn(){}

    public MonAn(String idMon, String tenMon, double gia, String moTa, TrangThaiMonAn trangThai, String menuId) {
        this.idMon = idMon;
        this.tenMon = tenMon;
        this.gia = gia;
        this.moTa = moTa;
        this.trangThai = trangThai;
        this.menuId = menuId;
    }

    // =============== HÀM CHỨC NĂNG ===============
    // Tính giá bán, k phải giá bàn đâu=))))
    public abstract double giaBan();

    // ================ GETTER =================

    public String getIdMon() {
        return idMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public double getGia() {
        return gia;
    }

    public String getMoTa() {
        return moTa;
    }

    public TrangThaiMonAn getTrangThai() {
        return trangThai;
    }

    public String getMenuId() {
        return menuId;
    }

    // ================ SETTER =================

    public void setIdMon(String idMon) {
        this.idMon = idMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public void setTrangThai(TrangThaiMonAn trangThai) {
        this.trangThai = trangThai;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
