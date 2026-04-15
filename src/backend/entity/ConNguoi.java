package backend.entity;

public abstract class ConNguoi {
    private String id;
    private String hoTen;
    private String sdt;
    private String diaChi;
    private String email;

    // ================ CONSTRUCTOR =================
    public ConNguoi(){}

    public ConNguoi(String id, String hoTen, String sdt, String diaChi, String email) {
        this.id = id;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.email = email;
    }

    // ================ HÀM CHỨC NĂNG =================
    public abstract void hienThiVaiTro();

    // ================ GET =================
    public String getId() {
        return id;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public String getEmail() {
        return email;
    }

    // ================ SET =================

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public void setId(String id) {
        this.id = id;
    }
}
