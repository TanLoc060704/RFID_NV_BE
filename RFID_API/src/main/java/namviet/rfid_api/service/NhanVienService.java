package namviet.rfid_api.service;

import namviet.rfid_api.dto.NhanVienDTO;
import namviet.rfid_api.constant.ResponseObject;

import java.util.List;

public interface NhanVienService {
    ResponseObject<List<NhanVienDTO>> findAll();
    ResponseObject<NhanVienDTO> findById(Integer id);
    ResponseObject<NhanVienDTO> create(NhanVienDTO nhanVienDTO);
    ResponseObject<NhanVienDTO> update(Integer id, NhanVienDTO nhanVienDTO);
    ResponseObject<String> delete(Integer id);
}
