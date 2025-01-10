package namviet.rfid_api.mapper;

import namviet.rfid_api.dto.BanThanhPhamDTO;
import namviet.rfid_api.entity.BanThanhPham;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BanThanhPhamMapper {

    @Mapping(source = "nvl.nvlId",target = "nvlId")
    BanThanhPhamDTO toDTO(BanThanhPham banThanhPham);

    @Mapping(source = "nvlId",target = "nvl.nvlId")
    BanThanhPham toEntity (BanThanhPhamDTO banThanhPhamDTO);

}
