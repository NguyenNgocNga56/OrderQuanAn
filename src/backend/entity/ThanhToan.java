package backend.entity;

import backend.enums.PhuongThucThanhToan;
import backend.enums.TrangThaiThanhToan;
import java.time.LocalDateTime;

public class ThanhToan {
    private String ThanhToanID;
    private String HoaDonID;
    private PhuongThucThanhToan PhuongThuc;
    private double SoTien;
    private LocalDateTime ThoiGian;
    private TrangThaiThanhToan TrangThai;

    // ================ CONSTRUCTOR =================
    public ThanhToan() {}

    public ThanhToan(String ThanhToanID, String HoaDonID, PhuongThucThanhToan PhuongThuc, double SoTien, TrangThaiThanhToan TrangThai) {
        this.ThanhToanID = ThanhToanID;
        this.HoaDonID = HoaDonID;
        this.PhuongThuc = PhuongThuc;
        this.SoTien = SoTien;
        this.TrangThai = TrangThai;
        this.ThoiGian = LocalDateTime.now();
    }

    // ================ GETTER =================
    public String getIdThanhToan() {
        return ThanhToanID;
    }

    public String getIdHoaDon() {
        return HoaDonID;
    }

    public PhuongThucThanhToan getPhuongThuc() {
        return PhuongThuc;
    }

    public double getSoTien() {
        return SoTien;
    }

    public LocalDateTime getThoiGian() {
        return ThoiGian;
    }

    public TrangThaiThanhToan getTrangThai() {
        return TrangThai;
    }
    
    // ================ SETTER =================

	public void setThanhToanID(String ThanhToanID) {
		this.ThanhToanID = ThanhToanID;
	}

	public void setHoaDonID(String HoaDonID) {
		this.HoaDonID = HoaDonID;
	}

	public void setPhuongThuc(PhuongThucThanhToan PhuongThuc) {
		this.PhuongThuc = PhuongThuc;
	}

	public void setSoTien(double SoTien) {
		this.SoTien = SoTien;
	}

	public void setThoiGian(LocalDateTime ThoiGian) {
		this.ThoiGian = ThoiGian;
	}

	public void setTrangThai(TrangThaiThanhToan TrangThai) {
		this.TrangThai = TrangThai;
	}

}
