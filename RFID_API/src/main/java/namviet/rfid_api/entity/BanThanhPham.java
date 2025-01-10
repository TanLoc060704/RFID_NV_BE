package namviet.rfid_api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ban_thanh_pham")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BanThanhPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ban_thanh_pham_id")
    Integer banThanhPhamId;

    @Column(name = "code", length = 100)
    String code;

    @Column(name = "so_pcs_tot", nullable = false)
    Integer soPcsTot;

    @Column(name = "so_pcs_hu", columnDefinition = "int default 0")
    Integer soPcsHu = 0;

    @ManyToOne
    @JoinColumn(name = "nvl_id", referencedColumnName = "nvl_id")
    NguyenVatLieu nvl;
}
