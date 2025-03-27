package namviet.rfid_api.mapper;

import namviet.rfid_api.dto.DonHangSanPhamDTO;
import namviet.rfid_api.entity.DonHangSanPham;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {SanPhamMapper.class , DonHangMapper.class, UpcMapper.class})
public interface DonHangSanPhamMapper {

    @Mapping(source = "donHangSanPhamId", target = "donHangSanPhamId")
    @Mapping(source = "donHang",target = "donHang")
    @Mapping(source = "sanPham", target = "sanPham")
    @Mapping(source = "tenFile",target = "tenFile")
    @Mapping(source = "soLuong", target = "soLuong")
    @Mapping(source = "sanPham.upc", target = "upc")
    DonHangSanPhamDTO toDTO(DonHangSanPham donHangSanPham);

    @Mapping(source = "donHangSanPhamId" , target = "donHangSanPhamId")
    @Mapping(source = "donHang",target = "donHang")
    @Mapping(source = "sanPham",target = "sanPham")
    @Mapping(source = "tenFile", target = "tenFile")
    @Mapping(source = "soLuong", target = "soLuong")
    DonHangSanPham toEntity(DonHangSanPhamDTO donHangSanPhamDTO);
}
