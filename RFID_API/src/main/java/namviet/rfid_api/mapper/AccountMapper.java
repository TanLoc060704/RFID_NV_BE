package namviet.rfid_api.mapper;

import namviet.rfid_api.dto.AccountDTO;
import namviet.rfid_api.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "role.roleId", target = "roleId")
    @Mapping(source = "nhanVien.nhanVienId", target = "nhanVienId")
    AccountDTO toDto(Account account);

    @Mapping(source = "roleId",target = "role.roleId")
    @Mapping(source = "nhanVienId", target = "nhanVien.nhanVienId")
    Account toEntity(AccountDTO accountDTO);
}
