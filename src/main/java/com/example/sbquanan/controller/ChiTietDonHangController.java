package com.example.sbquanan.controller;

import com.example.sbquanan.dto.ApiResponse;
import com.example.sbquanan.entity.ChiTietDonHang;
import com.example.sbquanan.service.ChiTietDonHangService;
import com.example.sbquanan.service.impl.ChiTietDonHangServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chitietdonhang")
@CrossOrigin(origins = "*")
public class ChiTietDonHangController {

    @Autowired private ChiTietDonHangServiceImpl service;

    @GetMapping
    public ApiResponse<List<ChiTietDonHang>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChiTietDonHang>> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ct -> ResponseEntity.ok(ApiResponse.success(ct)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/donhang/{donHangId}")
    public ApiResponse<List<ChiTietDonHang>> getByDonHang(@PathVariable Long donHangId) {
        return ApiResponse.success(service.getByDonHang(donHangId));
    }

    @PostMapping
    public ApiResponse<ChiTietDonHang> create(@RequestBody ChiTietDonHang chiTiet) {
        return ApiResponse.success(service.create(chiTiet), "Thêm chi tiết đơn hàng thành công");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ChiTietDonHang>> update(@PathVariable Long id, @RequestBody ChiTietDonHang chiTiet) {
        try {
            return ResponseEntity.ok(ApiResponse.success(service.update(id, chiTiet), "Cập nhật thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok(ApiResponse.success(null, "Xóa thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
