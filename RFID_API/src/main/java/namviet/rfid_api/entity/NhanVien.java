package namviet.rfid_api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name = "nhan_vien")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NhanVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nhan_vien_id")
    Integer nhanVienId;

    @Column(name = "ho_ten", nullable = false, length = 50)
    String hoTen;

    @Column(name = "chuc_vu", length = 50)
    String chucVu;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    Account account;
}
