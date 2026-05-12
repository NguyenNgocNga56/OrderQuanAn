package com.example.sbquanan.controller;

import com.example.sbquanan.entity.KhuyenMai;
import com.example.sbquanan.repository.KhuyenMaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/khuyenmai")
public class KhuyenMaiController {
    @Autowired private KhuyenMaiRepository repository;

    @GetMapping public List<KhuyenMai> getAll() { return repository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<KhuyenMai> getById(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping public KhuyenMai create(@RequestBody KhuyenMai e) { return repository.save(e); }

    @PutMapping("/{id}")
    public ResponseEntity<KhuyenMai> update(@PathVariable Long id, @RequestBody KhuyenMai updated) {
        return repository.findById(id).map(e -> { updated.setKhuyenMaiID(e.getKhuyenMaiID()); return ResponseEntity.ok(repository.save(updated)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id); return ResponseEntity.noContent().build();
    }
}
