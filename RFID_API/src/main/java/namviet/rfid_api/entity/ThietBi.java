package namviet.rfid_api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name = "thiet_bi")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ThietBi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thiet_bi_id")
    Integer thietBiId;

    @Column(name = "ten_may", length = 50)
    String tenMay;

    @Column(name = "chuc_nang", length = 100)
    String chucNang;
}
