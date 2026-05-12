package com.example.sbquanan.entity;

import com.example.sbquanan.enums.LoaiKhuyenMai;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "KhuyenMai")
@Data
@NoArgsConstructor
public class KhuyenMai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "KhuyenMaiID")
    private Integer khuyenMaiID;

    @Column(name = "TenKhuyenMai", length = 100)
    private String tenKhuyenMai;

    @Enumerated(EnumType.STRING)
    @Column(name = "LoaiKhuyenMai", length = 50)
    private LoaiKhuyenMai loaiKhuyenMai;

    @Column(name = "GiaTri")
    private double giaTri;

    // Trong SQL là kiểu DATE nên dùng LocalDate là chuẩn nhất
    @Column(name = "NgayBatDau")
    private LocalDate ngayBatDau;

    @Column(name = "NgayKetThuc")
    private LocalDate ngayKetThuc;

    // Cột này KHÔNG có trong SQL bạn gửi.
    // Mình thêm @Transient để bạn vẫn dùng được logic trong Java mà không bị lỗi DB.
    @Transient
    private boolean dangKichHoat = true;

    public boolean hopLe() {
        LocalDate now = LocalDate.now();
        // Kiểm tra xem hôm nay có nằm trong khoảng thời gian khuyến mãi không
        boolean trongThoiGian = (now.isEqual(ngayBatDau) || now.isAfter(ngayBatDau))
                && (now.isEqual(ngayKetThuc) || now.isBefore(ngayKetThuc));
        return dangKichHoat && trongThoiGian;
    }
}
