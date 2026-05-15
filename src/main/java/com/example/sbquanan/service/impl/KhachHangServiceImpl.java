package com.example.sbquanan.service.impl;

import com.example.sbquanan.entity.KhachHang;
import com.example.sbquanan.exception.ResourceNotFoundException;
import com.example.sbquanan.repository.KhachHangRepository;
import com.example.sbquanan.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class KhachHangServiceImpl implements KhachHangService {

    @Autowired
    private KhachHangRepository repository;

    @Override
    public List<KhachHang> getAll() { return repository.findAll(); }

    @Override
    public Optional<KhachHang> getById(Long id) { return repository.findById(id); }

    @Override
    public KhachHang create(KhachHang entity) {
        entity.capNhatHangKhachHang();
        return repository.save(entity);
    }

    @Override
    public KhachHang update(Long id, KhachHang updated) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Khách hàng không tồn tại với id: " + id);
        updated.setId(id);
        updated.capNhatHangKhachHang();
        return repository.save(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Khách hàng không tồn tại với id: " + id);
        repository.deleteById(id);
    }
}
