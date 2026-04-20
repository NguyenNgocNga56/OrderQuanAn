package backend.entity;

public abstract class ConNguoi {
    private String ID;
    private String HoTen;
    private String Sdt;
    private String DiaChi;
    private String Email;

    // ================ CONSTRUCTOR =================
    public ConNguoi(){}

    public ConNguoi(String ID, String HoTen, String Sdt, String DiaChi, String Email) {
        this.ID = ID;
        this.HoTen = HoTen;
        this.Sdt = Sdt;
        this.DiaChi = DiaChi;
        this.Email = Email;
    }

    // ================ HÀM CHỨC NĂNG =================
    public abstract void hienThiVaiTro();

    // ================ GET =================
    
	public String getID() {
		return ID;
	}
	public String getHoTen() {
		return HoTen;
	}
	public String getSdt() {
		return Sdt;
	}
	public String getDiaChi() {
		return DiaChi;
	}
	public String getEmail() {
		return Email;
	}

	// ================ SET =================
	
	public void setID(String iD) {
		ID = iD;
	}
	public void setHoTen(String hoTen) {
		HoTen = hoTen;
	}
	public void setSdt(String sdt) {
		Sdt = sdt;
	}
	public void setDiaChi(String diaChi) {
		DiaChi = diaChi;
	}
	public void setEmail(String email) {
		Email = email;
	}
    
}
