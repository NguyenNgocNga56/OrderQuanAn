package com.example.sbquanan.service;

import com.example.sbquanan.DTO.OrderRequest;
import com.example.sbquanan.DTO.OrderResponse;
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

    // ------------------------------------------------------------------ //
    //  POST /orders – tao don moi tu gio hang                             //
    // ------------------------------------------------------------------ //
    @Transactional
    public OrderResponse createOrder(OrderRequest req) {

        if (req.getItems() == null || req.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Gio hang rong, khong the tao don.");
        }

        // 1. Tao DonHang
        DonHang donHang = new DonHang();
        donHang.setTrangThai(TrangThaiDonHang.DA_NHAN_DON_HANG);
        donHang.setNgayDat(LocalDateTime.now());

        if (req.getKhachHangId() != null) {
            KhachHang kh = khachHangRepo.findById(req.getKhachHangId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Khong tim thay khach hang"));
            donHang.setKhachHang(kh);
        }
        if (req.getNhanVienId() != null) {
            NhanVien nv = nhanVienRepo.findById(req.getNhanVienId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Khong tim thay nhan vien"));
            donHang.setNhanVien(nv);
        }

        // Luu lan 1 de lay donHangID (can cho FK ChiTietDonHang)
        donHang = donHangRepo.save(donHang);

        // 2. Tao ChiTietDonHang
        List<ChiTietDonHang> chiTiets = new ArrayList<>();
        double tongTien = 0;

        for (OrderRequest.OrderItemRequest item : req.getItems()) {
            MonAn mon = monAnRepo.findById(item.getMonId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Khong tim thay mon: " + item.getMonId()));

            ChiTietDonHang ct = new ChiTietDonHang();
            ct.setDonHang(donHang);
            ct.setMonAn(mon);
            ct.setSoLuong(item.getSoLuong());
            ct.setDonGia(mon.getGia());
            chiTiets.add(ct);
            tongTien += mon.getGia() * item.getSoLuong();
        }

        chiTietRepo.saveAll(chiTiets);

        // 3. Cap nhat TongTien tren DonHang
        donHang.setTongTien(tongTien);
        donHang = donHangRepo.save(donHang);

        // 4. Tao HoaDon tu dong kem theo don hang
        HoaDon hoaDon = new HoaDon();
        hoaDon.setDonHang(donHang);
        hoaDon.setTongTien(tongTien);
        hoaDon.setGiamGia(0);
        hoaDon.setNgayLap(LocalDateTime.now());
        hoaDonRepo.save(hoaDon);

        // 5. Build response
        return buildResponse(donHang, chiTiets);
    }

    // ------------------------------------------------------------------ //
    //  GET /orders – lay tat ca don                                       //
    // ------------------------------------------------------------------ //
    @Transactional(readOnly = true)
    public List<OrderResponse> getAll() {
        return donHangRepo.findAll().stream()
                .map(dh -> buildResponse(dh,
                        chiTietRepo.findByDonHangID(dh.getDonHangID())))
                .collect(Collectors.toList());
    }

    // ------------------------------------------------------------------ //
    //  GET /orders/{id} – lay mot don theo ID                             //
    // ------------------------------------------------------------------ //
    @Transactional(readOnly = true)
    public OrderResponse getById(Integer id) {
        DonHang dh = donHangRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Khong tim thay don hang #" + id));
        List<ChiTietDonHang> ct = chiTietRepo.findByDonHangID(id);
        return buildResponse(dh, ct);
    }

    // ------------------------------------------------------------------ //
    //  Helper: DonHang + ChiTiet -> OrderResponse                        //
    // ------------------------------------------------------------------ //
    private OrderResponse buildResponse(DonHang dh, List<ChiTietDonHang> chiTiets) {
        OrderResponse res = new OrderResponse();
        res.setDonHangID(dh.getDonHangID());
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
                d.setDonGia(ct.getMonAn().getGia());
            }
            d.setSoLuong(ct.getSoLuong());
            d.setThanhTien(ct.getDonGia() * ct.getSoLuong());
            return d;
        }).collect(Collectors.toList());

        res.setChiTiet(details);
        return res;
    }
}
