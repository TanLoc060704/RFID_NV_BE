package namviet.rfid_api.service;

import namviet.rfid_api.dto.DonHangDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DonHangService {

    List<DonHangDTO> findAll();

    DonHangDTO taoDonHang(DonHangDTO donHangDTO);

    List<DonHangDTO> timDonHangTheoMaLenh(String maLenh);
}
