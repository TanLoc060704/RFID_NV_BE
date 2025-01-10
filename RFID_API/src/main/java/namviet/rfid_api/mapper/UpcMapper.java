package namviet.rfid_api.mapper;

import namviet.rfid_api.dto.UpcDTO;
import namviet.rfid_api.entity.Upc;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UpcMapper {

    UpcDTO toDTO(Upc upc);

    Upc toEntity(UpcDTO upcDTO);
}
