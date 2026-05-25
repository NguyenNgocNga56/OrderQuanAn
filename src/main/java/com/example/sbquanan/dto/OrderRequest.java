package com.example.sbquanan.dto;

import lombok.Data;
import java.util.List;

/**
 * DTO dùng cho POST /orders
 * Nhận thông tin đơn hàng + danh sách món từ giỏ hàng phía frontend.
 */
@Data
public class OrderRequest {
    private Integer banId;
    private Integer khachHangId;   // nullable – khách vãng lai
    private Integer nhanVienId;    // nullable
    private String sdtKhachHang;   // nullable - dung cho gio hang khach
    private String maKhuyenMai;    // nullable - tam thoi chua ap dung giam gia

    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        private Integer monId;   // MonAn.monID
        private int soLuong;
    }
}
