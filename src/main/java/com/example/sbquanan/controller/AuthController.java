package com.example.sbquanan.controller;

import com.example.sbquanan.dto.ApiResponse;
import com.example.sbquanan.entity.NhanVien;
import com.example.sbquanan.repository.NhanVienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private NhanVienRepository nhanVienRepository;

    private static final ConcurrentHashMap<String, Long> tokenStore = new ConcurrentHashMap<>();
    private final NhanVienRepository nhanVienRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConcurrentHashMap<String, String> tokenStore;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(
            @RequestBody Map<String, String> body) {

        String email    = body.get("email");
        String password = body.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Email và mật khẩu không được để trống"));
        }

        NhanVien nhanVien = nhanVienRepository.findByEmail(email).orElse(null);

        if (nhanVien == null || !passwordEncoder.matches(password, nhanVien.getPassword())) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Email hoặc mật khẩu không đúng"));
        }

        if (!nhanVien.isTrangThai()) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error("Tài khoản đã bị vô hiệu hóa"));
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
        tokenStore.put(token, email);
        tokenStore.put(token, nhanVien.getId());

        return ResponseEntity.ok(ApiResponse.success(Map.of(
            "token",  token,
            "email",  nhanVien.getEmail(),
            "hoTen",  nhanVien.getHoTen(),
            "chucVu", nhanVien.getChucVu() != null ? nhanVien.getChucVu() : ""
        )));
        response.put("success", true);
        response.put("message", "Dang nhap thanh cong.");
        response.put("token", token);
        response.put("hoTen", nhanVien.getHoTen());
        response.put("chucVu", nhanVien.getChucVu());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @RequestHeader(value = "Authorization", required = false) String header) {
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

        if (header != null && header.startsWith("Bearer ")) {
            tokenStore.remove(header.substring(7));
        }
        return ResponseEntity.ok(ApiResponse.success("Đăng xuất thành công"));
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