package namviet.rfid_api.dto;

import lombok.Data;

@Data
public class FileResourceDTO {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
    private String fileContent;
}
