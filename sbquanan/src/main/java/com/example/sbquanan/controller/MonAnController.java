package com.example.sbquanan.controller;

import com.example.sbquanan.entity.MonAn;
import com.example.sbquanan.service.MonAnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monan")
public class MonAnController {

    @Autowired
    private MonAnService monAnService;

    @GetMapping
    public List<MonAn> getAll() {
        return monAnService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonAn> getById(@PathVariable Long id) {
        return monAnService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/menu/{menuId}")
    public List<MonAn> getByMenu(@PathVariable Long menuId) {
        return monAnService.getByMenu(menuId);
    }

    @PostMapping
    public MonAn create(@RequestBody MonAn monAn) {
        return monAnService.create(monAn);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MonAn> update(@PathVariable Long id, @RequestBody MonAn updated) {
        try {
            MonAn result = monAnService.update(id, updated);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            monAnService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
