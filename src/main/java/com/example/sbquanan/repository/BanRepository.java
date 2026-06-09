package com.example.sbquanan.repository;

import com.example.sbquanan.entity.Ban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BanRepository extends JpaRepository<Ban, Long> {
    List<Ban> findByTrangThai(String trangThai);
}