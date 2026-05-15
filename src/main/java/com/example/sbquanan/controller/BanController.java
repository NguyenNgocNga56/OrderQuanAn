package com.example.sbquanan.controller;

import com.example.sbquanan.dto.ApiResponse;
import com.example.sbquanan.entity.Ban;
import com.example.sbquanan.service.BanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ban")
@CrossOrigin(origins = "*")
public class BanController {

    @Autowired private BanService service;

    @GetMapping
    public ApiResponse<List<Ban>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Ban>> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(b -> ResponseEntity.ok(ApiResponse.success(b)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ApiResponse<Ban> create(@RequestBody Ban ban) {
        return ApiResponse.success(service.create(ban), "Tạo bàn thành công");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Ban>> update(@PathVariable Long id, @RequestBody Ban ban) {
        try {
            return ResponseEntity.ok(ApiResponse.success(service.update(id, ban), "Cập nhật thành công"));
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
