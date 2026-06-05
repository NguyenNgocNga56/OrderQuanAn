package com.example.sbquanan.service;

import com.example.sbquanan.entity.KhuyenMai;
import java.util.List;
import java.util.Optional;

public interface KhuyenMaiService {
    List<KhuyenMai> getAll();
    List<KhuyenMai> getKhaDung(String sdtKhachHang, double tongTien);
    Optional<KhuyenMai> getById(Long id);
    KhuyenMai create(KhuyenMai entity);
    KhuyenMai update(Long id, KhuyenMai updated);
    void delete(Long id);
}
