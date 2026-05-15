package com.example.sbquanan.service.impl;

import com.example.sbquanan.entity.DonHang;
import com.example.sbquanan.exception.ResourceNotFoundException;
import com.example.sbquanan.repository.DonHangRepository;
import com.example.sbquanan.service.DonHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DonHangServiceImpl implements DonHangService {

    @Autowired
    private DonHangRepository repository;

    @Override
    public List<DonHang> getAll() { return repository.findAll(); }

    @Override
    public Optional<DonHang> getById(Long id) { return repository.findById(id); }

    @Override
    public DonHang create(DonHang entity) { return repository.save(entity); }

    @Override
    public DonHang update(Long id, DonHang updated) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setTrangThai(updated.getTrangThai());
                    existing.setTongTien(updated.getTongTien());
                    existing.setKhachHang(updated.getKhachHang());
                    existing.setNhanVien(updated.getNhanVien());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Đơn hàng không tồn tại với id: " + id));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Đơn hàng không tồn tại với id: " + id);
        repository.deleteById(id);
    }
}
