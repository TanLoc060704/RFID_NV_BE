package namviet.rfid_api.mapper;

import namviet.rfid_api.dto.KhachHangSanPhamDTO;
import namviet.rfid_api.entity.KhachHangSanPham;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring",uses = {KhachHangMapper.class, SanPhamMapper.class})
public interface KhachHangSanPhamMapper {

    @Mapping(source = "khachHang.khachHangId", target = "khachHangId")
    @Mapping(source = "sanPham.sku", target = "sku")
    KhachHangSanPhamDTO toDTO(KhachHangSanPham khachHangSanPham);

    @Mapping(target = "khachHang.khachHangId", source = "khachHangId")
    @Mapping(target = "sanPham.sku", source = "sku")
    KhachHangSanPham toEntity(KhachHangSanPhamDTO khachHangSanPhamDTO);
}
