package backend.entity;

import backend.enums.LoaiKhuyenMai;

import java.time.LocalDateTime;

public class KhuyenMai {
    private String KhuyenMaiID;
    private String TenKhuyenMai;
    private LoaiKhuyenMai LoaiKhuyenMai;  //tạm thời để string nhé, chứ t tính dùng enum :Đ
    private double GiaTri;
    private LocalDateTime NgayBatDau;
    private LocalDateTime NgayKetThuc;
    private boolean DangKichHoat;

    // ================ CONSTRUCTOR =================
    public KhuyenMai() {}

    public KhuyenMai(String KhuyenMaiID, String TenKhuyenMai, LoaiKhuyenMai LoaiKhuyenMai, double GiaTri, LocalDateTime NgayBatDau, LocalDateTime NgayKetThuc, boolean DangKichHoat) {
        this.KhuyenMaiID = KhuyenMaiID;
        this.TenKhuyenMai = TenKhuyenMai;
        this.LoaiKhuyenMai = LoaiKhuyenMai;
        this.GiaTri = GiaTri;
        this.NgayBatDau = NgayBatDau;
        this.NgayKetThuc = NgayKetThuc;
        this.DangKichHoat = DangKichHoat;
    }

    // ================ HÀM CHỨC NĂNG =================
    // Check xem mã còn hạn dùng k
    public boolean conHieuLuc() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(NgayBatDau) && now.isBefore(NgayKetThuc);
    }
    public boolean hopLe() {
        LocalDateTime now = LocalDateTime.now();
        boolean trongThoiHan = now.isAfter(NgayBatDau) && now.isBefore(NgayKetThuc);
        return DangKichHoat && trongThoiHan;
    }

    

    // ================ GETTER =================
    
    public String getKhuyenMaiID() {
		return KhuyenMaiID;
	}

	public String getTenKhuyenMai() {
		return TenKhuyenMai;
	}

	public LoaiKhuyenMai getLoaiKhuyenMai() {
		return LoaiKhuyenMai;
	}

	public double getGiaTri() {
		return GiaTri;
	}

	public LocalDateTime getNgayBatDau() {
		return NgayBatDau;
	}

	public LocalDateTime getNgayKetThuc() {
		return NgayKetThuc;
	}

	public boolean isDangKichHoat() {
		return DangKichHoat;
	}

	// ================ SETTER =================
	
	public void setKhuyenMaiID(String khuyenMaiID) {
		KhuyenMaiID = khuyenMaiID;
	}

	public void setTenKhuyenMai(String tenKhuyenMai) {
		TenKhuyenMai = tenKhuyenMai;
	}

	public void setLoaiKhuyenMai(LoaiKhuyenMai loaiKhuyenMai) {
		LoaiKhuyenMai = loaiKhuyenMai;
	}

	public void setGiaTri(double giaTri) {
		GiaTri = giaTri;
	}

	public void setNgayBatDau(LocalDateTime ngayBatDau) {
		NgayBatDau = ngayBatDau;
	}

	public void setNgayKetThuc(LocalDateTime ngayKetThuc) {
		NgayKetThuc = ngayKetThuc;
	}

	public void setDangKichHoat(boolean dangKichHoat) {
		DangKichHoat = dangKichHoat;
	}

}	
