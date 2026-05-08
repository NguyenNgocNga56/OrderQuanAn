package com.example.sbquanan.controller;

import com.example.sbquanan.entity.CaLamViec;
import com.example.sbquanan.repository.CaLamViecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/calamviec")
public class CaLamViecController {
    @Autowired private CaLamViecRepository repository;

    @GetMapping public List<CaLamViec> getAll() { return repository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<CaLamViec> getById(@PathVariable Integer id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping public CaLamViec create(@RequestBody CaLamViec e) { return repository.save(e); }

    @PutMapping("/{id}")
    public ResponseEntity<CaLamViec> update(@PathVariable Integer id, @RequestBody CaLamViec updated) {
        return repository.findById(id).map(e -> { updated.setCaID(e.getCaID()); return ResponseEntity.ok(repository.save(updated)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id); return ResponseEntity.noContent().build();
    }
}
