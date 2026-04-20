package backend.entity;

public class NhanVien extends ConNguoi {
    private double Luong;
    private String ChucVu;
    private boolean TrangThai;
    // Constructor rỗng
    public NhanVien() {
        super();
    }

    // ================ CONSTRUCTOR =================
    public NhanVien(String ID, String HoTen, String Sdt, String DiaChi, String Email, double Luong, String ChucVu, boolean TrangThai) {
        super(ID, HoTen, Sdt, DiaChi, Email);
        this.Luong = Luong;
        this.ChucVu = ChucVu;
        this.TrangThai = TrangThai;
    }

    // ============== HÀM CHỨC NĂNG ===============
    @Override
    public void hienThiVaiTro() {
        System.out.println("VAI TRÒ: Nhân viên hệ thống");
        System.out.println("Chức vụ: " + this.ChucVu);
        System.out.println("Trạng thái làm việc: " + this.TrangThai);
    }

    // ================ GETTER =================

    public double getLuong() {
        return Luong;
    }

    public String getChucVu() {
        return ChucVu;
    }

    public boolean isTrangThai() {
		return TrangThai;
	}
    // ================ SETTER =================

	public void setLuong(double Luong) {
        this.Luong = Luong;
    }

    public void setChucVu(String ChucVu) {
        this.ChucVu = ChucVu;
    }

	public void setTrangThai(boolean trangThai) {
		TrangThai = trangThai;
	}
    
}
