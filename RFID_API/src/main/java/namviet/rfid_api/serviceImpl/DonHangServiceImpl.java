package namviet.rfid_api.serviceImpl;

import lombok.RequiredArgsConstructor;
import namviet.rfid_api.dto.DonHangDTO;
import namviet.rfid_api.entity.DonHang;
import namviet.rfid_api.mapper.DonHangMapper;
import namviet.rfid_api.repository.DonHangRepository;
import namviet.rfid_api.service.DonHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DonHangServiceImpl implements DonHangService {

    private final DonHangRepository donHangRepository;
    private final DonHangMapper donHangMapper;

    @Override
    public List<DonHangDTO> findAll() {
        List<DonHang> list = donHangRepository.findAll();
        return list.stream()
                .map(donHangMapper::toDto)
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

    @Override
    public Optional<DonHangDTO> getDonHangById(Integer id) {
        return donHangRepository.findById(id).map(donHangMapper::toDto);
    }

    @Override
    public DonHangDTO updateDonHang(Integer id, DonHangDTO donHangDTO) {
        DonHang existing = donHangRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng"));
        DonHang updatedEntity = donHangMapper.toEntity(donHangDTO);
        updatedEntity.setDonHangId(existing.getDonHangId());
        return donHangMapper.toDto(donHangRepository.save(updatedEntity));
    }

    @Override
    public void deleteDonHang(Integer id) {
        if (!donHangRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn hàng");
        }
        donHangRepository.deleteById(id);
    }

    @Override
    public Page<DonHangDTO> getDonHangPagination(Pageable pageable) {
        return donHangRepository.findAll(pageable).map(donHangMapper::toDto);
    }

    @Override
    public Page<DonHangDTO> searchWithFTSService(String searchText, Pageable pageable) {
        Page<DonHang> entities = donHangRepository.searchWithFTS(searchText, pageable);
        return entities.map(donHangMapper::toDto);
    }
}
