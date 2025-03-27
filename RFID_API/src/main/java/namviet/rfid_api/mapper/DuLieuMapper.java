package namviet.rfid_api.mapper;

import namviet.rfid_api.dto.DuLieuDTO;
import namviet.rfid_api.entity.Dulieu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SanPhamMapper.class,DonHangMapper.class, DonHangSanPhamMapper.class})
public interface DuLieuMapper {

    @Mapping(source = "sanPham.sku", target = "sku")
    @Mapping(source = "donHang.donHangId", target = "donHangId")
    @Mapping(source = "donHangSanPham.donHangSanPhamId", target = "donHangSanPhamId")
    DuLieuDTO toDto(Dulieu data);

    @Mapping(source = "sku", target = "sanPham.sku")
    @Mapping(source = "donHangId",target = "donHang.donHangId")
    @Mapping(source = "donHangSanPhamId", target = "donHangSanPham.donHangSanPhamId")
    Dulieu toEntity(DuLieuDTO dataDTO);
}
