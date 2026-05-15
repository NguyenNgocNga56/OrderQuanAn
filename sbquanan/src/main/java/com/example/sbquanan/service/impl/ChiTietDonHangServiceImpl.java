package com.example.sbquanan.service.impl;

import com.example.sbquanan.entity.ChiTietDonHang;
import com.example.sbquanan.exception.ResourceNotFoundException;
import com.example.sbquanan.repository.ChiTietDonHangRepository;
import com.example.sbquanan.service.ChiTietDonHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChiTietDonHangServiceImpl implements ChiTietDonHangService {

    @Autowired
    private ChiTietDonHangRepository repository;

    @Override
    public List<ChiTietDonHang> getAll() { return repository.findAll(); }

    @Override
    public Optional<ChiTietDonHang> getById(Long id) { return repository.findById(id); }

    public List<ChiTietDonHang> getByDonHang(Long donHangId) {
        return repository.findByDonHang_DonHangID(donHangId);
    }

    @Override
    public ChiTietDonHang create(ChiTietDonHang entity) {
        entity.tinhToan();
        return repository.save(entity);
    }

    @Override
    public ChiTietDonHang update(Long id, ChiTietDonHang updated) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setMonAn(updated.getMonAn());
                    existing.setSoLuong(updated.getSoLuong());
                    existing.tinhToan();
                    return repository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Chi tiết đơn hàng không tồn tại với id: " + id));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Chi tiết đơn hàng không tồn tại với id: " + id);
        repository.deleteById(id);
    }
}
