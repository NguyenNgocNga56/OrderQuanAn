package com.example.sbquanan.service;

import com.example.sbquanan.dto.DashboardDTO;
import com.example.sbquanan.dto.ThongKeDTO;
import com.example.sbquanan.enums.TrangThaiDonHang;

import java.time.LocalDateTime;

public interface AdminService {
    DashboardDTO getDashboard();
    DashboardDTO.DonHangDTO capNhatTrangThai(Long donHangID, TrangThaiDonHang trangThaiMoi);
    ThongKeDTO getThongKe(LocalDateTime from, LocalDateTime to);
}