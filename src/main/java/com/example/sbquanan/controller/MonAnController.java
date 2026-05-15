package com.example.sbquanan.controller;

import com.example.sbquanan.dto.MonAnDTO;
import com.example.sbquanan.entity.MonAn;
import com.example.sbquanan.repository.MonAnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin("*") // Thêm đúng 1 dòng này để cho phép web lấy dữ liệu
@RestController
public class MonAnController {
    @Autowired private MonAnRepository repository;

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
    public MonAn create(@RequestBody MonAn e) { return repository.save(e); }

    @DeleteMapping("/api/monan/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}