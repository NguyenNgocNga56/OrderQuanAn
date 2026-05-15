package com.example.sbquanan.controller;

import com.example.sbquanan.dto.ApiResponse;
import com.example.sbquanan.entity.KetCa;
import com.example.sbquanan.service.KetCaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ketca")
@CrossOrigin(origins = "*")
public class KetCaController {

    @Autowired private KetCaService service;

    @GetMapping
    public ApiResponse<List<KetCa>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<KetCa>> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(kc -> ResponseEntity.ok(ApiResponse.success(kc)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ApiResponse<KetCa> create(@RequestBody KetCa ketCa) {
        return ApiResponse.success(service.create(ketCa), "Tạo kết ca thành công");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<KetCa>> update(@PathVariable Long id, @RequestBody KetCa ketCa) {
        try {
            return ResponseEntity.ok(ApiResponse.success(service.update(id, ketCa), "Cập nhật thành công"));
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
