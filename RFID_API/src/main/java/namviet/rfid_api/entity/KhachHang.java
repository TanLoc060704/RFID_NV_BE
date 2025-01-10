package namviet.rfid_api.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "khach_hang")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "khach_hang_id")
    int khachHangId;

    @Column(name = "ten_khach_hang")
    String tenKhachHang;

}
