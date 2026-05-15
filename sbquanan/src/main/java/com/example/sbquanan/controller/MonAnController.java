package com.example.sbquanan.controller;

import com.example.sbquanan.dto.ApiResponse;
import com.example.sbquanan.entity.MonAn;
import com.example.sbquanan.service.MonAnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monan")
@CrossOrigin(origins = "*")
public class MonAnController {

    @Autowired private MonAnService service;

    @GetMapping
    public ApiResponse<List<MonAn>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MonAn>> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(m -> ResponseEntity.ok(ApiResponse.success(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/menu/{menuId}")
    public ApiResponse<List<MonAn>> getByMenu(@PathVariable Long menuId) {
        return ApiResponse.success(service.getByMenu(menuId));
    }

    @PostMapping
    public ApiResponse<MonAn> create(@RequestBody MonAn monAn) {
        return ApiResponse.success(service.create(monAn), "Tạo món ăn thành công");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MonAn>> update(@PathVariable Long id, @RequestBody MonAn monAn) {
        try {
            return ResponseEntity.ok(ApiResponse.success(service.update(id, monAn), "Cập nhật thành công"));
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
