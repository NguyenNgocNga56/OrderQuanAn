package com.example.sbquanan.controller;

import com.example.sbquanan.entity.Ban;
import com.example.sbquanan.repository.BanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ban")
public class BanController {
    @Autowired private BanRepository repository;

    @GetMapping public List<Ban> getAll() { return repository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Ban> getById(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping public Ban create(@RequestBody Ban e) { return repository.save(e); }

    @PutMapping("/{id}")
    public ResponseEntity<Ban> update(@PathVariable Long id, @RequestBody Ban updated) {
        return repository.findById(id).map(e -> { updated.setBanID(e.getBanID()); return ResponseEntity.ok(repository.save(updated)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id); return ResponseEntity.noContent().build();
    }
}
