package com.example.sbquanan.service;

import com.example.sbquanan.dto.OrderRequest;
import com.example.sbquanan.dto.OrderResponse;
import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    List<OrderResponse> getAll();
    OrderResponse getById(Long id);
}