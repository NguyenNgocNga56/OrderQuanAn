package com.example.sbquanan.service.impl;

import com.example.sbquanan.entity.Ban;
import com.example.sbquanan.exception.ResourceNotFoundException;
import com.example.sbquanan.repository.BanRepository;
import com.example.sbquanan.service.BanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BanServiceImpl implements BanService {

    @Autowired
    private BanRepository repository;

    @Override
    public List<Ban> getAll() { return repository.findAll(); }

    @Override
    public Optional<Ban> getById(Long id) { return repository.findById(id); }

    @Override
    public Ban create(Ban entity) { return repository.save(entity); }

    @Override
    public Ban update(Long id, Ban updated) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Bàn không tồn tại với id: " + id);
        updated.setBanID(id);
        return repository.save(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Bàn không tồn tại với id: " + id);
        repository.deleteById(id);
    }
}
