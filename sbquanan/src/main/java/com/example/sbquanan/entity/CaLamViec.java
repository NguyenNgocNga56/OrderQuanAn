package com.example.sbquanan.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Entity
@Table(name = "CaLamViec")
@Data
@NoArgsConstructor
public class CaLamViec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CaID")
    private Integer caID;

    @Column(name = "TenCa", length = 50)
    private String tenCa;

    @Column(name = "GioBatDau")
    private LocalTime gioBatDau;

    @Column(name = "GioKetThuc")
    private LocalTime gioKetThuc;
}
