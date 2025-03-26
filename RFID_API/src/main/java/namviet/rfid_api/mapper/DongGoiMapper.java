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
    @Mapping(source = "nhanVien.hoTen", target = "tenNhanVien")
    @Mapping(source = "thietBi.tenMay", target = "tenThietBi")
    @Mapping(source = "banThanhPham.code", target = "codeBanThanhPham")
    @Mapping(source = "donHangSanPham.donHang.maLenh", target = "maLenh")
    @Mapping(source = "donHangSanPham.sanPham.sku", target = "sku")
    @Mapping(source = "donHangSanPham.sanPham.url", target = "url")
    DongGoiDTO toDTO(DongGoi dongGoi);

    @Mapping(source = "donHangSanPhamId", target = "donHangSanPham.donHangSanPhamId")
    @Mapping(source = "nhanVienId", target = "nhanVien.nhanVienId")
    @Mapping(source = "thietBiId", target = "thietBi.thietBiId")
    @Mapping(source = "banThanhPhamId",target = "banThanhPham.banThanhPhamId")
    DongGoi toEntity(DongGoiDTO dongGoiDTO);
}
