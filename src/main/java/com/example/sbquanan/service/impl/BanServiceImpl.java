package com.example.sbquanan.service.impl;

import com.example.sbquanan.entity.Ban;
import com.example.sbquanan.exception.ResourceNotFoundException;
import com.example.sbquanan.repository.BanRepository;
import com.example.sbquanan.service.BanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BanServiceImpl implements BanService {

    @Autowired
    private BanRepository repository;

    @Override
    public List<Ban> getAll() {
        return repository.findAll();
    }

    @Override
    public Ban getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bàn không tồn tại với id: " + id));
    }

    @Override
    public Ban create(Ban ban) {
        return repository.save(ban);
    }

    @Override
    public Ban update(Long id, Ban updated) {
        Ban ban = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bàn không tồn tại với id: " + id));
        ban.setTenBan(updated.getTenBan());
        ban.setViTri(updated.getViTri());
        ban.setSoChoNgoi(updated.getSoChoNgoi());
        ban.setTrangThai(updated.getTrangThai());
        ban.setLoaiBan(updated.getLoaiBan());
        ban.setGhiChu(updated.getGhiChu());
        return repository.save(ban);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Bàn không tồn tại với id: " + id);
        repository.deleteById(id);
    }

    @Override
    public Ban capNhatTrangThai(Long banId, String trangThaiMoi) {
        Ban ban = repository.findById(banId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy bàn #" + banId));

        // ← ĐÃ BỎ DEP_BAN, chỉ còn 2 trạng thái
        boolean hopLe = switch (ban.getTrangThai()) {
            case "TRONG"    -> "CO_KHACH".equals(trangThaiMoi);
            case "CO_KHACH" -> "TRONG".equals(trangThaiMoi);
            default         -> false;
        };

        if (!hopLe) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Không thể chuyển bàn từ " + ban.getTrangThai()
                    + " sang " + trangThaiMoi);
        }

        ban.setTrangThai(trangThaiMoi);
        return repository.save(ban);
    }

    @Override
    public Ban toggleTrangThai(Long banId) {
        Ban ban = repository.findById(banId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy bàn #" + banId));

        // Tự động đảo trạng thái: TRONG <-> CO_KHACH
        String trangThaiMoi = "TRONG".equals(ban.getTrangThai()) ? "CO_KHACH" : "TRONG";
        ban.setTrangThai(trangThaiMoi);
        return repository.save(ban);
    }

    @Override
    public List<Ban> getBanTheoTrangThai(String trangThai) {
        return repository.findByTrangThai(trangThai);
    }
}