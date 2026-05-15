package com.example.sbquanan.controller;

import com.example.sbquanan.dto.ApiResponse;
import com.example.sbquanan.entity.CaLamViec;
import com.example.sbquanan.service.CaLamViecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calamviec")
@CrossOrigin(origins = "*")
public class CaLamViecController {

    @Autowired private CaLamViecService service;

    @GetMapping
    public ApiResponse<List<CaLamViec>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CaLamViec>> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(c -> ResponseEntity.ok(ApiResponse.success(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ApiResponse<CaLamViec> create(@RequestBody CaLamViec caLamViec) {
        return ApiResponse.success(service.create(caLamViec), "Tạo ca làm việc thành công");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CaLamViec>> update(@PathVariable Long id, @RequestBody CaLamViec caLamViec) {
        try {
            return ResponseEntity.ok(ApiResponse.success(service.update(id, caLamViec), "Cập nhật thành công"));
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
