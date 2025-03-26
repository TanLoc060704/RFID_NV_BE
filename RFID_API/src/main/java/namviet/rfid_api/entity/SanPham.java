package namviet.rfid_api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name = "san_pham")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class SanPham {

    public SanPham(Integer partition, Integer filter, Upc upc) {
        this.partition = partition;
        this.filter = filter;
        this.upc = upc;
    }

    @Id
    @Column(name = "sku", length = 50)
    String sku;

    @Column(name = "partition")
    Integer partition;

    @Column(name = "filter")
    Integer filter;

    @Column(name = "head", length = 50)
    String head;

    @Column(name = "content", length = 2000)
    String content;

    @Column(name = "kich_thuoc", length = 50)
    String kichThuoc;

    @Column(name = "inlay", length = 50)
    String inlay;

    @Column(name = "ncc_inlay", length = 50)
    String nccInlay;

    @Column(name = "masp", length = 50)
    String masp;

    @Column(name = "url")
    String url;

    @ManyToOne
    @JoinColumn(name = "upc_id")
    Upc upc;

    @ManyToOne
    @JoinColumn(name = "khach_hang_id")
    KhachHang khachHang;
}
