package com.example.sbquanan.controller;

import com.example.sbquanan.entity.DoAn;
import com.example.sbquanan.entity.DoUong;
import com.example.sbquanan.entity.Menu;
import com.example.sbquanan.entity.MonAn;
import com.example.sbquanan.enums.SizeDoUong;
import com.example.sbquanan.enums.TrangThaiMonAn;
import com.example.sbquanan.repository.MenuRepository;
import com.example.sbquanan.repository.MonAnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/monan")
public class MonAnController {
    @Autowired private MonAnRepository repository;
    @Autowired private MenuRepository menuRepository;

    @GetMapping public List<MonAn> getAll() { return repository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<MonAn> getById(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/menu/{menuId}")
    public List<MonAn> getByMenu(@PathVariable Long menuId) {
        return repository.findByMenu_MenuID(menuId);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Map<String, Object> body) {
        String tenMon = String.valueOf(body.getOrDefault("tenMon", "")).trim();
        if (tenMon.isEmpty()) return ResponseEntity.badRequest().build();

        double gia = Double.parseDouble(String.valueOf(body.getOrDefault("gia", "0")));
        String moTa = String.valueOf(body.getOrDefault("moTa", ""));
        String hinhAnh = String.valueOf(body.getOrDefault("hinhAnh", ""));
        String phanLoai = String.valueOf(body.getOrDefault("phanLoai", "doan"));
        String loai = String.valueOf(body.getOrDefault("loai", ""));
        TrangThaiMonAn trangThai = TrangThaiMonAn.valueOf(String.valueOf(body.getOrDefault("trangThai", "CON_HANG")));

        Long menuId = Long.valueOf(String.valueOf(body.getOrDefault("menuID", "0")));
        Menu menu = menuRepository.findById(menuId)
                .orElseGet(() -> menuRepository.findAll().stream().findFirst().orElse(null));
        if (menu == null) return ResponseEntity.badRequest().build();

        MonAn monAn;
        if ("douong".equalsIgnoreCase(phanLoai)) {
            SizeDoUong size = loai == null || loai.isBlank() ? SizeDoUong.M : SizeDoUong.valueOf(loai);
            monAn = new DoUong(tenMon, gia, moTa, trangThai, menu, size);
            monAn.setHinhAnh(hinhAnh);
        } else {
            monAn = new DoAn(tenMon, gia, moTa, hinhAnh, trangThai, menu, loai);
        }
        repository.save(monAn);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id); return ResponseEntity.noContent().build();
    }
}
