package com.example.sbquanan.repository;

import com.example.sbquanan.entity.MonAn;
import com.example.sbquanan.enums.TrangThaiMonAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MonAnRepository extends JpaRepository<MonAn, Long> {
    List<MonAn> findByMenu_MenuID(Long menuId);
    List<MonAn> findByTrangThai(TrangThaiMonAn trangThai);
    List<MonAn> findByMenu_MenuIDAndTrangThai(Long menuId, TrangThaiMonAn trangThai);
}
