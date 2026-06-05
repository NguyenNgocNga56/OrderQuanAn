package com.example.sbquanan.dto;

import com.example.sbquanan.enums.PhuongThucThanhToan;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ThanhToanRequest {

    @NotNull(message = "Phương thức thanh toán không được để trống")
    private PhuongThucThanhToan phuongThuc;

    private Double soTien;

    private String maKhuyenMai;
}