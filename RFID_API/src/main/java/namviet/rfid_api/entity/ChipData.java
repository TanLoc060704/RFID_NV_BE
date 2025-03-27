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
@Table(name = "chip_data")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChipData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chip_id")
    Integer chipId;

    @Column(name = "chip", nullable = false, length = 50)
    String chip;

    @Column(name = "tid_header", length = 50)
    String tidHeader;

    @Column(name = "epc_word_length")
    Integer epcWordLength;

    @Column(name = "usr_word_length")
    Integer usrWordLength;

    @Column(name = "tid_word_length")
    Integer tidWordLength;

    @Column(name = "epc_bit_length")
    Integer epcBitLength;

    @Column(name = "usr_bit_length")
    Integer usrBitLength;

    @Column(name = "tid_bit_length")
    Integer tidBitLength;
}