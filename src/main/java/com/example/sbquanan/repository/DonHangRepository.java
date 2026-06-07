package com.example.sbquanan.repository;

import com.example.sbquanan.entity.DonHang;
import com.example.sbquanan.enums.TrangThaiDonHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Long> {

    @Query("SELECT d FROM DonHang d " +
           "LEFT JOIN FETCH d.khachHang " +
           "LEFT JOIN FETCH d.nhanVien " +
           "ORDER BY d.ngayDat DESC")
    List<DonHang> findAllWithDetails();

    @Query("SELECT d FROM DonHang d WHERE d.ngayDat BETWEEN :from AND :to")
    List<DonHang> findByNgayDatBetween(
            @Param("from") LocalDateTime from,
            @Param("to")   LocalDateTime to);

    @Query("SELECT d FROM DonHang d " +
           "LEFT JOIN FETCH d.khachHang k " +
           "WHERE (:sdt IS NULL OR k.sdt = :sdt) " +
           "AND (:ngay IS NULL OR CAST(d.ngayDat AS date) = CAST(:ngay AS date)) " +
           "ORDER BY d.ngayDat DESC")
    List<DonHang> searchDonHang(
            @Param("sdt")  String sdt,
            @Param("ngay") LocalDateTime ngay);

    long countByTrangThai(TrangThaiDonHang trangThai);

    Page<DonHang> findAllByOrderByNgayDatDesc(Pageable pageable);

    @Query("SELECT COALESCE(SUM(hd.thanhTien), 0) FROM HoaDon hd " +
           "WHERE hd.donHang.trangThai = com.example.sbquanan.enums.TrangThaiDonHang.HOAN_THANH " +
           "AND hd.donHang.ngayDat >= :startOfDay")
    double doanhThuTuNgay(@Param("startOfDay") LocalDateTime startOfDay);
}