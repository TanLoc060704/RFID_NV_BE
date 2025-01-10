package namviet.rfid_api.serviceImpl;

import lombok.RequiredArgsConstructor;
import namviet.rfid_api.dto.DonHangDTO;
import namviet.rfid_api.entity.DonHang;
import namviet.rfid_api.mapper.DonHangMapper;
import namviet.rfid_api.repository.DonHangRepository;
import namviet.rfid_api.service.DonHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DonHangServiceImpl implements DonHangService {

    private final DonHangRepository donHangRepository;
    private final DonHangMapper donHangMapper;

    @Override
    public List<DonHangDTO> findAll() {
        List<DonHang> list = donHangRepository.findAll();
        return list.stream()
                .map(donHang -> donHangMapper.toDto(donHang))
                .toList();
    }

    @Override
    @Transactional
    public DonHangDTO taoDonHang(DonHangDTO donHangDTO) {
        try {
            DonHang donHang = donHangMapper.toEntity(donHangDTO);
            DonHang savedDonHang = donHangRepository.save(donHang);
            return donHangMapper.toDto(savedDonHang);
        } catch (DataAccessException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi kết nối cơ sở dữ liệu hoặc thao tác lưu đơn hàng không thành công", e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Dữ liệu đơn hàng không hợp lệ", e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi không xác định khi tạo đơn hàng", e);
        }
    }

    @Override
    public List<DonHangDTO> timDonHangTheoMaLenh(String maLenh) {
        List<DonHang> dsDonHang = donHangRepository.findByMaLenhContaining(maLenh);
        return dsDonHang.stream()
                .map(donHangMapper::toDto)
                .toList();
    }
}
