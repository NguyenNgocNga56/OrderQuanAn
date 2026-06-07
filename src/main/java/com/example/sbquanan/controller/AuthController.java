package com.example.sbquanan.controller;

import com.example.sbquanan.dto.ApiResponse;
import com.example.sbquanan.entity.NhanVien;
import com.example.sbquanan.repository.NhanVienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final NhanVienRepository nhanVienRepository;

    // Của Ngà: Đổi thành static và lưu Token -> NhanVien ID (Long)
    private static final ConcurrentHashMap<String, Long> tokenStore = new ConcurrentHashMap<>();

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(
            @RequestBody Map<String, String> body) {

        String email    = body.get("email");
        String password = body.get("password");

        // Của Ngà: Thêm check isBlank() chặt chẽ hơn
        if (email == null || password == null || email.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Vui lòng nhập đầy đủ email và mật khẩu."));
        }

        // Của Ngà: Dùng Optional truy vấn DB
        Optional<NhanVien> optNhanVien = nhanVienRepository.findByEmail(email);

        if (optNhanVien.isEmpty()) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Email hoặc mật khẩu không đúng."));
        }

        NhanVien nhanVien = optNhanVien.get();

        // CỦA BẠN: So sánh mật khẩu trực tiếp (Plain-text), không BCrypt
        if (!password.equals(nhanVien.getPassword())) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Email hoặc mật khẩu không đúng."));
        }

        if (!nhanVien.isTrangThai()) {
            return ResponseEntity.status(403)
                    .body(ApiResponse.error("Tài khoản đã bị vô hiệu hóa."));
        }

        String token = UUID.randomUUID().toString();
        // Của Ngà: Lưu ID nhân viên vào tokenStore thay vì lưu email
        tokenStore.put(token, nhanVien.getId());

        // Của bạn: Giữ nguyên format ApiResponse
        return ResponseEntity.ok(ApiResponse.success(Map.of(
                "token",  token,
                "email",  nhanVien.getEmail(),
                "hoTen",  nhanVien.getHoTen(),
                "chucVu", nhanVien.getChucVu() != null ? nhanVien.getChucVu() : ""
        )));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @RequestHeader(value = "Authorization", required = false) String header) {

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            tokenStore.remove(token);
        }
        return ResponseEntity.ok(ApiResponse.success("Đăng xuất thành công."));
    }

    // Của Ngà: Hàm check token tĩnh cho các Filter gọi vào
    public static boolean isValidToken(String token) {
        return token != null && tokenStore.containsKey(token);
    }
}