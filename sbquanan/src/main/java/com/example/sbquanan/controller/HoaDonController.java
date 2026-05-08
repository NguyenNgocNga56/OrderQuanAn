package com.example.sbquanan.controller;

import com.example.sbquanan.entity.HoaDon;
import com.example.sbquanan.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/hoadon")
public class HoaDonController {
    @Autowired private HoaDonRepository repository;

    @GetMapping public List<HoaDon> getAll() { return repository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<HoaDon> getById(@PathVariable Integer id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping public HoaDon create(@RequestBody HoaDon e) { return repository.save(e); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id); return ResponseEntity.noContent().build();
    }
}
