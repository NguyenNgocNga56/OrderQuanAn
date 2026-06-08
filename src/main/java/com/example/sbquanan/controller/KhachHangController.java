package com.example.sbquanan.controller;

import com.example.sbquanan.dto.ApiResponse;
import com.example.sbquanan.entity.KhachHang;
import com.example.sbquanan.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/khachhang")
@CrossOrigin(origins = "*")
public class KhachHangController {

    @Autowired private KhachHangService service;

    @GetMapping
    public ApiResponse<List<KhachHang>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<KhachHang>> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(kh -> ResponseEntity.ok(ApiResponse.success(kh)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sdt/{sdt}")
    public ResponseEntity<ApiResponse<KhachHang>> getBySdt(@PathVariable String sdt) {
        return service.getBySdt(sdt)
                .map(kh -> ResponseEntity.ok(ApiResponse.success(kh)))
                .orElse(ResponseEntity.notFound().build());
    }

    /** Khách tự đăng ký thành viên */
    @PostMapping("/dang-ky")
    public ApiResponse<KhachHang> dangKy(@RequestBody KhachHang khachHang) {
        return ApiResponse.success(service.create(khachHang), "Đăng ký thành công");
    }

    /** Admin thêm thủ công */
    @PostMapping
    public ApiResponse<KhachHang> create(@RequestBody KhachHang khachHang) {
        return ApiResponse.success(service.create(khachHang), "Tạo khách hàng thành công");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<KhachHang>> update(
            @PathVariable Long id, @RequestBody KhachHang khachHang) {
        try {
            return ResponseEntity.ok(ApiResponse.success(
                    service.update(id, khachHang), "Cập nhật thành công"));
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