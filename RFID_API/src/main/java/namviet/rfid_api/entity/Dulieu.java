package namviet.rfid_api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name = "data")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Dulieu {

    public Dulieu(String epc, String sku, SanPham sanPham, DonHang donHang,DonHangSanPham donHangSanPham) {
        this.epc = epc;
        this.sku = sku;
        this.sanPham = sanPham;
        this.donHang = donHang;
        this.donHangSanPham = donHangSanPham;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "data_id")
    Integer dataId;

    @Column(name = "epc", length = 50)
    String epc;

    @Column(name = "sku", length = 50)
    String sku;

    @Column(name = "tid", length = 50)
    String tid;

    @Column(name = "data_goc", length = 200)
    String dataGoc;

    @Column(name = "noi_dung_bien_doi", length = 500)
    String noiDungBienDoi;

    @ManyToOne
    @JoinColumn(name = "sku", referencedColumnName = "sku", insertable = false, updatable = false)
    SanPham sanPham;

    @ManyToOne
    @JoinColumn(name = "don_hang_id", referencedColumnName = "don_hang_id")
    DonHang donHang;

    @ManyToOne
    @JoinColumn(name = "don_hang_san_pham_id", referencedColumnName = "don_hang_san_pham_id")
    DonHangSanPham donHangSanPham;
}
