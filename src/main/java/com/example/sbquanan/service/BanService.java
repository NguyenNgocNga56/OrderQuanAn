package com.example.sbquanan.service;

import com.example.sbquanan.entity.Ban;
import java.util.List;
import java.util.Optional;

public interface BanService {
    List<Ban> getAll();
    Optional<Ban> getById(Long id);
    Ban create(Ban entity);
    Ban update(Long id, Ban updated);
    void delete(Long id);
}
