package com.example.sbquanan.dto;

import com.example.sbquanan.enums.PhuongThucThanhToan;
import lombok.Data;

@Data
public class ThanhToanRequest {
    private PhuongThucThanhToan phuongThuc;
    private Double soTien;
}
