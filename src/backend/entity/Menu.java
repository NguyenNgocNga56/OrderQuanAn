package backend.entity;

import backend.enums.TrangThaiMenu;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    private String MenuID;
    private String TenMenu;
    private String MoTa;
    private TrangThaiMenu TrangThai;
    private LocalDateTime NgayTao;

    private List<MonAn> danhSachMonAn = new ArrayList<>();

    // ================ CONSTRUCTOR =================
    public Menu() {}

    public Menu(String MenuID, String TenMenu, String MoTa, TrangThaiMenu TrangThai) {
        this.MenuID = MenuID;
        this.TenMenu = TenMenu;
        this.MoTa = MoTa;
        this.TrangThai = TrangThai;
        this.NgayTao = LocalDateTime.now();
    }

    // ================ GETTER =================
    public String getIdMenu() {
        return MenuID;
    }

    public String getTenMenu() {
        return TenMenu;
    }

    public String getMoTa() {
        return MoTa;
    }

    public TrangThaiMenu getTrangThai() {
        return TrangThai;
    }

    public LocalDateTime getNgayTao() {
        return NgayTao;
    }

    public List<MonAn> getDanhSachMonAn() {
        return danhSachMonAn;
    }

    // ================ SETTER =================
    public void setIdMenu(String MenuID) {
        this.MenuID = MenuID;
    }

    public void setTenMenu(String TenMenu) {
        this.TenMenu = TenMenu;
    }

    public void setMoTa(String moTa) {
        this.MoTa = MoTa;
    }

    public void setTrangThai(TrangThaiMenu trangThai) {
        this.TrangThai = TrangThai;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.NgayTao = NgayTao;
    }

    public void setDanhSachMonAn(List<MonAn> danhSachMonAn) {
        this.danhSachMonAn = danhSachMonAn;
    }
}
