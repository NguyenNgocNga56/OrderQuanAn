package com.example.sbquanan.service;

import com.example.sbquanan.entity.HoaDon;
import java.util.List;
import java.util.Optional;

public interface HoaDonService {
    List<HoaDon> getAll();
    Optional<HoaDon> getById(Long id);
    HoaDon create(HoaDon entity);
    HoaDon update(Long id, HoaDon updated);
    void delete(Long id);
}
