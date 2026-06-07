package com.example.sbquanan.controller;

import com.example.sbquanan.dto.ApiResponse;
import com.example.sbquanan.entity.KhuyenMai;
import com.example.sbquanan.service.KhuyenMaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/khuyenmai")
@CrossOrigin(origins = "*")
public class KhuyenMaiController {

    @Autowired private KhuyenMaiService service;

    @GetMapping
    public ApiResponse<List<KhuyenMai>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    @GetMapping("/kha-dung")
    public ApiResponse<List<KhuyenMai>> getKhaDung(
            @RequestParam(required = false) String sdt,
            @RequestParam(defaultValue = "0") double tongTien) {
        return ApiResponse.success(service.getKhaDung(sdt, tongTien));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<KhuyenMai>> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(km -> ResponseEntity.ok(ApiResponse.success(km)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ApiResponse<KhuyenMai> create(@RequestBody KhuyenMai khuyenMai) {
        return ApiResponse.success(service.create(khuyenMai), "Tạo khuyến mãi thành công");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<KhuyenMai>> update(@PathVariable Long id, @RequestBody KhuyenMai khuyenMai) {
        try {
            return ResponseEntity.ok(ApiResponse.success(service.update(id, khuyenMai), "Cập nhật thành công"));
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
