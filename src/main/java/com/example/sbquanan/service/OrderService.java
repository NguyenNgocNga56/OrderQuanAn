package com.example.sbquanan.service;

import com.example.sbquanan.dto.OrderRequest;
import com.example.sbquanan.dto.OrderResponse;
import com.example.sbquanan.entity.*;
import com.example.sbquanan.enums.TrangThaiDonHang;
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

    // POST /orders
    @Transactional
    public OrderResponse createOrder(OrderRequest req) {

        if (req.getItems() == null || req.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Gio hang rong, khong the tao don.");
        }

        // 1. Tao DonHang
        DonHang donHang = new DonHang();
        donHang.setTrangThai(TrangThaiDonHang.CHO_XAC_NHAN);
        donHang.setNgayDat(LocalDateTime.now());

        if (req.getKhachHangId() != null) {
            KhachHang kh = khachHangRepo.findById(req.getKhachHangId().longValue())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Khong tim thay khach hang"));
            donHang.setKhachHang(kh);
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
        hoaDon.setGiamGia(0);
        hoaDon.setNgayLap(LocalDateTime.now());
        hoaDonRepo.save(hoaDon);

        return buildResponse(donHang, chiTiets);
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
        res.setTrangThai(dh.getTrangThai() != null ? dh.getTrangThai().name() : null);
        res.setTongTien(dh.getTongTien());
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
}
