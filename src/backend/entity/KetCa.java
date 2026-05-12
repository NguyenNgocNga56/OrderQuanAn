package backend.entity;

import java.time.LocalDateTime;

public class KetCa {
    private String KetCaID;
    private String CaID;
    private String NhanVienID;
    private LocalDateTime tgBatDau;
    private LocalDateTime tgKetThuc;
    private double TongDoanhThu;

    // ================ CONSTRUCTOR =================
    public KetCa() {}

    public KetCa(String idKetCa, String CaID, String NhanVienID, LocalDateTime tgBatDau) {
        this.KetCaID = KetCaID;
        this.CaID = CaID;
        this.NhanVienID = NhanVienID;
        this.tgBatDau = tgBatDau;
        this.TongDoanhThu = 0; // Mới mở ca thì doanh thu = 0
    }

    // ================ GETTER =================
    
    public String getKetCaID() {
		return KetCaID;
	}

	public String getCaID() {
		return CaID;
	}

	public String getNhanVienID() {
		return NhanVienID;
	}

	public LocalDateTime getTgBatDau() {
		return tgBatDau;
	}

	public LocalDateTime getTgKetThuc() {
		return tgKetThuc;
	}

	public double getTongDoanhThu() {
		return TongDoanhThu;
	}
    // ================ SETTER =================

	public void setKetCaID(String KetCaID) {
		this.KetCaID = KetCaID;
	}

	public void setCaID(String CaID) {
		this.CaID = CaID;
	}

	public void setNhanVienID(String nhanVienID) {
		NhanVienID = nhanVienID;
	}

	public void setTgBatDau(LocalDateTime tgBatDau) {
		this.tgBatDau = tgBatDau;
	}

	public void setTgKetThuc(LocalDateTime tgKetThuc) {
		this.tgKetThuc = tgKetThuc;
	}

	public void setTongDoanhThu(double tongDoanhThu) {
		TongDoanhThu = tongDoanhThu;
	}  
    
}
