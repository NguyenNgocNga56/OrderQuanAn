package backend.entity;

import backend.enums.TrangThaiMonAn;

public abstract class MonAn {
    private String MonID;
    private String TenMon;
    private double Gia;
    private String MoTa;
    private String HinhAnh;
    private TrangThaiMonAn TrangThai;
    private String MenuId;

    // ================ CONSTRUCTOR =================
    public MonAn(){}

    
    public MonAn(String MonID, String TenMon, double Gia, String MoTa, String HinhAnh, TrangThaiMonAn TrangThai,
			String MenuId) {
		super();
		this.MonID = MonID;
		this.TenMon = TenMon;
		this.Gia = Gia;
		this.MoTa = MoTa;
		this.HinhAnh = HinhAnh;
		this.TrangThai = TrangThai;
		this.MenuId = MenuId;
	}

    // =============== HÀM CHỨC NĂNG ===============
    // Tính giá bán, k phải giá bàn đâu=))))
    public abstract double giaBan();


	// ================ GETTER =================
	
    public String getMonID() {
		return MonID;
	}
	public String getTenMon() {
		return TenMon;
	}

	public double getGia() {
		return Gia;
	}

	public String getMoTa() {
		return MoTa;
	}
	public String getHinhAnh() {
		return HinhAnh;
	}
	public TrangThaiMonAn getTrangThai() {
		return TrangThai;
	}
	public String getMenuId() {
		return MenuId;
	}

	// ================ GETTER ================= 
	
	public void setMonID(String monID) {
		MonID = monID;
	}
	public void setTenMon(String tenMon) {
		TenMon = tenMon;
	}
	public void setGia(double gia) {
		Gia = gia;
	}
	public void setMoTa(String moTa) {
		MoTa = moTa;
	}

	public void setHinhAnh(String hinhAnh) {
		HinhAnh = hinhAnh;
	}

	public void setTrangThai(TrangThaiMonAn trangThai) {
		TrangThai = trangThai;
	}

	public void setMenuId(String menuId) {
		MenuId = menuId;
	}

}
