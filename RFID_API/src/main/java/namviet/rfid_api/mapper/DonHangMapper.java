package namviet.rfid_api.mapper;

import namviet.rfid_api.dto.DonHangDTO;
import namviet.rfid_api.entity.DonHang;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {NhanVienMapper.class,KhachHangMapper.class})
public interface DonHangMapper {
    @Mapping(source = "nhanVien.nhanVienId", target = "nhanVienId")
    @Mapping(source = "khachHang.khachHangId", target = "khachHangId")
    @Mapping(source = "nhanVien.hoTen", target = "tenNhanVien")
    @Mapping(source = "khachHang.tenKhachHang", target = "tenKhachHang")
    DonHangDTO toDto(DonHang donHang);

    @Mapping(source = "nhanVienId", target = "nhanVien.nhanVienId")
    @Mapping(source = "khachHangId", target = "khachHang.khachHangId")
    DonHang toEntity(DonHangDTO donHangDTO);
}

