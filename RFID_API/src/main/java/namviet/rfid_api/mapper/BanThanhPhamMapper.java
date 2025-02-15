package namviet.rfid_api.mapper;

import namviet.rfid_api.dto.BanThanhPhamDTO;
import namviet.rfid_api.entity.BanThanhPham;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BanThanhPhamMapper {

    @Mapping(source = "nvl.nvlId",target = "nvlId")
    @Mapping(source = "account.accountId", target = "accountId")
    @Mapping(source = "account.nhanVien.hoTen", target = "tenNhanVien")
    @Mapping(source = "nvl.code", target = "maNvl")
    BanThanhPhamDTO toDTO(BanThanhPham banThanhPham);

    @Mapping(source = "nvlId",target = "nvl.nvlId")
    @Mapping(source = "accountId", target = "account.accountId")
    BanThanhPham toEntity (BanThanhPhamDTO banThanhPhamDTO);

}
