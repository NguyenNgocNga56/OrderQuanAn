package com.example.sbquanan.entity;

import com.example.sbquanan.enums.TrangThaiMenu;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Menu")
@Data
@NoArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MenuID")
    private Integer menuID;

    @Column(name = "TenMenu", nullable = false, unique = true, length = 50)
    private String tenMenu;

    @Column(name = "MoTa", length = 255)
    private String moTa;

    @Enumerated(EnumType.STRING)
    @Column(name = "TrangThai", length = 50)
    private TrangThaiMenu trangThai;

    @Column(name = "NgayTao")
    private LocalDate ngayTao;

    // Bỏ final để Hibernate có thể khởi tạo Proxy cho Lazy Loading
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @ToString.Exclude
    private List<MonAn> danhSachMonAn = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (ngayTao == null) {
            ngayTao = LocalDate.now();
        }
    }
}
