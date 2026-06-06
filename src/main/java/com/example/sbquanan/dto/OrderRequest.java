package com.example.sbquanan.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {

    @NotNull(message = "BanId không được để trống")
    private Integer banId;
    private Integer khachHangId;   // nullable – khách vãng lai
    private Integer nhanVienId;    // nullable
    private String sdtKhachHang;   // nullable - dung cho gio hang khach
    private Long khuyenMaiId;      // nullable - UI moi chon tu danh sach khuyen mai
    private String maKhuyenMai;    // nullable - tuong thich neu FE gui ten/ma khuyen mai

    @Pattern(regexp = "^0[0-9]{9}$", message = "Số điện thoại không hợp lệ (VD: 0912345678)")
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
