package com.example.sbquanan.controller;

import com.example.sbquanan.entity.KhachHang;
import com.example.sbquanan.repository.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/khachhang")
public class KhachHangController {
    @Autowired private KhachHangRepository repository;

    @GetMapping public List<KhachHang> getAll() { return repository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<KhachHang> getById(@PathVariable Integer id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping public KhachHang create(@RequestBody KhachHang e) {
        e.capNhatHangKhachHang(); return repository.save(e);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KhachHang> update(@PathVariable Integer id, @RequestBody KhachHang updated) {
        return repository.findById(id).map(e -> {
            updated.setId(e.getId()); updated.capNhatHangKhachHang();
            return ResponseEntity.ok(repository.save(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id); return ResponseEntity.noContent().build();
    }
}
