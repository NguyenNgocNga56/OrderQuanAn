package com.example.sbquanan.controller;

import com.example.sbquanan.dto.ApiResponse;
import com.example.sbquanan.entity.ThanhToan;
import com.example.sbquanan.service.ThanhToanService;
import com.example.sbquanan.service.ThanhToanService;
import com.example.sbquanan.repository.ThanhToanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/thanhtoan")
@CrossOrigin(origins = "*")
public class ThanhToanController {
    @Autowired private ThanhToanRepository repository;

    @Autowired private ThanhToanService service;

    @GetMapping
    public ApiResponse<List<ThanhToan>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ThanhToan>> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(tt -> ResponseEntity.ok(ApiResponse.success(tt)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ApiResponse<ThanhToan> create(@RequestBody ThanhToan thanhToan) {
        return ApiResponse.success(service.create(thanhToan), "Tạo thanh toán thành công");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ThanhToan>> update(@PathVariable Long id, @RequestBody ThanhToan thanhToan) {
        try {
            return ResponseEntity.ok(ApiResponse.success(service.update(id, thanhToan), "Cập nhật thành công"));
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
