package com.example.sbquanan.controller;


import com.example.sbquanan.entity.NhanVien;
import com.example.sbquanan.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/migrate")
public class MigrateController {

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    
    @PostMapping("/hash-passwords")
    public ResponseEntity<String> hashPasswords() {
        List<NhanVien> list = nhanVienRepository.findAll();
        int count = 0;

        for (NhanVien nv : list) {
            // Chỉ hash nếu chưa hash (tránh double-hash)
            if (nv.getPassword() != null && !nv.getPassword().startsWith("$2a$")) {
                nv.setPassword(passwordEncoder.encode(nv.getPassword()));
                nhanVienRepository.save(nv);
                count++;
            }
        }

        return ResponseEntity.ok("Done: " + count + "/" + list.size() + " accounts migrated. Xóa file MigrateController.java đi!");
    }
}
