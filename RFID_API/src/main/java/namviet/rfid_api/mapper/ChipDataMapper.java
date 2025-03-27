package namviet.rfid_api.mapper;

import namviet.rfid_api.dto.ChipDataDTO;
import namviet.rfid_api.entity.ChipData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChipDataMapper {
    ChipDataDTO toDTO(ChipData chipData);
    ChipData toEntity(ChipDataDTO chipDataDTO);
}