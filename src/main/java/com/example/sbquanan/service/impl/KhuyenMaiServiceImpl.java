package com.example.sbquanan.service.impl;

import com.example.sbquanan.entity.KhuyenMai;
import com.example.sbquanan.exception.ResourceNotFoundException;
import com.example.sbquanan.repository.KhuyenMaiRepository;
import com.example.sbquanan.service.KhuyenMaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class KhuyenMaiServiceImpl implements KhuyenMaiService {

    @Autowired
    private KhuyenMaiRepository repository;

    @Override
    public List<KhuyenMai> getAll() { return repository.findAll(); }

    @Override
    public Optional<KhuyenMai> getById(Long id) { return repository.findById(id); }

    @Override
    public KhuyenMai create(KhuyenMai entity) { return repository.save(entity); }

    @Override
    public KhuyenMai update(Long id, KhuyenMai updated) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Khuyến mãi không tồn tại với id: " + id);
        updated.setKhuyenMaiID(id);
        return repository.save(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Khuyến mãi không tồn tại với id: " + id);
        repository.deleteById(id);
    }
}
