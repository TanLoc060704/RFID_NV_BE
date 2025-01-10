package namviet.rfid_api.mapper;

import namviet.rfid_api.dto.NhanVienDTO;
import namviet.rfid_api.entity.NhanVien;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NhanVienMapper {

    @Mapping(source = "account.accountId",target = "accountId")
    NhanVienDTO toDto(NhanVien nhanVien);

    @Mapping(source = "accountId",target = "account.accountId")
    NhanVien toEntity(NhanVienDTO nhanVienDTO);
}
