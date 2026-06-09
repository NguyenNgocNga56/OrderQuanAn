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

    @Autowired private KhachHangRepository repository;

    @Override
    public List<KhachHang> getAll() { return repository.findAll(); }

    @Override
    public Optional<KhachHang> getById(Long id) { return repository.findById(id); }

    @Override
    public Optional<KhachHang> getBySdt(String sdt) {
        String cleaned = sdt == null ? "" : sdt.trim();
        // Thử exact match trước, nếu không có thì thử trim cả DB
        Optional<KhachHang> result = repository.findBySdt(cleaned);
        if (result.isEmpty()) {
            result = repository.findBySdtTrimmed(cleaned);
        }
        return result;
    }

    @Override
    public KhachHang create(KhachHang khachHang) {
        khachHang.capNhatHangKhachHang();
        return repository.save(khachHang);
    }

    @Override
    public KhachHang update(Long id, KhachHang updated) {
        KhachHang existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Khách hàng không tồn tại với id: " + id));
        existing.setHoTen(updated.getHoTen());
        existing.setSdt(updated.getSdt());
        existing.setDiaChi(updated.getDiaChi());
        existing.setEmail(updated.getEmail());
        existing.setDiemTichLuy(updated.getDiemTichLuy());
        existing.capNhatHangKhachHang(); // tự cập nhật loại DONG/BAC/VANG/KIM_CUONG
        return repository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Khách hàng không tồn tại với id: " + id);
        repository.deleteById(id);
    }
}