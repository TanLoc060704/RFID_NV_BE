package namviet.rfid_api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.text.Format;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nvl")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NguyenVatLieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nvl_id")
    Integer nvlId;

    @Column(name = "code", length = 100)
    String code;

    @Column(name = "lot", length = 100)
    String lot;

    @Column(name = "so_pcs_tot", nullable = false)
    Integer soPcsTot;

    @Column(name = "so_pcs_hu", columnDefinition = "int default 0")
    Integer soPcsHu = 0;

    @Column(name = "ngay_nhap")
    @Temporal(TemporalType.DATE)
    Date ngayNhap = new Date();


    @ManyToOne()
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    Account account;
}
