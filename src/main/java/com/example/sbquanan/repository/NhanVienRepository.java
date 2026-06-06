package com.example.sbquanan.repository;

import com.example.sbquanan.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, Long> {
    Optional<NhanVien> findByEmail(String email);
    Optional<NhanVien> findByEmailIgnoreCase(String email);
}
