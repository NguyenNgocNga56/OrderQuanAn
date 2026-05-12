package com.example.sbquanan.controller;

import com.example.sbquanan.entity.KetCa;
import com.example.sbquanan.repository.KetCaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ketca")
public class KetCaController {
    @Autowired private KetCaRepository repository;

    @GetMapping public List<KetCa> getAll() { return repository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<KetCa> getById(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping public KetCa create(@RequestBody KetCa e) { return repository.save(e); }

    @PutMapping("/{id}")
    public ResponseEntity<KetCa> update(@PathVariable Long id, @RequestBody KetCa updated) {
        return repository.findById(id).map(e -> { updated.setKetCaID(e.getKetCaID()); return ResponseEntity.ok(repository.save(updated)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id); return ResponseEntity.noContent().build();
    }
}
