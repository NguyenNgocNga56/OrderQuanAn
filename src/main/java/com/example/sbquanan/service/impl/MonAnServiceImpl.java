package com.example.sbquanan.service.impl;

import com.example.sbquanan.entity.MonAn;
import com.example.sbquanan.exception.ResourceNotFoundException;
import com.example.sbquanan.repository.MonAnRepository;
import com.example.sbquanan.service.MonAnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MonAnServiceImpl implements MonAnService {

    @Autowired
    private MonAnRepository repository;

    @Override
    public List<MonAn> getAll() { return repository.findAll(); }

    @Override
    public Optional<MonAn> getById(Long id) { return repository.findById(id); }

    @Override
    public List<MonAn> getByMenu(Long menuId) {
        return repository.findByMenu_MenuID(menuId);
    }

    @Override
    public MonAn create(MonAn monAn) { return repository.save(monAn); }

    @Override
    public MonAn update(Long id, MonAn updated) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setTenMon(updated.getTenMon());
                    existing.setGia(updated.getGia());
                    existing.setMoTa(updated.getMoTa());
                    existing.setHinhAnh(updated.getHinhAnh());
                    existing.setTrangThai(updated.getTrangThai());
                    existing.setMenu(updated.getMenu());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Món ăn không tồn tại với id: " + id));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Món ăn không tồn tại với id: " + id);
        repository.deleteById(id);
    }
}
