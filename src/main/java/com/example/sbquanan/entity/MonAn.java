package com.example.sbquanan.entity;

import com.example.sbquanan.enums.TrangThaiMonAn;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MonAn")


@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PhanLoai", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
// Cho Jackson biết cách serialize các subclass
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "phanLoai", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DoAn.class,   name = "Đồ ăn"),
        @JsonSubTypes.Type(value = DoUong.class, name = "Đồ uống")
})
// Bỏ qua các field không cần thiết khi serialize
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public abstract class MonAn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MonID")
    private Long monID;

    @Column(name = "TenMon", nullable = false, length = 100)
    private String tenMon;

    @Column(name = "Gia")
    private double gia;

    @Column(name = "MoTa", length = 255)
    private String moTa;

    @Column(name = "HinhAnh", length = 255)
    private String hinhAnh;

    @Enumerated(EnumType.STRING)
    @Column(name = "TrangThai", length = 50)
    private TrangThaiMonAn trangThai;

    // @JsonIgnore để Jackson không cố load lazy relation này → tránh LazyInitializationException
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MenuID", nullable = false)
    private Menu menu;

    public abstract double giaBan();
}