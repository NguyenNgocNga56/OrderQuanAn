package com.example.sbquanan.controller;

import com.example.sbquanan.entity.DonHang;
import com.example.sbquanan.repository.DonHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/donhang")
public class DonHangController {
    @Autowired private DonHangRepository repository;

    @GetMapping public List<DonHang> getAll() { return repository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<DonHang> getById(@PathVariable Integer id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping public DonHang create(@RequestBody DonHang e) { return repository.save(e); }

    @PutMapping("/{id}")
    public ResponseEntity<DonHang> update(@PathVariable Integer id, @RequestBody DonHang updated) {
        return repository.findById(id).map(e -> { updated.setDonHangID(e.getDonHangID()); return ResponseEntity.ok(repository.save(updated)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id); return ResponseEntity.noContent().build();
    }
}
