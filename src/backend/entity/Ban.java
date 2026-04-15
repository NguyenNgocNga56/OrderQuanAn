package backend.entity;

import backend.enums.TrangThaiBan;

public class Ban {
    private String idBan;
    private int soBan;
    private TrangThaiBan trangThai;

    // ================ CONSTRUCTOR =================
    public Ban(){}

    public Ban(String idBan, int soBan, TrangThaiBan trangThai) {
        this.idBan = idBan;
        this.soBan = soBan;
        this.trangThai = trangThai;
    }

    // ================ GETTER =================

    public String getIdBan() {
        return idBan;
    }

    public int getSoBan() {
        return soBan;
    }

    public TrangThaiBan getTrangThai() {
        return trangThai;
    }

    // ================ SETTER =================

    public void setIdBan(String idBan) {
        this.idBan = idBan;
    }

    public void setSoBan(int soBan) {
        this.soBan = soBan;
    }

    public void setTrangThai(TrangThaiBan trangThai) {
        this.trangThai = trangThai;
    }
}
