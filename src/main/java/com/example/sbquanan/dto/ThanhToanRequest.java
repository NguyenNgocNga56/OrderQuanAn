package com.example.sbquanan.dto;

import com.example.sbquanan.enums.PhuongThucThanhToan;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
 
@Data
public class ThanhToanRequest {
    @NotNull(message = "Vui lòng chọn phương thức thanh toán")
    private PhuongThucThanhToan phuongThuc;
 
    @Positive(message = "Số tiền phải lớn hơn 0")
    private Double soTien;
}