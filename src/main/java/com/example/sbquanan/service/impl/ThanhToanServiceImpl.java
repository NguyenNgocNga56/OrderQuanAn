package com.example.sbquanan.service.impl;

import com.example.sbquanan.entity.ThanhToan;
import com.example.sbquanan.exception.ResourceNotFoundException;
import com.example.sbquanan.repository.ThanhToanRepository;
import com.example.sbquanan.service.ThanhToanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ThanhToanServiceImpl implements ThanhToanService {

    @Autowired
    private ThanhToanRepository repository;

    @Override
    public List<ThanhToan> getAll() { return repository.findAll(); }

    @Override
    public Optional<ThanhToan> getById(Long id) { return repository.findById(id); }

    @Override
    public ThanhToan create(ThanhToan entity) { return repository.save(entity); }

    @Override
    public ThanhToan update(Long id, ThanhToan updated) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setPhuongThuc(updated.getPhuongThuc());
                    existing.setSoTien(updated.getSoTien());
                    existing.setTrangThai(updated.getTrangThai());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Thanh toán không tồn tại với id: " + id));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Thanh toán không tồn tại với id: " + id);
        repository.deleteById(id);
    }
}
