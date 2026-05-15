package com.example.sbquanan.service;

import com.example.sbquanan.entity.KhachHang;
import java.util.List;
import java.util.Optional;

public interface KhachHangService {
    List<KhachHang> getAll();
    Optional<KhachHang> getById(Long id);
    KhachHang create(KhachHang entity);
    KhachHang update(Long id, KhachHang updated);
    void delete(Long id);
}
