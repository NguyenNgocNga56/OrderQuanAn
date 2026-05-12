package com.example.sbquanan.repository;

import com.example.sbquanan.entity.KetCa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KetCaRepository extends JpaRepository<KetCa, Integer> {}
