package com.example.sbquanan.repository;

import com.example.sbquanan.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {
    Optional<HoaDon> findByDonHang_DonHangID(Long donHangID);
    List<HoaDon> findByDonHang_DonHangIDIn(List<Long> donHangIDs); // ★ MỚI
}