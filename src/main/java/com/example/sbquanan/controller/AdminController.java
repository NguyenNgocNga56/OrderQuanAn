package com.example.sbquanan.controller;

import com.example.sbquanan.dto.ApiResponse;
import com.example.sbquanan.dto.DashboardDTO;
import com.example.sbquanan.dto.ThongKeDTO;
import com.example.sbquanan.enums.TrangThaiDonHang;
import com.example.sbquanan.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired private AdminService adminService;

    /**
     * GET /api/admin/dashboard
     * Tra ve toan bo don hang kem thong tin khach, tong tien, trang thai
     */
    @GetMapping("/dashboard")
    public ApiResponse<DashboardDTO> getDashboard() {
        return ApiResponse.success(adminService.getDashboard());
    }

    /**
     * PATCH /api/admin/orders/{id}/trang-thai
     * Body: { "trangThai": "DANG_NAU" }
     * Flow: CHO_XAC_NHAN → DANG_NAU → DA_PHUC_VU → HOAN_THANH / DA_HUY
     */
    @PatchMapping("/orders/{id}/trang-thai")
    public ApiResponse<DashboardDTO.DonHangDTO> capNhatTrangThai(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, String> body) {
        TrangThaiDonHang trangThaiMoi = TrangThaiDonHang.valueOf(body.get("trangThai"));
        return ApiResponse.success(
                adminService.capNhatTrangThai(id, trangThaiMoi),
                "Cap nhat trang thai thanh cong");
    }

    /**
     * GET /api/admin/thongke?from=2026-01-01T00:00:00&to=2026-12-31T23:59:59
     */
    @GetMapping("/thongke")
    public ApiResponse<ThongKeDTO> getThongKe(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to) {
        return ApiResponse.success(adminService.getThongKe(from, to));
    }
}