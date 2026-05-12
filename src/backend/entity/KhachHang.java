package backend.entity;

import backend.enums.LoaiKhachHang;

public class KhachHang extends ConNguoi {
    private int DiemTichLuy;
    private LoaiKhachHang LoaiKhachHang;

    public KhachHang() {
        super();
        this.LoaiKhachHang = LoaiKhachHang.DONG; //Mặc định là khách Đồng
    }

    public KhachHang(String ID, String HoTen, String Sdt, String DiaChi, String Email, int DiemTichLuy) {
        super(ID, HoTen, Sdt, DiaChi, Email);
        this.DiemTichLuy = DiemTichLuy;
        capNhatHangKhachHang(); // Tự tính khi KT
    }

    // Auto nhảy hạng dựa trên điểm
    public void capNhatHangKhachHang() {
        if(DiemTichLuy >= 1000) {
            this.LoaiKhachHang = LoaiKhachHang.KIM_CUONG;
        } else if(DiemTichLuy >= 500){
            this.LoaiKhachHang = LoaiKhachHang.VANG;
        } else if(DiemTichLuy >= 100){
            this.LoaiKhachHang = LoaiKhachHang.BAC;
        } else{
            this.LoaiKhachHang = LoaiKhachHang.DONG;
        }
    }

    // ================ HÀM CHỨC NĂNG =================
    @Override
    public void hienThiVaiTro() {
        System.out.println("Khách hàng hạng: " + LoaiKhachHang.getTenHienThi());
    }

    // ================ GETTER - SETTER =================

    public int getDiemTichLuy() {
        return DiemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.DiemTichLuy = diemTichLuy;
    }

    public LoaiKhachHang getLoaiKhachHang() {
        return LoaiKhachHang;
    }

    public void setLoaiKhachHang(LoaiKhachHang LoaiKhachHang) {
        this.LoaiKhachHang = LoaiKhachHang;
    }
}
