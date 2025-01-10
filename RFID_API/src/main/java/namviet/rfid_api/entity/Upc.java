package namviet.rfid_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name = "Upc")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Upc {

    public Upc(int upcId, long serial) {
        this.upcId = upcId;
        this.serial = serial;
    }

    public Upc(String upc, long serial) {
        this.upc = upc;
        this.serial = serial;
    }

    @Id
    @Column(name = "upc_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int upcId;

    @Column(name = "upc", length = 14)
    String upc;

    @Column(name = "serial")
    @Max(value = 274877906943L, message = "Serial không được vượt quá 274,877,906,943")
    long serial;
}
