package com.example.sbquanan.entity;

import com.example.sbquanan.enums.TrangThaiBan;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Ban")
@Data
@NoArgsConstructor
public class Ban {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BanID")
    private Long banID;

    @Column(name = "SoBan", nullable = false)
    private int soBan;

    @Enumerated(EnumType.STRING)
    @Column(name = "TrangThai", length = 20)
    private TrangThaiBan trangThai = TrangThaiBan.TRONG;
}
