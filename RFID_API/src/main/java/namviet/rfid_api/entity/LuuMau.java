package namviet.rfid_api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "luu_mau")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LuuMau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "luu_mau_id")
    Integer luuMauId;

    @Column(name = "code", length = 20)
    String code;

    @Column(name = "vitri", length = 20)
    String vitri;

    @Column(name = "nha_cung_cap", length = 20)
    String nhaCungCap;

    @Column(name = "ten_san_pham", length = 30)
    String tenSanPham;

    @Column(name = "thanh_dang_pham", length = 30)
    String thanhDangPham;

    @Column(name = "chieu_dai_nhan")
    Float chieuDaiNhan;

    @Column(name = "chieu_ronng_nhan")
    Float chieuRongNhan;

    @Column(name = "chieu_dai_chip")
    Float chieuDaiChip;

    @Column(name = "chieu_rong_chip")
    Float chieuRongChip;

    @Column(name = "chat_lieu", length = 20)
    String chatLieu;

    @Column(name = "ma_giay", length = 20)
    String maGiay;

    @Column(name = "ma_inlay", length = 20)
    String maInlay;

    @Column(name = "chip", length = 20)
    String chip;

    @Column(name = "ma_keo", length = 20)
    String maKeo;

    @Column(name = "thiet_do", length = 20)
    String thietDo;

    @Column(name = "dinh_luong", length = 20)
    String dinhLuong;

    @Column(name = "ghi_chu", length = 100)
    String ghiChu;

    @Column(name = "so_luong")
    Integer soLuong;

    @Temporal(TemporalType.DATE)
    @Column(name = "ngay_tao")
    Date ngayTao;

    @ManyToOne
    @JoinColumn(name = "nhan_vien_id", referencedColumnName = "nhan_vien_id")
    NhanVien nhanVien;
}
