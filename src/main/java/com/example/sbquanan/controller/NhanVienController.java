package com.example.sbquanan.controller;

import com.example.sbquanan.entity.NhanVien;
import com.example.sbquanan.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/nhanvien")
public class NhanVienController {
    @Autowired private NhanVienRepository repository;

    @GetMapping public List<NhanVien> getAll() { return repository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<NhanVien> getById(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping public NhanVien create(@RequestBody NhanVien e) { return repository.save(e); }

    @PutMapping("/{id}")
    public ResponseEntity<NhanVien> update(@PathVariable Long id, @RequestBody NhanVien updated) {
        return repository.findById(id).map(e -> { updated.setId(e.getId()); return ResponseEntity.ok(repository.save(updated)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id); return ResponseEntity.noContent().build();
    }
}
