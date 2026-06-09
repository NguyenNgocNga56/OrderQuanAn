package com.example.sbquanan.repository;

import com.example.sbquanan.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {

    /** Tìm khách hàng theo số điện thoại (exact) */
    Optional<KhachHang> findBySdt(String sdt);

    /** Tìm theo SĐT, trim khoảng trắng cả 2 đầu, không phân biệt hoa thường */
    @Query("SELECT k FROM KhachHang k WHERE TRIM(k.sdt) = TRIM(:sdt)")
    Optional<KhachHang> findBySdtTrimmed(@Param("sdt") String sdt);
}