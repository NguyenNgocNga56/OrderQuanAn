package backend.entity;

public class ChiTietDonHang {
    private String CTDHID;
    private String DonHangID;
    private MonAn monAn;
    private int SoLuong;
    private double giaBan;    // Giá tại thời điểm bán (lấy từ monAn.giaBan())
    private double tongTien; // = soLuong * giaBan


    // ================ CONSTRUCTOR =================
    public ChiTietDonHang() {}

    public ChiTietDonHang(String CTDHID, String DonHangID, MonAn monAn, int SoLuong) {
        this.CTDHID = CTDHID;
        this.DonHangID = DonHangID;
        this.monAn = monAn;
        this.SoLuong = SoLuong;

        // Khi khởi tạo, lấy luôn giá bán từ đối tượng MonAn
        if (monAn != null) {
            this.giaBan = monAn.giaBan();
            this.tongTien = this.SoLuong * this.giaBan;
        }
    }


    // ================ GETTER =================

    public String getCTDHID() {
		return CTDHID;
	}

	public String getDonHangID() {
		return DonHangID;
	}

	public MonAn getMonAn() {
		return monAn;
	}

	public int getSoLuong() {
		return SoLuong;
	}

	public double getGiaBan() {
		return giaBan;
	}

	public double getTongTien() {
		return tongTien;
	}

	// ================ SETTER =================
	
	public void setCTDHID(String CTDHID) {
		this.CTDHID = CTDHID;
	}

	public void setDonHangID(String DonHangID) {
		this.DonHangID = DonHangID;
	}

	public void setMonAn(MonAn monAn) {
		this.monAn = monAn;
	}

	public void setSoLuong(int SoLuong) {
		this.SoLuong = SoLuong;
	}

	public void setGiaBan(double giaBan) {
		this.giaBan = giaBan;
	}

	public void setTongTien(double tongTien) {
		this.tongTien = tongTien;
	}
}
