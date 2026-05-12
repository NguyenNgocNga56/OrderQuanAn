package com.example.sbquanan.controller;

import com.example.sbquanan.entity.NhanVien;
import com.example.sbquanan.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // Lưu token tạm thời trong bộ nhớ (có thể dùng Redis hoặc DB sau này)
    private static final ConcurrentHashMap<String, Long> tokenStore = new ConcurrentHashMap<>();

    /**
     * POST /api/auth/login
     * Body: { "email": "...", "password": "..." }
     *
     * Lưu ý: hiện tại password được lưu thẳng (plain text).
     * Trong thực tế nên dùng BCrypt để mã hóa mật khẩu.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        Map<String, Object> response = new HashMap<>();

        String email    = body.get("email");
        String password = body.get("password");

        if (email == null || password == null || email.isBlank() || password.isBlank()) {
            response.put("success", false);
            response.put("message", "Vui lòng nhập đầy đủ email và mật khẩu.");
            return ResponseEntity.badRequest().body(response);
        }

        // Tìm nhân viên theo email
        Optional<NhanVien> optNhanVien = nhanVienRepository.findByEmail(email);

        if (optNhanVien.isEmpty()) {
            response.put("success", false);
            response.put("message", "Email hoặc mật khẩu không đúng.");
            return ResponseEntity.status(401).body(response);
        }

        NhanVien nhanVien = optNhanVien.get();

        // Kiểm tra mật khẩu (plain text — nên dùng BCrypt trong thực tế)
        // Giả sử trường password được lưu trong entity (sẽ thêm sau)
        // Tạm thời kiểm tra theo logic đơn giản
        if (!password.equals(nhanVien.getPassword())) {
            response.put("success", false);
            response.put("message", "Email hoặc mật khẩu không đúng.");
            return ResponseEntity.status(401).body(response);
        }

        // Kiểm tra trạng thái tài khoản
        if (!nhanVien.isTrangThai()) {
            response.put("success", false);
            response.put("message", "Tài khoản đã bị vô hiệu hóa.");
            return ResponseEntity.status(403).body(response);
        }

        // Tạo token đơn giản
        String token = UUID.randomUUID().toString();
        tokenStore.put(token, nhanVien.getId());

        response.put("success", true);
        response.put("message", "Đăng nhập thành công.");
        response.put("token", token);
        response.put("hoTen", nhanVien.getHoTen());
        response.put("chucVu", nhanVien.getChucVu());

        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/logout
     * Header: Authorization: Bearer <token>
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> response = new HashMap<>();

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenStore.remove(token);
        }

        response.put("success", true);
        response.put("message", "Đăng xuất thành công.");
        return ResponseEntity.ok(response);
    }

    /**
     * Kiểm tra token có hợp lệ không (dùng nội bộ).
     */
    public static boolean isValidToken(String token) {
        return token != null && tokenStore.containsKey(token);
    }
}
