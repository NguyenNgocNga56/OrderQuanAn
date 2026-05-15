package com.example.sbquanan.service.impl;

import com.example.sbquanan.entity.HoaDon;
import com.example.sbquanan.exception.ResourceNotFoundException;
import com.example.sbquanan.repository.HoaDonRepository;
import com.example.sbquanan.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HoaDonServiceImpl implements HoaDonService {

    @Autowired
    private HoaDonRepository repository;

    @Override
    public List<HoaDon> getAll() { return repository.findAll(); }

    @Override
    public Optional<HoaDon> getById(Long id) { return repository.findById(id); }

    @Override
    public HoaDon create(HoaDon entity) {
        // FIX: gọi tinhThanhTien() trước khi save để đảm bảo thanhTien đúng
        entity.tinhThanhTien();
        return repository.save(entity);
    }

    @Override
    public HoaDon update(Long id, HoaDon updated) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setDonHang(updated.getDonHang());
                    existing.setKhuyenMai(updated.getKhuyenMai());
                    existing.setTongTien(updated.getTongTien());
                    existing.setGiamGia(updated.getGiamGia());
                    existing.tinhThanhTien();
                    return repository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Hóa đơn không tồn tại với id: " + id));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Hóa đơn không tồn tại với id: " + id);
        repository.deleteById(id);
    }
}
