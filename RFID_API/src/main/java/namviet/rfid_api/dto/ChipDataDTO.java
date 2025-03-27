package namviet.rfid_api.dto;

import lombok.Data;

@Data
public class ChipDataDTO {
    private Integer chipId;
    private String chip;
    private String tidHeader;
    private Integer epcWordLength;
    private Integer usrWordLength;
    private Integer tidWordLength;
    private Integer epcBitLength;
    private Integer usrBitLength;
    private Integer tidBitLength;
}