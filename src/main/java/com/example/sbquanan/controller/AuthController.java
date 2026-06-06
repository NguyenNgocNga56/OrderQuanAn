package com.example.sbquanan.controller;

import com.example.sbquanan.entity.NhanVien;
import com.example.sbquanan.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private NhanVienRepository nhanVienRepository;

    private static final ConcurrentHashMap<String, Long> tokenStore = new ConcurrentHashMap<>();

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        Map<String, Object> response = new HashMap<>();

        String email = body.get("email") != null ? body.get("email").trim() : null;
        String password = body.get("password") != null ? body.get("password").trim() : null;

        if (email == null || password == null || email.isBlank() || password.isBlank()) {
            response.put("success", false);
            response.put("message", "Vui long nhap day du email va mat khau.");
            return ResponseEntity.badRequest().body(response);
        }

        Optional<NhanVien> optNhanVien = nhanVienRepository.findByEmailIgnoreCase(email);
        if (optNhanVien.isEmpty()) {
            response.put("success", false);
            response.put("message", "Email hoac mat khau khong dung.");
            return ResponseEntity.status(401).body(response);
        }

        NhanVien nhanVien = optNhanVien.get();
        if (!password.equals(nhanVien.getPassword() != null ? nhanVien.getPassword().trim() : null)) {
            response.put("success", false);
            response.put("message", "Email hoac mat khau khong dung.");
            return ResponseEntity.status(401).body(response);
        }

        if (!nhanVien.isTrangThai()) {
            response.put("success", false);
            response.put("message", "Tai khoan da bi vo hieu hoa.");
            return ResponseEntity.status(403).body(response);
        }

        if (!isAdminRole(nhanVien.getChucVu())) {
            response.put("success", false);
            response.put("message", "Tai khoan nay khong co quyen quan tri.");
            return ResponseEntity.status(403).body(response);
        }

        String token = UUID.randomUUID().toString();
        tokenStore.put(token, nhanVien.getId());

        response.put("success", true);
        response.put("message", "Dang nhap thanh cong.");
        response.put("token", token);
        response.put("hoTen", nhanVien.getHoTen());
        response.put("chucVu", nhanVien.getChucVu());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> response = new HashMap<>();

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenStore.remove(token);
        }

        response.put("success", true);
        response.put("message", "Dang xuat thanh cong.");
        return ResponseEntity.ok(response);
    }

    public static boolean isValidToken(String token) {
        return token != null && tokenStore.containsKey(token);
    }

    private boolean isAdminRole(String chucVu) {
        if (chucVu == null) return false;
        String role = Normalizer.normalize(chucVu.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replace('Đ', 'D')
                .replace('đ', 'd')
                .toUpperCase();
        return role.equals("ADMIN")
                || role.equals("QUAN_LY")
                || role.equals("QUAN LY")
                || role.equals("CHU QUAN")
                || role.equals("CHU_QUAN");
    }
}
