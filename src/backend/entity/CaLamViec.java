package backend.entity;

import java.time.LocalTime;

public class CaLamViec {
    private String CaID;
    private String TenCa;
    private LocalTime GioBatDau;
    private LocalTime GioKetThuc;

    // ================ CONSTRUCTOR =================
    public CaLamViec() {}

    public CaLamViec(String CaID, String TenCa, LocalTime GioBatDau, LocalTime GioKetThuc) {
        this.CaID = CaID;
        this.TenCa = TenCa;
        this.GioBatDau = GioBatDau;
        this.GioKetThuc = GioKetThuc;
    }

    // ================ GETTER =================
    
    public String getCaID() {
		return CaID;
	}

	public String getTenCa() {
		return TenCa;
	}

	public LocalTime getGioBatDau() {
		return GioBatDau;
	}

	public LocalTime getGioKetThuc() {
		return GioKetThuc;
	}
    // ================ SETTER =================

	public void setCaID(String caID) {
		CaID = caID;
	}

	public void setTenCa(String tenCa) {
		TenCa = tenCa;
	}

	public void setGioBatDau(LocalTime gioBatDau) {
		GioBatDau = gioBatDau;
	}

	public void setGioKetThuc(LocalTime gioKetThuc) {
		GioKetThuc = gioKetThuc;
	}

	@Override
	public String toString() {
		return "CaLamViec [CaID=" + CaID + ", TenCa=" + TenCa + ", GioBatDau=" + GioBatDau + ", GioKetThuc="
				+ GioKetThuc + "]";
	}

}
