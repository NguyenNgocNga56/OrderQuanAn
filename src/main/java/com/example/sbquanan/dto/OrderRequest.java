package com.example.sbquanan.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
 
import java.util.List;
 
@Data
public class OrderRequest {
    private Integer banId;
    private Integer khachHangId;    // nullable – khách vãng lai
    private Integer nhanVienId;     // nullable
    private String sdtKhachHang;    // nullable - dùng cho giỏ hàng khách
    private Long khuyenMaiId;       // nullable
    private String maKhuyenMai;     // nullable
 
    @Valid
    @NotEmpty(message = "Giỏ hàng trống, phải có ít nhất 1 món")
    private List<OrderItemRequest> items;
 
    @Data
    public static class OrderItemRequest {
        @NotNull(message = "ID món ăn không được để trống")
        private Integer monId;
 
        @Min(value = 1, message = "Số lượng phải lớn hơn 0")
        private int soLuong;
    }
}
