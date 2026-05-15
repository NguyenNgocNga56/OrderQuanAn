package com.example.sbquanan.service;

import com.example.sbquanan.entity.ThanhToan;
import java.util.List;
import java.util.Optional;

public interface ThanhToanService {
    List<ThanhToan> getAll();
    Optional<ThanhToan> getById(Long id);
    ThanhToan create(ThanhToan entity);
    ThanhToan update(Long id, ThanhToan updated);
    void delete(Long id);
}
