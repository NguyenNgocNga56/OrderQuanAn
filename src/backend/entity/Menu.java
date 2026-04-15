package backend.entity;

import backend.enums.TrangThaiMenu;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    private String idMenu;
    private String tenMenu;
    private String moTa;
    private TrangThaiMenu trangThai;
    private LocalDateTime ngayTao;

    private List<MonAn> danhSachMonAn = new ArrayList<>();

    // ================ CONSTRUCTOR =================
    public Menu() {}

    public Menu(String idMenu, String tenMenu, String moTa, TrangThaiMenu trangThai) {
        this.idMenu = idMenu;
        this.tenMenu = tenMenu;
        this.moTa = moTa;
        this.trangThai = trangThai;
        this.ngayTao = LocalDateTime.now();
    }

    // ================ GETTER =================
    public String getIdMenu() {
        return idMenu;
    }

    public String getTenMenu() {
        return tenMenu;
    }

    public String getMoTa() {
        return moTa;
    }

    public TrangThaiMenu getTrangThai() {
        return trangThai;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public List<MonAn> getDanhSachMonAn() {
        return danhSachMonAn;
    }

    // ================ SETTER =================
    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu;
    }

    public void setTenMenu(String tenMenu) {
        this.tenMenu = tenMenu;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public void setTrangThai(TrangThaiMenu trangThai) {
        this.trangThai = trangThai;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public void setDanhSachMonAn(List<MonAn> danhSachMonAn) {
        this.danhSachMonAn = danhSachMonAn;
    }
}