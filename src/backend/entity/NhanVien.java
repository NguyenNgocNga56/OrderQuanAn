package backend.entity;

public class NhanVien extends ConNguoi {
    private double luong;
    private String chucVu;

    // Constructor rỗng
    public NhanVien() {
        super();
    }

    // ================ CONSTRUCTOR =================
    public NhanVien(String id, String hoTen, String sdt, String diaChi, String email, double luong, String chucVu) {
        super(id, hoTen, sdt, diaChi, email);
        this.luong = luong;
        this.chucVu = chucVu;
    }

    // ============== HÀM CHỨC NĂNG ===============
    @Override
    public void hienThiVaiTro() {
        System.out.println("VAI TRÒ: Nhân viên hệ thống");
        System.out.println("Chức vụ: " + this.chucVu);
    }

    // ================ GETTER =================

    public double getLuong() {
        return luong;
    }

    public String getChucVu() {
        return chucVu;
    }

    // ================ SETTER =================

    public void setLuong(double luong) {
        this.luong = luong;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }
}