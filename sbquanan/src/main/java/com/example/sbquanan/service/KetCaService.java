package com.example.sbquanan.service;

import com.example.sbquanan.entity.KetCa;
import java.util.List;
import java.util.Optional;

public interface KetCaService {
    List<KetCa> getAll();
    Optional<KetCa> getById(Long id);
    KetCa create(KetCa entity);
    KetCa update(Long id, KetCa updated);
    void delete(Long id);
}
