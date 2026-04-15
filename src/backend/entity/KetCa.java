package backend.entity;

import java.time.LocalDateTime;

public class KetCa {
    private String idKetCa;
    private String idCaLv;
    private String idNhanVien;
    private LocalDateTime tgBatDau;
    private LocalDateTime tgKetThuc;
    private double tongDoanhThu;

    // ================ CONSTRUCTOR =================
    public KetCa() {}

    public KetCa(String idKetCa, String idCaLv, String idNhanVien, LocalDateTime tgBatDau) {
        this.idKetCa = idKetCa;
        this.idCaLv = idCaLv;
        this.idNhanVien = idNhanVien;
        this.tgBatDau = tgBatDau;
        this.tongDoanhThu = 0; // Mới mở ca thì doanh thu = 0
    }

    // ================ GETTER =================
    public String getIdKetCa() {
        return idKetCa;
    }

    public String getIdCaLv() {
        return idCaLv;
    }

    public String getIdNhanVien() {
        return idNhanVien;
    }

    public LocalDateTime getTgBatDau() {
        return tgBatDau;
    }

    public LocalDateTime getTgKetThuc() {
        return tgKetThuc;
    }

    public double getTongDoanhThu() {
        return tongDoanhThu;
    }


    // ================ SETTER =================
    public void setIdKetCa(String idKetCa) {
        this.idKetCa = idKetCa;
    }

    public void setIdCaLv(String idCaLv) {
        this.idCaLv = idCaLv;
    }

    public void setIdNhanVien(String idNhanVien) {
        this.idNhanVien = idNhanVien;
    }

    public void setTgBatDau(LocalDateTime tgBatDau) {
        this.tgBatDau = tgBatDau;
    }

    public void setTgKetThuc(LocalDateTime tgKetThuc) {
        this.tgKetThuc = tgKetThuc;
    }

    public void setTongDoanhThu(double tongDoanhThu) {
        this.tongDoanhThu = tongDoanhThu;
    }
}