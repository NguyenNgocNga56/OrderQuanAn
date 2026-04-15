package backend.entity;

import backend.enums.LoaiKhachHang;

public class KhachHang extends ConNguoi {
    private int diemTichLuy;
    private LoaiKhachHang loaiKhachHang;

    public KhachHang() {
        super();
        this.loaiKhachHang = LoaiKhachHang.DONG; //Mặc định là khách Đồng
    }

    public KhachHang(String id, String hoTen, String sdt, String diaChi, String email, int diemTichLuy) {
        super(id, hoTen, sdt, diaChi, email);
        this.diemTichLuy = diemTichLuy;
        capNhatHangKhachHang(); // Tự tính khi KT
    }

    // Auto nhảy hạng dựa trên điểm
    public void capNhatHangKhachHang() {
        if(diemTichLuy >= 1000) {
            this.loaiKhachHang = LoaiKhachHang.KIM_CUONG;
        } else if(diemTichLuy >= 500){
            this.loaiKhachHang = LoaiKhachHang.VANG;
        } else if(diemTichLuy >= 100){
            this.loaiKhachHang = LoaiKhachHang.BAC;
        } else{
            this.loaiKhachHang = LoaiKhachHang.DONG;
        }
    }

    // ================ HÀM CHỨC NĂNG =================
    @Override
    public void hienThiVaiTro() {
        System.out.println("Khách hàng hạng: " + loaiKhachHang.getTenHienThi());
    }

    // ================ GETTER - SETTER =================

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    public LoaiKhachHang getLoaiKhachHang() {
        return loaiKhachHang;
    }

    public void setLoaiKhachHang(LoaiKhachHang loaiKhachHang) {
        this.loaiKhachHang = loaiKhachHang;
    }
}