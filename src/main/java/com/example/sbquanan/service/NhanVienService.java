package com.example.sbquanan.service;

import com.example.sbquanan.entity.NhanVien;
import java.util.List;
import java.util.Optional;

public interface NhanVienService {
    List<NhanVien> getAll();
    Optional<NhanVien> getById(Long id);
    NhanVien create(NhanVien entity);
    NhanVien update(Long id, NhanVien updated);
    void delete(Long id);
}
