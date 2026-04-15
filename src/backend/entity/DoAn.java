package backend.entity;

import backend.enums.TrangThaiMonAn;

public class DoAn extends MonAn {
    private String donViTinh; // VD: "Đĩa", "Bát", "Cái",...?

    // ================ CONSTRUCTOR =================
    public DoAn() { super(); }

    public DoAn(String idMon, String tenMon, double gia, String moTa,
                TrangThaiMonAn trangThai, String menuId, String donViTinh) {
        super(idMon, tenMon, gia, moTa, trangThai, menuId);
        this.donViTinh = donViTinh;
    }

    // ================ HÀM CHỨC NĂNG =================
    @Override
    public double giaBan() {
        return this.getGia();
    }

    // ================ GETTER - SETTER =================
    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }
}