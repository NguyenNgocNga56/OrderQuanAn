package test;

import backend.entity.*;
import backend.enums.*;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- TEST ---");
        MonAn traSua = new DoUong(
                "DU01",
                "Trà Sữa Matcha",
                30000,
                "Siêu matchamatme",
                TrangThaiMonAn.CON_HANG,
                "MENU_01",
                SizeDoUong.L
        );

        System.out.println("Tên món: " + traSua.getTenMon());
        System.out.println("Giá niêm yết: " + traSua.getGia() + " VNĐ");

        System.out.println("Giá bán thực tế (đã cộng Size L): " + traSua.giaBan() + " VNĐ");

        if (traSua.giaBan() == 40000) {
            System.out.println("\n=> ok");
        } else {
            System.out.println("\n=> sai");
        }
    }
}