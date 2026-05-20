package com.example.sbquanan.repository;

import com.example.sbquanan.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {

    /** Tìm khách hàng theo số điện thoại */
    Optional<KhachHang> findBySdt(String sdt);
}
