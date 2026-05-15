package com.example.sbquanan.service;

import com.example.sbquanan.entity.ChiTietDonHang;
import java.util.List;
import java.util.Optional;

public interface ChiTietDonHangService {
    List<ChiTietDonHang> getAll();
    Optional<ChiTietDonHang> getById(Long id);
    ChiTietDonHang create(ChiTietDonHang entity);
    ChiTietDonHang update(Long id, ChiTietDonHang updated);
    void delete(Long id);
}
