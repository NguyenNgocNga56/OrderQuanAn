package com.example.sbquanan.controller;

import com.example.sbquanan.entity.Menu;
import com.example.sbquanan.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    @Autowired private MenuRepository repository;

    @GetMapping public List<Menu> getAll() { return repository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> getById(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping public Menu create(@RequestBody Menu e) { return repository.save(e); }

    @PutMapping("/{id}")
    public ResponseEntity<Menu> update(@PathVariable Long id, @RequestBody Menu updated) {
        return repository.findById(id).map(e -> { updated.setMenuID(e.getMenuID()); return ResponseEntity.ok(repository.save(updated)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id); return ResponseEntity.noContent().build();
    }
}
