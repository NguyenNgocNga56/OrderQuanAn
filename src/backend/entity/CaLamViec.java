package backend.entity;

import java.time.LocalTime;

public class CaLamViec {
    private String idCaLv;
    private String tenCa;
    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;

    // ================ CONSTRUCTOR =================
    public CaLamViec() {}

    public CaLamViec(String idCaLv, String tenCa, LocalTime gioBatDau, LocalTime gioKetThuc) {
        this.idCaLv = idCaLv;
        this.tenCa = tenCa;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
    }

    // ================ GETTER =================
    public String getIdCaLv() {
        return idCaLv;
    }

    public String getTenCa() {
        return tenCa;
    }

    public LocalTime getGioBatDau() {
        return gioBatDau;
    }

    public LocalTime getGioKetThuc() {
        return gioKetThuc;
    }

    // ================ SETTER =================

    public void setIdCaLv(String idCaLv) {
        this.idCaLv = idCaLv;
    }

    public void setTenCa(String tenCa) {
        this.tenCa = tenCa;
    }

    public void setGioBatDau(LocalTime gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public void setGioKetThuc(LocalTime gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }
}