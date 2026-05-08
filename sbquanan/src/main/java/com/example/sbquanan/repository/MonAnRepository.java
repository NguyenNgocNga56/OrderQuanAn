package com.example.sbquanan.repository;

import com.example.sbquanan.entity.MonAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MonAnRepository extends JpaRepository<MonAn, Integer> {
    List<MonAn> findByMenu_MenuID(Integer menuId);
}
