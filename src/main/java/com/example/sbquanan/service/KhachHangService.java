package com.example.sbquanan.service;

import com.example.sbquanan.entity.KhachHang;
import java.util.List;
import java.util.Optional;

public interface KhachHangService {
    List<KhachHang> getAll();
    Optional<KhachHang> getById(Long id);
    Optional<KhachHang> getBySdt(String sdt);
    KhachHang create(KhachHang khachHang);
    KhachHang update(Long id, KhachHang khachHang);
    void delete(Long id);
}