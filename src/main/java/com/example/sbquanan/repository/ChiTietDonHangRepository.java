package com.example.sbquanan.repository;

import com.example.sbquanan.entity.ChiTietDonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietDonHangRepository extends JpaRepository<ChiTietDonHang, Long> {

    @Query("SELECT ct FROM ChiTietDonHang ct WHERE ct.donHang.donHangID = :donHangID")
    List<ChiTietDonHang> findByDonHangID(@Param("donHangID") Long donHangID);
}
