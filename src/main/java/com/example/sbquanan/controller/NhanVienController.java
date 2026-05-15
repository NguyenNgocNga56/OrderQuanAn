package com.example.sbquanan.controller;

import com.example.sbquanan.dto.ApiResponse;
import com.example.sbquanan.entity.NhanVien;
import com.example.sbquanan.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nhanvien")
@CrossOrigin(origins = "*")
public class NhanVienController {

    @Autowired private NhanVienService service;

    @GetMapping
    public ApiResponse<List<NhanVien>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NhanVien>> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(nv -> ResponseEntity.ok(ApiResponse.success(nv)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ApiResponse<NhanVien> create(@RequestBody NhanVien nhanVien) {
        return ApiResponse.success(service.create(nhanVien), "Tạo nhân viên thành công");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NhanVien>> update(@PathVariable Long id, @RequestBody NhanVien nhanVien) {
        try {
            return ResponseEntity.ok(ApiResponse.success(service.update(id, nhanVien), "Cập nhật thành công"));
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
