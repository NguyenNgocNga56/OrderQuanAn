package com.example.sbquanan.service;

import com.example.sbquanan.dto.OrderRequest;
import com.example.sbquanan.dto.OrderResponse;
import com.example.sbquanan.entity.*;
import com.example.sbquanan.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired private DonHangRepository        donHangRepo;
    @Autowired private ChiTietDonHangRepository chiTietRepo;
    @Autowired private MonAnRepository          monAnRepo;
    @Autowired private KhachHangRepository      khachHangRepo;
    @Autowired private NhanVienRepository       nhanVienRepo;
    @Autowired private HoaDonRepository         hoaDonRepo;
    @Autowired private KhuyenMaiRepository      khuyenMaiRepo;

    // POST /orders
    @Transactional
    public OrderResponse createOrder(OrderRequest req) {

        if (req.getItems() == null || req.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Gio hang rong, khong the tao don.");
        }

        // 1. Tao DonHang
        DonHang donHang = new DonHang();
        donHang.setTrangThai("Chờ xử lý");
        donHang.setNgayDat(LocalDateTime.now());

        KhachHang khachHang = null;
        if (req.getSdtKhachHang() != null && !req.getSdtKhachHang().isBlank()) {
            // Khách nhập SĐT → bắt buộc phải tồn tại trong DB để tích điểm
            khachHang = khachHangRepo.findBySdt(req.getSdtKhachHang().trim())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Khong tim thay khach hang voi SDT: " + req.getSdtKhachHang().trim()));
        } else if (req.getKhachHangId() != null) {
            khachHang = khachHangRepo.findById(req.getKhachHangId().longValue())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Khong tim thay khach hang"));
        }
        if (khachHang != null) {
            donHang.setKhachHang(khachHang);
        }
        if (req.getNhanVienId() != null) {
            NhanVien nv = nhanVienRepo.findById(req.getNhanVienId().longValue())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Khong tim thay nhan vien"));
            donHang.setNhanVien(nv);
        }

        donHang = donHangRepo.save(donHang);

        // 2. Tao ChiTietDonHang
        List<ChiTietDonHang> chiTiets = new ArrayList<>();
        double tongTien = 0;

        for (OrderRequest.OrderItemRequest item : req.getItems()) {
            if (item.getMonId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Mon trong gio hang khong hop le.");
            }
            if (item.getSoLuong() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "So luong mon phai lon hon 0.");
            }

            MonAn mon = monAnRepo.findById(item.getMonId().longValue())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Khong tim thay mon: " + item.getMonId()));

            ChiTietDonHang ct = new ChiTietDonHang();
            ct.setDonHang(donHang);
            ct.setMonAn(mon);
            ct.setSoLuong(item.getSoLuong());
            ct.setGiaBan(mon.giaBan());
            ct.setTongTien(mon.giaBan() * item.getSoLuong());
            chiTiets.add(ct);
            tongTien += mon.giaBan() * item.getSoLuong();
        }

        chiTietRepo.saveAll(chiTiets);

        // 3. Cap nhat TongTien
        donHang.setTongTien(tongTien);
        donHang = donHangRepo.save(donHang);

        // 4. Tao HoaDon
        HoaDon hoaDon = new HoaDon();
        hoaDon.setDonHang(donHang);
        hoaDon.setTongTien(tongTien);
        apDungKhuyenMai(req, hoaDon, tongTien);
        hoaDon.setNgayLap(LocalDateTime.now());
        hoaDon = hoaDonRepo.save(hoaDon);

        OrderResponse response = buildResponse(donHang, chiTiets);
        response.setHoaDonID(hoaDon.getHoaDonID());
        response.setGiamGia(hoaDon.getGiamGia());
        response.setTongTien(tinhThanhTien(hoaDon));
        return response;
    }

    // GET /orders
    @Transactional(readOnly = true)
    public List<OrderResponse> getAll() {
        return donHangRepo.findAll().stream()
                .map(dh -> buildResponse(dh,
                        chiTietRepo.findByDonHangID(dh.getDonHangID())))
                .collect(Collectors.toList());
    }

    // GET /orders/{id}
    @Transactional(readOnly = true)
    public OrderResponse getById(Long id) {   // FIX: Long thay vi Integer
        DonHang dh = donHangRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Khong tim thay don hang #" + id));
        List<ChiTietDonHang> ct = chiTietRepo.findByDonHangID(id);
        return buildResponse(dh, ct);
    }

    // Helper
    private OrderResponse buildResponse(DonHang dh, List<ChiTietDonHang> chiTiets) {
        OrderResponse res = new OrderResponse();
        // FIX: donHangID la Long, cast sang Integer cho DTO
        res.setDonHangID(dh.getDonHangID() != null ? dh.getDonHangID().intValue() : null);
        hoaDonRepo.findByDonHang_DonHangID(dh.getDonHangID())
                .ifPresent(hoaDon -> {
                    res.setHoaDonID(hoaDon.getHoaDonID());
                    res.setGiamGia(hoaDon.getGiamGia());
                    res.setTongTien(tinhThanhTien(hoaDon));
                });
        res.setTrangThai(dh.getTrangThai());
        if (res.getHoaDonID() == null) {
            res.setTongTien(dh.getTongTien());
        }
        res.setNgayDat(dh.getNgayDat());

        if (dh.getKhachHang() != null) res.setTenKhachHang(dh.getKhachHang().getHoTen());
        if (dh.getNhanVien()  != null) res.setTenNhanVien(dh.getNhanVien().getHoTen());

        List<OrderResponse.ItemDetail> details = chiTiets.stream().map(ct -> {
            OrderResponse.ItemDetail d = new OrderResponse.ItemDetail();
            if (ct.getMonAn() != null) {
                d.setTenMon(ct.getMonAn().getTenMon());
                d.setHinhAnh(ct.getMonAn().getHinhAnh());
                d.setDonGia(ct.getMonAn().giaBan());
            }
            d.setSoLuong(ct.getSoLuong());
            d.setThanhTien(ct.getGiaBan() * ct.getSoLuong());
            return d;
        }).collect(Collectors.toList());

        res.setChiTiet(details);
        return res;
    }

    private void apDungKhuyenMai(OrderRequest req, HoaDon hoaDon, double tongTien) {
        KhuyenMai khuyenMai = timKhuyenMai(req);

        if (khuyenMai == null) {
            hoaDon.setGiamGia(0);
            return;
        }
        if (!khuyenMai.hopLe()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ma khuyen mai da het han hoac chua ap dung");
        }

        double giamGia = switch (khuyenMai.getLoaiKhuyenMai()) {
            case PHAN_TRAM -> tongTien * khuyenMai.getGiaTri() / 100;
            case GIAM_TIEN_MAT, SO_TIEN -> khuyenMai.getGiaTri();
        };

        hoaDon.setKhuyenMai(khuyenMai);
        hoaDon.setGiamGia(Math.min(Math.max(giamGia, 0), tongTien));
    }

    private double tinhThanhTien(HoaDon hoaDon) {
        double thanhTien = hoaDon.getThanhTien();
        if (thanhTien > 0) return thanhTien;
        return Math.max(hoaDon.getTongTien() - hoaDon.getGiamGia(), 0);
    }

    private KhuyenMai timKhuyenMai(OrderRequest req) {
        if (req.getKhuyenMaiId() != null) {
            return khuyenMaiRepo.findById(req.getKhuyenMaiId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay khuyen mai"));
        }
        if (req.getMaKhuyenMai() != null && !req.getMaKhuyenMai().isBlank()) {
            return khuyenMaiRepo.findByTenKhuyenMaiIgnoreCase(req.getMaKhuyenMai().trim())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay ma khuyen mai"));
        }
        return null;
    }
}
