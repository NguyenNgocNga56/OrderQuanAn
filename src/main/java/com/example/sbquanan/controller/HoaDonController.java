package com.example.sbquanan.controller;

import com.example.sbquanan.dto.ApiResponse;
import com.example.sbquanan.entity.HoaDon;
import com.example.sbquanan.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hoadon")
@CrossOrigin(origins = "*")
public class HoaDonController {

    @Autowired private HoaDonService service;

    @GetMapping
    public ApiResponse<List<HoaDon>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HoaDon>> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(hd -> ResponseEntity.ok(ApiResponse.success(hd)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ApiResponse<HoaDon> create(@RequestBody HoaDon hoaDon) {
        return ApiResponse.success(service.create(hoaDon), "Tạo hóa đơn thành công");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HoaDon>> update(@PathVariable Long id, @RequestBody HoaDon hoaDon) {
        try {
            return ResponseEntity.ok(ApiResponse.success(service.update(id, hoaDon), "Cập nhật thành công"));
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
