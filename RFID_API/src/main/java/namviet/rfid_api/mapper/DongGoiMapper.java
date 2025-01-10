package namviet.rfid_api.mapper;

import namviet.rfid_api.dto.DongGoiDTO;
import namviet.rfid_api.entity.DongGoi;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {DonHangSanPhamMapper.class,NhanVienMapper.class,ThietBiMapper.class})
public interface DongGoiMapper {

    @Mapping(source = "donHangSanPham.donHangSanPhamId", target = "donHangSanPhamId")
    @Mapping(source = "nhanVien.nhanVienId", target = "nhanVienId")
    @Mapping(source = "thietBi.thietBiId", target = "thietBiId")
    @Mapping(source = "banThanhPham.banThanhPhamId",target = "banThanhPhamId")
    DongGoiDTO toDTO(DongGoi dongGoi);

    @Mapping(source = "donHangSanPhamId", target = "donHangSanPham.donHangSanPhamId")
    @Mapping(source = "nhanVienId", target = "nhanVien.nhanVienId")
    @Mapping(source = "thietBiId", target = "thietBi.thietBiId")
    @Mapping(source = "banThanhPhamId",target = "banThanhPham.banThanhPhamId")
    DongGoi toEntity(DongGoiDTO dongGoiDTO);
}
