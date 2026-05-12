package com.example.sbquanan.controller;

import com.example.sbquanan.entity.ThanhToan;
import com.example.sbquanan.repository.ThanhToanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/thanhtoan")
public class ThanhToanController {
    @Autowired private ThanhToanRepository repository;

    @GetMapping public List<ThanhToan> getAll() { return repository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<ThanhToan> getById(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping public ThanhToan create(@RequestBody ThanhToan e) { return repository.save(e); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id); return ResponseEntity.noContent().build();
    }
}
