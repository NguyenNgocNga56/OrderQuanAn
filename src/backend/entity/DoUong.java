package backend.entity;

import backend.enums.SizeDoUong;
import backend.enums.TrangThaiMonAn;

public class DoUong extends MonAn {
    private SizeDoUong size;

    // ================ CONSTRUCTOR =================
    public DoUong() { super(); }

    public DoUong(String idMon, String tenMon, double gia, String moTa,
                  TrangThaiMonAn trangThai, String menuId, SizeDoUong size) {
        super(idMon, tenMon, gia, moTa, trangThai, menuId);
        this.size = size;
    }

    // ================ HÀM CHỨC NĂNG =================
    @Override
    public double giaBan() {
        // Giá bán = Giá gốc + Phụ phí theo Size
        return this.getGia() + size.getPhuPhi();
    }

    // ================ GETTER =================

    public SizeDoUong getSize() {
        return size;
    }

    // ================ SETTER =================

    public void setSize(SizeDoUong size) {
        this.size = size;
    }
}