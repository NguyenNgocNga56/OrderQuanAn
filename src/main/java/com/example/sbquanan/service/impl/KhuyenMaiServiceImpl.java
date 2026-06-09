package com.example.sbquanan.service.impl;

import com.example.sbquanan.entity.KhuyenMai;
import com.example.sbquanan.entity.KhachHang;
import com.example.sbquanan.exception.ResourceNotFoundException;
import com.example.sbquanan.repository.HoaDonRepository;
import com.example.sbquanan.repository.KhachHangRepository;
import com.example.sbquanan.repository.KhuyenMaiRepository;
import com.example.sbquanan.service.KhuyenMaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class KhuyenMaiServiceImpl implements KhuyenMaiService {

    @Autowired
    private KhuyenMaiRepository repository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Override
    public List<KhuyenMai> getAll() { return repository.findAll(); }

    @Override
    public List<KhuyenMai> getKhaDung(String sdtKhachHang, double tongTien) {
        KhachHang khachHang = null;
        if (sdtKhachHang != null && !sdtKhachHang.isBlank()) {
            String sdt = sdtKhachHang.trim();
            khachHang = khachHangRepository.findBySdt(sdt)
                    .or(() -> khachHangRepository.findBySdtTrimmed(sdt))
                    .orElseThrow(() -> new IllegalArgumentException("Khong tim thay khach hang voi so dien thoai: " + sdt));
        }

        // Lấy tập KhuyenMaiID đã dùng bởi SĐT này (nếu có)
        final java.util.Set<Long> daDung = (sdtKhachHang != null && !sdtKhachHang.isBlank())
                ? hoaDonRepository.findUsedKhuyenMaiIdsBySdt(sdtKhachHang.trim())
                : java.util.Collections.emptySet();

        KhachHang finalKhachHang = khachHang;
        return repository.findAll().stream()
                .filter(khuyenMai -> khuyenMai.duDieuKien(tongTien, finalKhachHang))
                .filter(khuyenMai -> !daDung.contains(khuyenMai.getKhuyenMaiID()))
                .collect(Collectors.toList());
    }

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