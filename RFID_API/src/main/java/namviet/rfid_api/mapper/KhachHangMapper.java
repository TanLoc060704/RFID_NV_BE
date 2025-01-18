package namviet.rfid_api.mapper;

import namviet.rfid_api.dto.KhachHangDTO;
import namviet.rfid_api.entity.KhachHang;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface KhachHangMapper {
    KhachHangDTO toDTO (KhachHang khachHang);
    KhachHang toEntity(KhachHangDTO khachHangdto);

}
