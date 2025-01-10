package namviet.rfid_api.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import namviet.rfid_api.dto.DonHangSanPhamDTO;
import namviet.rfid_api.dto.SanPhamDTO;
import namviet.rfid_api.entity.DonHang;
import namviet.rfid_api.entity.DonHangSanPham;
import namviet.rfid_api.entity.SanPham;
import namviet.rfid_api.mapper.*;
import namviet.rfid_api.repository.DonHangRepository;
import namviet.rfid_api.repository.DonHangSanPhamRepository;
import namviet.rfid_api.repository.UpcRepository;
import namviet.rfid_api.service.DonHangSanPhamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class DonHangSanPhamServiceImpl implements DonHangSanPhamService {

    final DonHangSanPhamRepository donHangSanPhamRepository;
    final DonHangSanPhamMapper donHangSanPhamMapper;
    final DonHangRepository donHangRepository;
    final SanPhamMapper sanPhamMapper;
    final DonHangMapper donHangMapper;
    final ThietBiMapper thietBiMapper;
    final NhanVienMapper nhanVienMapper;
    final UpcRepository upcRepository;

    @Override
    public List<DonHangSanPhamDTO> dSSanPhamTheoDonHang(String maLenh) {
        List<DonHangSanPham> dSDonHangSP = donHangSanPhamRepository.findByDonHangMaLenhContaining(maLenh);
        return dSDonHangSP.stream()
                .map(donHangSanPhamMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DonHangSanPhamDTO> dSTatCaSanPham() {
        List<DonHangSanPham> dSTatCaSP = donHangSanPhamRepository.findAll();
        return dSTatCaSP.stream()
                .map(donHangSanPhamMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DonHangSanPhamDTO capNhatDonHangSanPham(DonHangSanPhamDTO donHangSanPhamDTO) {
        if (donHangSanPhamDTO == null || donHangSanPhamDTO.getDonHangSanPhamId() == null) {
            throw new IllegalArgumentException("Dữ liệu đơn hàng hoặc ID không hợp lệ");
        }
        Optional<DonHangSanPham> optionalDonHang = donHangSanPhamRepository.findByDonHangSanPhamId(donHangSanPhamDTO.getDonHangSanPhamId());
        if (optionalDonHang.isEmpty()) {
            throw new EntityNotFoundException("Không tìm thấy đơn hàng với ID: " + donHangSanPhamDTO.getDonHangSanPhamId());
        }
        DonHangSanPham donHangSanPham = optionalDonHang.get();
        DonHang donHang = donHangSanPham.getDonHang();

        donHangSanPham.setSanPham(sanPhamMapper.toEntity(donHangSanPhamDTO.getSanPham()));
        donHangSanPham.setSoLuong(donHangSanPhamDTO.getSoLuong());
        donHangSanPham.setDonHang(donHangMapper.toEntity(donHangSanPhamDTO.getDonHang()));

        SanPhamDTO sanPhamDTO = sanPhamMapper.toDto(donHangSanPham.getSanPham());
        donHangSanPham.setTenFile(taoTenFile(sanPhamDTO));

        donHangSanPham = donHangSanPhamRepository.save(donHangSanPham);

        return donHangSanPhamMapper.toDTO(donHangSanPham);
    }

    @Override
    @Transactional
    public List<DonHangSanPhamDTO> themSPVaoDonHangNew(List<DonHangSanPhamDTO> dsDonHangSanPhamDTO) {
        List<DonHangSanPhamDTO> result = new ArrayList<>();
        DonHang donHang = donHangRepository.findByDonHangId(dsDonHangSanPhamDTO.get(0).getDonHang().getDonHangId());
        if (donHang == null) {
            return null;
        }

        for (DonHangSanPhamDTO donHangSanPhamDTO : dsDonHangSanPhamDTO) {
            SanPham sanPham = sanPhamMapper.toEntity(donHangSanPhamDTO.getSanPham());

            DonHangSanPham donHangSanPham = new DonHangSanPham();
            donHangSanPham.setDonHang(donHang);
            donHangSanPham.setSanPham(sanPham);
            donHangSanPham.setTenFile(taoTenFile(sanPhamMapper.toDto(sanPham)));
            donHangSanPham.setSoLuong(donHangSanPhamDTO.getSoLuong());

            donHangSanPhamRepository.save(donHangSanPham);

            DonHangSanPhamDTO savedDonHangSanPhamDTO = donHangSanPhamMapper.toDTO(donHangSanPham);
            result.add(savedDonHangSanPhamDTO);
        }

        return result;
    }

    @Override
    public Optional<DonHangSanPhamDTO> timDonHangSanPham(int donHangSanPhamId) {
        if(donHangSanPhamId <= 0){
            return Optional.empty();
        }
        return donHangSanPhamRepository.findByDonHangSanPhamId(donHangSanPhamId)
                .map(donHangSanPhamMapper::toDTO);
    }

    @Override
    public Optional<List<DonHangSanPhamDTO>> timDonHangTheoDonHangId(int donHangId) {
        if(donHangId <= 0){
            return Optional.empty();
        }
        List<DonHangSanPham> dsDonHangDonHangId = donHangSanPhamRepository.findByDonHangDonHangId(donHangId);
        if(dsDonHangDonHangId.isEmpty()){
            return Optional.empty();
        }
        List<DonHangSanPhamDTO> dsDonHangSanPhamDTO = dsDonHangDonHangId.stream()
                .map(donHangSanPhamMapper::toDTO)
                .collect(Collectors.toList());

        return Optional.of(dsDonHangSanPhamDTO);
    }


    private String taoTenFile(SanPhamDTO sanPham) {
        if (sanPham == null || sanPham.getSku() == null) {
            throw new IllegalArgumentException("SanPham or SKU cannot be null");
        }

        Date date = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");

        String sku = sanPham.getSku();
        String ngayThang = dateFormat.format(date);
        String thoiGian = timeFormat.format(date);

        String tenFile = sku +"-"+ ngayThang + "-" + thoiGian+".xlsx";


        return tenFile;
    }


}
