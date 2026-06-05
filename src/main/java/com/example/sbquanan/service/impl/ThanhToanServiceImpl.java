package com.example.sbquanan.service.impl;

import com.example.sbquanan.dto.ThanhToanRequest;
import com.example.sbquanan.dto.ThanhToanResponse;
import com.example.sbquanan.entity.HoaDon;
import com.example.sbquanan.entity.KhachHang;
import com.example.sbquanan.entity.ThanhToan;
import com.example.sbquanan.enums.PhuongThucThanhToan;
import com.example.sbquanan.enums.TrangThaiDonHang;
import com.example.sbquanan.enums.TrangThaiThanhToan;
import com.example.sbquanan.exception.ResourceNotFoundException;
import com.example.sbquanan.repository.HoaDonRepository;
import com.example.sbquanan.repository.KhachHangRepository;
import com.example.sbquanan.repository.ThanhToanRepository;
import com.example.sbquanan.service.ThanhToanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ThanhToanServiceImpl implements ThanhToanService {

    @Autowired private ThanhToanRepository repository;
    @Autowired private HoaDonRepository    hoaDonRepository;
    @Autowired private KhachHangRepository khachHangRepository;

    @Override
    public List<ThanhToan> getAll() { return repository.findAll(); }

    @Override
    public Optional<ThanhToan> getById(Long id) { return repository.findById(id); }

    @Override
    public ThanhToanResponse thanhToanDonHang(Long donHangID, ThanhToanRequest request) {
        HoaDon hoaDon = hoaDonRepository.findByDonHang_DonHangID(donHangID)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy hóa đơn cho đơn hàng #" + donHangID));

        TrangThaiDonHang trangThai = hoaDon.getDonHang().getTrangThai();

        // CHECK TRẠNG THÁI — chỉ cho thanh toán khi DA_PHUC_VU
        if (trangThai != TrangThaiDonHang.DA_PHUC_VU) {
            String msg = switch (trangThai) {
                case DA_HUY       -> "Không thể thanh toán — đơn hàng đã bị hủy.";
                case CHO_XAC_NHAN -> "Không thể thanh toán — đơn hàng chưa được xác nhận.";
                case DANG_NAU     -> "Không thể thanh toán — đơn hàng đang được chuẩn bị.";
                case HOAN_THANH   -> "Đơn hàng này đã hoàn thành trước đó.";
                default           -> "Trạng thái đơn hàng không hợp lệ để thanh toán: " + trangThai;
            };
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
        }

        // CHECK đã thanh toán thành công chưa
        if (repository.existsByHoaDon_HoaDonIDAndTrangThai(
                hoaDon.getHoaDonID(), TrangThaiThanhToan.THANH_CONG)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Đơn hàng này đã được thanh toán.");
        }

        PhuongThucThanhToan phuongThuc = request != null ? request.getPhuongThuc() : null;
        if (phuongThuc == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Vui lòng chọn phương thức thanh toán.");
        }

        double soTienPhaiTra = tinhSoTienPhaiTra(hoaDon);
        double soTien = request.getSoTien() != null ? request.getSoTien() : soTienPhaiTra;

        if (soTien <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Số tiền thanh toán phải lớn hơn 0.");
        }
        if (Double.compare(soTien, soTienPhaiTra) != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Số tiền thanh toán không khớp với hóa đơn.");
        }

        ThanhToan thanhToan = new ThanhToan();
        thanhToan.setHoaDon(hoaDon);
        thanhToan.setPhuongThuc(phuongThuc);
        thanhToan.setSoTien(soTien);
        thanhToan.setTrangThai(TrangThaiThanhToan.THANH_CONG);
        thanhToan.setThoiGian(LocalDateTime.now());

        ThanhToan saved = repository.save(thanhToan);
        int diemCong = congDiemSauThanhToan(hoaDon, soTienPhaiTra);
        return toResponse(saved, soTienPhaiTra, diemCong);
    }

    @Override
    public ThanhToan create(ThanhToan entity) { return repository.save(entity); }

    @Override
    public ThanhToan update(Long id, ThanhToan updated) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setPhuongThuc(updated.getPhuongThuc());
                    existing.setSoTien(updated.getSoTien());
                    existing.setTrangThai(updated.getTrangThai());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Thanh toán không tồn tại với id: " + id));
    }

    private double tinhSoTienPhaiTra(HoaDon hoaDon) {
        double thanhTien = hoaDon.getThanhTien();
        if (thanhTien > 0) return thanhTien;
        return Math.max(hoaDon.getTongTien() - hoaDon.getGiamGia(), 0);
    }

    private int congDiemSauThanhToan(HoaDon hoaDon, double soTienPhaiTra) {
        KhachHang khachHang = hoaDon.getDonHang().getKhachHang();
        if (khachHang == null) return 0;

        int diemCong = (int) (soTienPhaiTra / 10000);
        if (diemCong <= 0) return 0;

        khachHang.setDiemTichLuy(khachHang.getDiemTichLuy() + diemCong);
        khachHang.capNhatHangKhachHang();
        khachHangRepository.save(khachHang);
        return diemCong;
    }

    private ThanhToanResponse toResponse(ThanhToan thanhToan, double soTienPhaiTra, int diemCong) {
        HoaDon hoaDon = thanhToan.getHoaDon();
        ThanhToanResponse response = new ThanhToanResponse();
        response.setThanhToanID(thanhToan.getThanhToanID());
        response.setHoaDonID(hoaDon.getHoaDonID());
        response.setDonHangID(hoaDon.getDonHang().getDonHangID());
        response.setPhuongThuc(thanhToan.getPhuongThuc().name());
        response.setTrangThai(thanhToan.getTrangThai().name());
        response.setTongTien(hoaDon.getTongTien());
        response.setGiamGia(hoaDon.getGiamGia());
        response.setSoTien(soTienPhaiTra);
        response.setDiemCong(diemCong);
        response.setThoiGian(thanhToan.getThoiGian());
        return response;
    }
}
 