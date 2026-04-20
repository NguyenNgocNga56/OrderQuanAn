package backend.entity;

import backend.enums.SizeDoUong;
import backend.enums.TrangThaiMonAn;
import backend.enums.*;
public class DoUong extends MonAn {
    private SizeDoUong Size;

    // ================ CONSTRUCTOR =================
    public DoUong() { super(); }

    
    public DoUong(SizeDoUong Size) {
		super();
		this.Size = Size;
	}
    
	public DoUong(String MonID, String TenMon, double Gia, String MoTa,
			TrangThaiMonAn TrangThai, String MenuID, SizeDoUong Size) {
		super(MonID, TenMon, Gia, MoTa, MenuID, TrangThai, MenuID );
		this.Size = Size;
        
    }

    // ================ HÀM CHỨC NĂNG =================
    @Override
    public double giaBan() {
        // Giá bán = Giá gốc + Phụ phí theo Size
        return this.getGia() + Size.getPhuPhi();
    }

    // ================ GETTER =================

    public SizeDoUong getSize() {
        return Size;
    }

    // ================ SETTER =================

    public void setSize(SizeDoUong size) {
        this.Size = size;
    }
}
