package com.example.sbquanan.repository;

import com.example.sbquanan.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {
    Optional<HoaDon> findByDonHang_DonHangID(Long donHangID);
    List<HoaDon> findByDonHang_DonHangIDIn(List<Long> donHangIDs); // ★ MỚI

    /**
     * Lấy tập hợp KhuyenMaiID đã được sử dụng bởi khách hàng có SĐT :sdt
     * (chỉ tính các đơn có khách hàng liên kết).
     */
    @Query("SELECT h.khuyenMai.khuyenMaiID FROM HoaDon h " +
            "WHERE h.khuyenMai IS NOT NULL " +
            "AND h.donHang.khachHang IS NOT NULL " +
            "AND h.donHang.khachHang.sdt = :sdt")
    Set<Long> findUsedKhuyenMaiIdsBySdt(@Param("sdt") String sdt);
}