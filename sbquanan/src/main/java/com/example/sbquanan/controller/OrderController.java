package com.example.sbquanan.controller;

import com.example.sbquanan.DTO.OrderRequest;
import com.example.sbquanan.DTO.OrderResponse;
import com.example.sbquanan.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller xử lý API:
 *   POST   /orders        – tạo đơn hàng mới
 *   GET    /orders        – danh sách tất cả đơn
 *   GET    /orders/{id}   – chi tiết một đơn
 */
@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")   // cho phép gọi từ file HTML tĩnh
public class OrderController {

    @Autowired
    private OrderService orderService;

    /** Tạo đơn hàng mới từ giỏ hàng */
    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest req) {
        OrderResponse res = orderService.createOrder(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    /** Danh sách tất cả đơn hàng (kèm chi tiết món) */
    @GetMapping
    public List<OrderResponse> getAll() {
        return orderService.getAll();
    }

    /** Chi tiết một đơn hàng theo ID */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getById(id));
    }
}
