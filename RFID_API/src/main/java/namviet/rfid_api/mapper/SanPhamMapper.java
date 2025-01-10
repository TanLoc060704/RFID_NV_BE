package namviet.rfid_api.mapper;

import namviet.rfid_api.dto.SanPhamDTO;
import namviet.rfid_api.entity.SanPham;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = UpcMapper.class)
public interface SanPhamMapper {

    @Mapping(source = "upc",target = "upcDTO")
    SanPhamDTO toDto(SanPham sanPham);

    @Mapping(source = "upcDTO",target = "upc")
    SanPham toEntity(SanPhamDTO sanPhamDTO);
}
