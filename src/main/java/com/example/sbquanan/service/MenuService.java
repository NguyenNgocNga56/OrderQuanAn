package com.example.sbquanan.service;

import com.example.sbquanan.entity.Menu;
import java.util.List;
import java.util.Optional;

public interface MenuService {
    List<Menu> getAll();
    Optional<Menu> getById(Long id);
    Menu create(Menu entity);
    Menu update(Long id, Menu updated);
    void delete(Long id);
}
