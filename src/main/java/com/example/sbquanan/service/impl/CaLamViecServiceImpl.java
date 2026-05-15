package com.example.sbquanan.service.impl;

import com.example.sbquanan.entity.CaLamViec;
import com.example.sbquanan.exception.ResourceNotFoundException;
import com.example.sbquanan.repository.CaLamViecRepository;
import com.example.sbquanan.service.CaLamViecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CaLamViecServiceImpl implements CaLamViecService {

    @Autowired
    private CaLamViecRepository repository;

    @Override
    public List<CaLamViec> getAll() { return repository.findAll(); }

    @Override
    public Optional<CaLamViec> getById(Long id) { return repository.findById(id); }

    @Override
    public CaLamViec create(CaLamViec entity) { return repository.save(entity); }

    @Override
    public CaLamViec update(Long id, CaLamViec updated) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Ca làm việc không tồn tại với id: " + id);
        updated.setCaID(id);
        return repository.save(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Ca làm việc không tồn tại với id: " + id);
        repository.deleteById(id);
    }
}
