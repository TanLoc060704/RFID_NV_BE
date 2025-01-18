package namviet.rfid_api.mapper;

import namviet.rfid_api.dto.LuuMauDTO;
import namviet.rfid_api.entity.LuuMau;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LuuMauMapper {

    @Mapping(source = "nhanVien.nhanVienId", target = "nhanVienId")
    LuuMauDTO toDto (LuuMau luuMau);

    @Mapping(source = "nhanVienId", target = "nhanVien.nhanVienId")
    LuuMau toEntity(LuuMauDTO luuMauDTO);
}
