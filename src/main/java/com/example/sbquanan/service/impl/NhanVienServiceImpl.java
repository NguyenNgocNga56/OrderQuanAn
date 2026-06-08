package com.example.sbquanan.service.impl;

import com.example.sbquanan.entity.NhanVien;
import com.example.sbquanan.exception.ResourceNotFoundException;
import com.example.sbquanan.repository.NhanVienRepository;
import com.example.sbquanan.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NhanVienServiceImpl implements NhanVienService {

    @Autowired private NhanVienRepository repository;

    @Override
    public List<NhanVien> getAll() { return repository.findAll(); }

    @Override
    public Optional<NhanVien> getById(Long id) { return repository.findById(id); }

    @Override
    public NhanVien create(NhanVien nhanVien) {
        return repository.save(nhanVien);
    }

    @Override
    public NhanVien update(Long id, NhanVien updated) {
        NhanVien existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Nhân viên không tồn tại với id: " + id));
        existing.setHoTen(updated.getHoTen());
        existing.setSdt(updated.getSdt());
        existing.setDiaChi(updated.getDiaChi());
        existing.setEmail(updated.getEmail());
        existing.setChucVu(updated.getChucVu());
        existing.setLuong(updated.getLuong());
        existing.setTrangThai(updated.isTrangThai());
        // Chỉ đổi password nếu FE gửi lên
        if (updated.getPassword() != null && !updated.getPassword().isBlank())
            existing.setPassword(updated.getPassword());
        return repository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Nhân viên không tồn tại với id: " + id);
        repository.deleteById(id);
    }

    @Override
    public Optional<NhanVien> findByEmail(String email) {
        return repository.findByEmail(email);
    }
}