package com.example.sbquanan.service.impl;

import com.example.sbquanan.entity.Menu;
import com.example.sbquanan.exception.ResourceNotFoundException;
import com.example.sbquanan.repository.MenuRepository;
import com.example.sbquanan.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository repository;

    @Override
    public List<Menu> getAll() { return repository.findAll(); }

    @Override
    public Optional<Menu> getById(Long id) { return repository.findById(id); }

    @Override
    public Menu create(Menu entity) { return repository.save(entity); }

    @Override
    public Menu update(Long id, Menu updated) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Menu không tồn tại với id: " + id);
        updated.setMenuID(id);
        return repository.save(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException("Menu không tồn tại với id: " + id);
        repository.deleteById(id);
    }
}
