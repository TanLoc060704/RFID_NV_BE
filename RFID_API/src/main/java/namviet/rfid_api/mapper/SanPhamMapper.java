package namviet.rfid_api.mapper;

import namviet.rfid_api.dto.SanPhamDTO;
import namviet.rfid_api.entity.SanPham;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = UpcMapper.class)
public interface SanPhamMapper {

//    @Mapping(source = "upc",target = "upcDTO")
    @Mapping(source = "upc.upcId",target = "upcId")
    @Mapping(source = "upc.upc",target = "upc")
    @Mapping(source = "khachHang.khachHangId",target = "khachHangId")
    @Mapping(source = "khachHang.tenKhachHang",target = "tenKhachHang")
    @Mapping(source = "upc.serial",target = "serial")
    SanPhamDTO toDto(SanPham sanPham);

//    @Mapping(source = "upcDTO",target = "upc")
    @Mapping(source = "upcId",target = "upc.upcId")
    @Mapping(source = "khachHangId",target = "khachHang.khachHangId")
    SanPham toEntity(SanPhamDTO sanPhamDTO);
}
