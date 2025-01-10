package namviet.rfid_api.mapper;

import namviet.rfid_api.dto.ThietBiDTO;
import namviet.rfid_api.entity.ThietBi;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ThietBiMapper {

    ThietBiDTO toDto(ThietBi thietBi);

    ThietBi toEntity(ThietBiDTO thietBiDTO);
}
