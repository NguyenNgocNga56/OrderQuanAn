package com.example.sbquanan.service.impl;

import com.example.sbquanan.entity.KetCa;
import com.example.sbquanan.exception.ResourceNotFoundException;
import com.example.sbquanan.repository.KetCaRepository;
import com.example.sbquanan.service.KetCaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class KetCaServiceImpl implements KetCaService {

    @Autowired
    private KetCaRepository repository;

    @Override
    public List<KetCa> getAll() { return repository.findAll(); }

    @Override
    public Optional<KetCa> getById(Long id) { return repository.findById(id); }

    @Override
    public KetCa create(KetCa entity) { return repository.save(entity); }

    @Override
    public KetCa update(Long id, KetCa updated) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Kết ca không tồn tại với id: " + id);
        updated.setKetCaID(id);
        return repository.save(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Kết ca không tồn tại với id: " + id);
        repository.deleteById(id);
    }
}
