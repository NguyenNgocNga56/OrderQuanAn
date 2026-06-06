package com.example.sbquanan.service.impl;

import com.example.sbquanan.entity.NhanVien;
import com.example.sbquanan.exception.ResourceNotFoundException;
import com.example.sbquanan.repository.NhanVienRepository;
import com.example.sbquanan.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NhanVienServiceImpl implements NhanVienService {

    @Autowired
    private NhanVienRepository repository;

    @Override
    public List<NhanVien> getAll() { return repository.findAll(); }

    @Override
    public Optional<NhanVien> getById(Long id) { return repository.findById(id); }

    @Override
    public NhanVien create(NhanVien entity) { return repository.save(entity); }

    @Override
    public NhanVien update(Long id, NhanVien updated) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Nhân viên không tồn tại với id: " + id);
        updated.setId(id);
        return repository.save(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Nhân viên không tồn tại với id: " + id);
        repository.deleteById(id);
    }
}
