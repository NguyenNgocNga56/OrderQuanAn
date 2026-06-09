package com.example.sbquanan.controller;

import com.example.sbquanan.entity.NhanVien;
import com.example.sbquanan.repository.NhanVienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/migrate")
@RequiredArgsConstructor
public class MigrateController {

    private final NhanVienRepository nhanVienRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/hash-passwords")
    public ResponseEntity<String> hashPasswords() {
        List<NhanVien> list = nhanVienRepository.findAll();
        int count = 0;
        for (NhanVien nv : list) {
            String pw = nv.getPassword();
            if (pw != null && !pw.startsWith("$2a$")) {
                nv.setPassword(passwordEncoder.encode(pw));
                nhanVienRepository.save(nv);
                count++;
            }
        }
        return ResponseEntity.ok("Đã hash " + count + " mật khẩu. Hãy xóa MigrateController ngay!");
    }
}