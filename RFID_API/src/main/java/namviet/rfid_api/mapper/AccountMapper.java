package namviet.rfid_api.mapper;

import namviet.rfid_api.dto.AccountDTO;
import namviet.rfid_api.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "role.roleId", target = "roleId")
    AccountDTO toDto(Account account);

    @Mapping(source = "roleId",target = "role.roleId")
    Account toEntity(AccountDTO accountDTO);
}
