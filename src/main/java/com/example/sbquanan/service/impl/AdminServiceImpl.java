package com.example.sbquanan.service.impl;

import com.example.sbquanan.dto.DashboardDTO;
import com.example.sbquanan.dto.ThongKeDTO;
import com.example.sbquanan.entity.Ban;
import com.example.sbquanan.entity.ChiTietDonHang;
import com.example.sbquanan.entity.DonHang;
import com.example.sbquanan.entity.HoaDon;
import com.example.sbquanan.enums.TrangThaiDonHang;
import com.example.sbquanan.exception.ResourceNotFoundException;
import com.example.sbquanan.repository.BanRepository;
import com.example.sbquanan.repository.ChiTietDonHangRepository;
import com.example.sbquanan.repository.DonHangRepository;
import com.example.sbquanan.repository.HoaDonRepository;
import com.example.sbquanan.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired private DonHangRepository        donHangRepo;
    @Autowired private ChiTietDonHangRepository chiTietRepo;
    @Autowired private HoaDonRepository         hoaDonRepo;
    @Autowired private BanRepository            banRepo;

    @Override
    public DashboardDTO getDashboard() {
        List<DonHang> donHangs = donHangRepo.findAllWithDetails();

        List<Long> ids = donHangs.stream()
                .map(DonHang::getDonHangID)
                .collect(Collectors.toList());

        Map<Long, List<ChiTietDonHang>> chiTietMap = chiTietRepo
                .findByDonHang_DonHangIDIn(ids).stream()
                .collect(Collectors.groupingBy(
                        ct -> ct.getDonHang().getDonHangID()));

        Map<Long, HoaDon> hoaDonMap = hoaDonRepo
                .findByDonHang_DonHangIDIn(ids).stream()
                .collect(Collectors.toMap(
                        hd -> hd.getDonHang().getDonHangID(), hd -> hd));

        List<DashboardDTO.DonHangDTO> list = donHangs.stream()
                .map(dh -> toDonHangDTO(dh,
                        chiTietMap.getOrDefault(dh.getDonHangID(), List.of()),
                        hoaDonMap.get(dh.getDonHangID())))
                .collect(Collectors.toList());

        DashboardDTO.TongQuan tq = new DashboardDTO.TongQuan();
        tq.setTongDon(list.size());
        tq.setDonChoXacNhan(count(list, "CHO_XAC_NHAN"));
        tq.setDonDangNau(count(list, "DANG_NAU"));
        tq.setDonDaPhucVu(count(list, "DA_PHUC_VU"));
        tq.setDonHoanThanh(count(list, "HOAN_THANH"));
        tq.setDonDaHuy(count(list, "DA_HUY"));
        tq.setDoanhThuHomNay(donHangRepo.doanhThuTuNgay(
                LocalDate.now().atStartOfDay()));

        DashboardDTO dto = new DashboardDTO();
        dto.setTongQuan(tq);
        dto.setDonHangs(list);
        return dto;
    }

    @Override
    public DashboardDTO.DonHangDTO capNhatTrangThai(Long donHangID,
                                                     TrangThaiDonHang trangThaiMoi) {
        DonHang donHang = donHangRepo.findById(donHangID)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Khong tim thay don hang #" + donHangID));

        validateChuyenTrangThai(donHang.getTrangThai(), trangThaiMoi);

        donHang.setTrangThai(trangThaiMoi);
        donHangRepo.save(donHang);

        // Tu dong chuyen trang thai ban khi don HOAN_THANH hoac DA_HUY
        if ((trangThaiMoi == TrangThaiDonHang.HOAN_THANH
                || trangThaiMoi == TrangThaiDonHang.DA_HUY)
                && donHang.getBan() != null) {
            Ban ban = donHang.getBan();
            if ("CO_KHACH".equals(ban.getTrangThai())) {
                ban.setTrangThai("DEP_BAN");
                banRepo.save(ban);
            }
        }

        List<ChiTietDonHang> chiTiet = chiTietRepo.findByDonHang_DonHangID(donHangID);
        HoaDon hoaDon = hoaDonRepo.findByDonHang_DonHangID(donHangID).orElse(null);
        return toDonHangDTO(donHang, chiTiet, hoaDon);
    }

    @Override
    public ThongKeDTO getThongKe(LocalDateTime from, LocalDateTime to) {
        List<DonHang> donHangs = donHangRepo.findByNgayDatBetween(from, to);

        long tongDon      = donHangs.size();
        long donHoanThanh = donHangs.stream()
                .filter(d -> d.getTrangThai() == TrangThaiDonHang.HOAN_THANH).count();
        long donDaHuy     = donHangs.stream()
                .filter(d -> d.getTrangThai() == TrangThaiDonHang.DA_HUY).count();

        List<Long> idHoanThanh = donHangs.stream()
                .filter(d -> d.getTrangThai() == TrangThaiDonHang.HOAN_THANH)
                .map(DonHang::getDonHangID)
                .collect(Collectors.toList());

        double doanhThu = idHoanThanh.isEmpty() ? 0 :
                hoaDonRepo.findByDonHang_DonHangIDIn(idHoanThanh).stream()
                        .mapToDouble(HoaDon::getThanhTien)
                        .sum();

        long soKhach = donHangs.stream()
                .filter(d -> d.getKhachHang() != null)
                .map(d -> d.getKhachHang().getId())
                .distinct().count();

        return new ThongKeDTO(tongDon, donHoanThanh, donDaHuy, doanhThu, soKhach);
    }


    private void validateChuyenTrangThai(TrangThaiDonHang hienTai,
                                          TrangThaiDonHang moi) {
        boolean hopLe = switch (hienTai) {
            case CHO_XAC_NHAN -> moi == TrangThaiDonHang.DANG_NAU
                                  || moi == TrangThaiDonHang.DA_HUY;
            case DANG_NAU     -> moi == TrangThaiDonHang.DA_PHUC_VU
                                  || moi == TrangThaiDonHang.DA_HUY;
            case DA_PHUC_VU   -> moi == TrangThaiDonHang.HOAN_THANH
                                  || moi == TrangThaiDonHang.DA_HUY;
            default           -> false;
        };
        if (!hopLe) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Khong the chuyen tu " + hienTai + " sang " + moi
                    + ". Flow hop le: CHO_XAC_NHAN -> DANG_NAU -> DA_PHUC_VU -> HOAN_THANH / DA_HUY");
        }
    }

    private DashboardDTO.DonHangDTO toDonHangDTO(DonHang dh,
                                                   List<ChiTietDonHang> chiTiets,
                                                   HoaDon hoaDon) {
        DashboardDTO.DonHangDTO dto = new DashboardDTO.DonHangDTO();
        dto.setDonHangID(dh.getDonHangID());
        dto.setTrangThai(dh.getTrangThai().name());
        dto.setNgayDat(dh.getNgayDat());
        dto.setTongTien(dh.getTongTien());

        if (dh.getBan() != null) dto.setTenBan(dh.getBan().getTenBan());

        if (dh.getKhachHang() != null) {
            dto.setTenKhachHang(dh.getKhachHang().getHoTen());
            dto.setSdtKhach(dh.getKhachHang().getSdt());
        }
        if (dh.getNhanVien() != null) {
            dto.setTenNhanVien(dh.getNhanVien().getHoTen());
        }

        if (hoaDon != null) {
            dto.setGiamGia(hoaDon.getGiamGia());
            dto.setThanhTien(hoaDon.getThanhTien());
        } else {
            dto.setGiamGia(0);
            dto.setThanhTien(dh.getTongTien());
        }

        List<DashboardDTO.MonDTO> monList = chiTiets.stream().map(ct -> {
            DashboardDTO.MonDTO m = new DashboardDTO.MonDTO();
            if (ct.getMonAn() != null) m.setTenMon(ct.getMonAn().getTenMon());
            m.setSoLuong(ct.getSoLuong());
            m.setDonGia(ct.getGiaBan());
            m.setThanhTien(ct.getGiaBan() * ct.getSoLuong());
            return m;
        }).collect(Collectors.toList());

        dto.setChiTiet(monList);
        return dto;
    }

    private long count(List<DashboardDTO.DonHangDTO> list, String trangThai) {
        return list.stream()
                .filter(d -> trangThai.equals(d.getTrangThai()))
                .count();
    }
}