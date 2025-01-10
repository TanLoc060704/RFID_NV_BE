package namviet.rfid_api.mapper;

import namviet.rfid_api.dto.NguyenVatLieuDTO;
import namviet.rfid_api.entity.NguyenVatLieu;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NguyenVatLieuMapper {


    NguyenVatLieuDTO toDTO(NguyenVatLieu nguyenVatLieu);

    NguyenVatLieu toEntity(NguyenVatLieuDTO nguyenVatLieuDTO);
}
