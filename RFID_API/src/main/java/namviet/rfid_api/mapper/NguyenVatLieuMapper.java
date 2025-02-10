package namviet.rfid_api.mapper;

import namviet.rfid_api.dto.NguyenVatLieuDTO;
import namviet.rfid_api.entity.NguyenVatLieu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NguyenVatLieuMapper {

//    @Mapping(source = "nhanVien.hoTen", target = "tenNhanVien")
//    @Mapping(source = "nhanVien.nhanVienId", target = "nhanVienId")
    @Mapping(source = "account.accountId", target = "accountId")
    @Mapping(source = "account.nhanVien.hoTen", target = "tenNhanVien")
    NguyenVatLieuDTO toDTO(NguyenVatLieu nguyenVatLieu);

//    @Mapping(source = "tenNhanVien", target = "nhanVien.hoTen")
    @Mapping(source = "accountId", target = "account.accountId")
    NguyenVatLieu toEntity(NguyenVatLieuDTO nguyenVatLieuDTO);
}
