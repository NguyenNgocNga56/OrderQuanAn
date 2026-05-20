package com.example.sbquanan.controller;

import com.example.sbquanan.dto.MonAnDTO;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@CrossOrigin("*") // Cho phép web lấy dữ liệu
@RestController
public class MonAnController {
    @Autowired private MonAnRepository repository;
    @Autowired private MenuRepository menuRepository;

    @GetMapping("/api/monan")
    @Transactional(readOnly = true)
    public List<MonAnDTO> getAll() {
        return repository.findAllDoAn().stream()
                .map(MonAnDTO::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/douong")
    @Transactional(readOnly = true)
    public List<MonAnDTO> getDoUong() {
        return repository.findAllDoUong().stream()
                .map(MonAnDTO::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/monan/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<MonAnDTO> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(m -> ResponseEntity.ok(MonAnDTO.from(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/monan/menu/{menuId}")
    @Transactional(readOnly = true)
    public List<MonAnDTO> getByMenu(@PathVariable Long menuId) {
        return repository.findByMenu_MenuID(menuId).stream()
                .map(MonAnDTO::from)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/monan")
    public MonAn create(@RequestBody Map<String, Object> payload) {
        String tenMon = asString(payload.get("tenMon"));
        double gia = asDouble(payload.get("gia"));
        Long menuID = asLong(payload.get("menuID"));

        if (tenMon.isBlank()) throw new IllegalArgumentException("Ten mon khong duoc de trong");
        if (gia <= 0) throw new IllegalArgumentException("Gia mon phai lon hon 0");
        if (menuID == null) throw new IllegalArgumentException("Menu khong hop le");

        Menu menu = menuRepository.findById(menuID)
                .orElseThrow(() -> new IllegalArgumentException("Menu khong ton tai"));

        String phanLoai = asString(payload.get("phanLoai"));
        MonAn monAn;
        if ("douong".equalsIgnoreCase(phanLoai)) {
            DoUong doUong = new DoUong();
            doUong.setSize(parseSize(asString(payload.get("loai"))));
            monAn = doUong;
        } else {
            DoAn doAn = new DoAn();
            doAn.setDonViTinh(asString(payload.get("loai")));
            monAn = doAn;
        }

        monAn.setTenMon(tenMon);
        monAn.setGia(gia);
        monAn.setMenu(menu);
        monAn.setMoTa(asString(payload.get("moTa")));
        monAn.setHinhAnh(asString(payload.get("hinhAnh")));
        monAn.setTrangThai(parseTrangThai(asString(payload.get("trangThai"))));
        return repository.save(monAn);
    }

    private String asString(Object value) {
        return value == null ? "" : String.valueOf(value).trim();
    }

    private Long asLong(Object value) {
        String text = asString(value);
        if (text.isBlank()) return null;
        return Long.valueOf(text);
    }

    private double asDouble(Object value) {
        String text = asString(value);
        if (text.isBlank()) return 0;
        return Double.parseDouble(text);
    }

    private TrangThaiMonAn parseTrangThai(String value) {
        if (value.isBlank()) return TrangThaiMonAn.CON_HANG;
        return TrangThaiMonAn.valueOf(value);
    }

    private SizeDoUong parseSize(String value) {
        if (value.isBlank()) return SizeDoUong.M;
        return SizeDoUong.valueOf(value.toUpperCase());
    }

    @DeleteMapping("/api/monan/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
