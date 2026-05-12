package com.example.sbquanan.service;

import com.example.sbquanan.entity.MonAn;
import java.util.List;
import java.util.Optional;

public interface MonAnService {
    List<MonAn> getAll();
    Optional<MonAn> getById(Long id);
    List<MonAn> getByMenu(Long menuId);
    MonAn create(MonAn monAn);
    MonAn update(Long id, MonAn updated);
    void delete(Long id);
}