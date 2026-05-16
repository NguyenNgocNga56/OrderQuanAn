package com.example.sbquanan.service;

import com.example.sbquanan.entity.DonHang;
import java.util.List;
import java.util.Optional;

public interface DonHangService {
    List<DonHang> getAll();
    Optional<DonHang> getById(Long id);
    DonHang create(DonHang entity);
    DonHang update(Long id, DonHang updated);
    void delete(Long id);
}
