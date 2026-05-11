package com.example.sbquanan.DTO;

import lombok.Data;
import java.util.List;

/**
 * DTO dùng cho POST /orders
 * Nhận thông tin đơn hàng + danh sách món từ giỏ hàng phía frontend.
 */
@Data
public class OrderRequest {

    private Integer khachHangId;   // nullable – khách vãng lai
    private Integer nhanVienId;    // nullable

    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        private Integer monId;   // MonAn.monID
        private int soLuong;
    }
}
