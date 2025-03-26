package namviet.rfid_api.mapper;

import namviet.rfid_api.dto.NhanVienDTO;
import namviet.rfid_api.entity.NhanVien;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NhanVienMapper {

    @Mapping(source = "account.accountId", target = "accountId")
    @Mapping(source = "account.userName", target = "username")
    @Mapping(source = "account.password", target = "password")
    @Mapping(source = "account.role.roleId", target = "roleId")
    @Mapping(source = "account.role.roleName", target = "roleName")
    @Mapping(source = "account.active", target = "active")
    NhanVienDTO toDto(NhanVien nhanVien);

    @Mapping(source = "accountId", target = "account.accountId")
    @Mapping(source = "username", target = "account.userName")
    @Mapping(source = "password", target = "account.password")
    @Mapping(source = "roleId", target = "account.role.roleId")
    @Mapping(source = "active", target = "account.isActive")
    NhanVien toEntity(NhanVienDTO nhanVienDTO);
}