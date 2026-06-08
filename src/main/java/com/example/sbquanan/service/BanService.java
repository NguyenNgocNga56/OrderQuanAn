package com.example.sbquanan.service;

import com.example.sbquanan.entity.Ban;
import java.util.List;

public interface BanService {
    List<Ban> getAll();
    Ban getById(Long id);
    Ban create(Ban ban);
    Ban update(Long id, Ban ban);
    void delete(Long id);
    Ban capNhatTrangThai(Long banId, String trangThaiMoi);
    List<Ban> getBanTheoTrangThai(String trangThai);
}