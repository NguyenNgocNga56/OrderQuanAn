package com.example.sbquanan.repository;

import com.example.sbquanan.entity.ThanhToan;
import com.example.sbquanan.enums.TrangThaiThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThanhToanRepository extends JpaRepository<ThanhToan, Long> {
    boolean existsByHoaDon_HoaDonIDAndTrangThai(Long hoaDonID, TrangThaiThanhToan trangThai);
}
