package com.example.sbquanan.repository;

import com.example.sbquanan.entity.KhuyenMai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, Long> {
    Optional<KhuyenMai> findByTenKhuyenMaiIgnoreCase(String tenKhuyenMai);
}
