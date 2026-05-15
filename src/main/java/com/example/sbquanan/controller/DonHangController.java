package com.example.sbquanan.controller;

import com.example.sbquanan.dto.ApiResponse;
import com.example.sbquanan.entity.DonHang;
import com.example.sbquanan.service.DonHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donhang")
@CrossOrigin(origins = "*")
public class DonHangController {

    @Autowired private DonHangService service;

    @GetMapping
    public ApiResponse<List<DonHang>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DonHang>> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(dh -> ResponseEntity.ok(ApiResponse.success(dh)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ApiResponse<DonHang> create(@RequestBody DonHang donHang) {
        return ApiResponse.success(service.create(donHang), "Tạo đơn hàng thành công");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DonHang>> update(@PathVariable Long id, @RequestBody DonHang donHang) {
        try {
            return ResponseEntity.ok(ApiResponse.success(service.update(id, donHang), "Cập nhật thành công"));
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
