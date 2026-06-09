package com.example.sbquanan.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChiTietOrderRequest {

    @NotNull(message = "MonAnId không được để trống")
    private Long monAnId;

    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private int soLuong;

    private String ghiChu;
}