package backend.entity;

import backend.enums.TrangThaiDonHang;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class DonHang {
    private String DonHangID;
    private TrangThaiDonHang TrangThai;
    private double TongTien;
    private LocalDateTime NgayDat;
    private String KhachHangID;
    private String NhanVienID;

    private Map<String, ChiTietDonHang> items = new LinkedHashMap<>(); // Lưu các chi tiết đơn hàng

    // ================ CONSTRUCTOR =================
    public DonHang(){}

    public DonHang(String DonHangID, TrangThaiDonHang TrangThai, double TongTien, LocalDateTime NgayDat,
			String KhachHangID, String NhanVienID) {
		super();
		this.DonHangID = DonHangID;
		this.TrangThai = TrangThai;
		this.TongTien = TongTien;
		this.NgayDat = NgayDat;
		this.KhachHangID = KhachHangID;
		this.NhanVienID =NhanVienID;
	}

	// ================ HÀM CHỨC NĂNG =================
    // Auto update tổng tiền
    public void tuDongCapNhatTongTien() {
        double temp = 0;
        for (ChiTietDonHang ct : items.values()) {
            temp += ct.getTongTien(); // Lấy tổng tiền từng dòng cộng lại
        }
        this.TongTien = temp;
    }

    // ================ GETTER =================

    public String getIdDonHang() {
        return DonHangID;
    }
    public TrangThaiDonHang getTrangThai() {
        return TrangThai;
    }
    public double getTongTien() {
        return TongTien;
    }
    public LocalDateTime getNgayDat() {
        return NgayDat;
    }
    public String getIdKhachHang() {
        return KhachHangID;
    }
    public String getIdNhanVien() {
        return NhanVienID;
    }

    // ================ SETTER =================
    
	public void setDonHangID(String donHangID) {
		DonHangID = donHangID;
	}
	public void setTrangThai(TrangThaiDonHang trangThai) {
		TrangThai = trangThai;
	}
	public void setTongTien(double tongTien) {
		TongTien = tongTien;
	}
	public void setNgayDat(LocalDateTime ngayDat) {
		NgayDat = ngayDat;
	}
	public void setNhanVienID(String nhanVienID) {
		NhanVienID = nhanVienID;
	}
	public void setKhachHangID(String khachHangID) {
		KhachHangID = khachHangID;
	}

    
}
