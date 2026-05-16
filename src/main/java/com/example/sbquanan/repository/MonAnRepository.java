package com.example.sbquanan.repository;

import com.example.sbquanan.entity.MonAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MonAnRepository extends JpaRepository<MonAn, Long> {
    List<MonAn> findByMenu_MenuID(Long menuId);

    @Query("SELECT m FROM MonAn m WHERE TYPE(m) = com.example.sbquanan.entity.DoAn")
    List<MonAn> findAllDoAn();

    @Query("SELECT m FROM MonAn m WHERE TYPE(m) = com.example.sbquanan.entity.DoUong")
    List<MonAn> findAllDoUong();
}
