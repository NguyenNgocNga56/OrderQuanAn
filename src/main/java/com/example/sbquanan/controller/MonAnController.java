package com.example.sbquanan.controller;

import com.example.sbquanan.entity.MonAn;
import com.example.sbquanan.repository.MonAnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/monan")
public class MonAnController {
    @Autowired private MonAnRepository repository;

    @GetMapping public List<MonAn> getAll() { return repository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<MonAn> getById(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/menu/{menuId}")
    public List<MonAn> getByMenu(@PathVariable Long menuId) {
        return repository.findByMenu_MenuID(menuId);
    }

    @PostMapping public MonAn create(@RequestBody MonAn e) { return repository.save(e); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id); return ResponseEntity.noContent().build();
    }
}
