package com.example.sbquanan.service;

import com.example.sbquanan.entity.CaLamViec;
import java.util.List;
import java.util.Optional;

public interface CaLamViecService {
    List<CaLamViec> getAll();
    Optional<CaLamViec> getById(Long id);
    CaLamViec create(CaLamViec entity);
    CaLamViec update(Long id, CaLamViec updated);
    void delete(Long id);
}
