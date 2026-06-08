package com.example.sbquanan.controller;

import com.example.sbquanan.dto.ApiResponse;
import com.example.sbquanan.entity.Ban;
import com.example.sbquanan.service.BanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ApiResponse<Ban> getById(@PathVariable Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @GetMapping("/trang-thai/{trangThai}")
    public ApiResponse<List<Ban>> getByTrangThai(@PathVariable String trangThai) {
        return ApiResponse.success(service.getBanTheoTrangThai(trangThai));
    }

    // PATCH /api/ban/{id}/trang-thai
    // Body: { "trangThai": "CO_KHACH" }
    // Flow: TRONG <-> CO_KHACH (đã bỏ DEP_BAN)
    @PatchMapping("/{id}/trang-thai")
    public ApiResponse<Ban> capNhatTrangThai(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String trangThaiMoi = body.get("trangThai");
        return ApiResponse.success(
                service.capNhatTrangThai(id, trangThaiMoi),
                "Cập nhật trạng thái bàn thành công");
    }

    // ← THÊM ENDPOINT MỚI
    // PATCH /api/ban/{id}/toggle-trang-thai
    // Không cần body, tự đảo: TRONG -> CO_KHACH, CO_KHACH -> TRONG
    @PatchMapping("/{id}/toggle-trang-thai")
    public ApiResponse<Ban> toggleTrangThai(@PathVariable Long id) {
        return ApiResponse.success(
                service.toggleTrangThai(id),
                "Đổi trạng thái bàn thành công");
    }

    @PostMapping
    public ApiResponse<Ban> create(@RequestBody Ban ban) {
        return ApiResponse.success(service.create(ban), "Tạo bàn thành công");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Ban>> update(
            @PathVariable Long id,
            @RequestBody Ban ban) {
        try {
            return ResponseEntity.ok(ApiResponse.success(
                    service.update(id, ban), "Cập nhật thành công"));
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