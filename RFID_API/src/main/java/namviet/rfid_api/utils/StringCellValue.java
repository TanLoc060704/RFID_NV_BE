package namviet.rfid_api.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.context.annotation.Configuration;

import java.text.DecimalFormat;

@Configuration
public class StringCellValue {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0"); // Định dạng số không có dấu phẩy

    public static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        CellType cellType = cell.getCellType();

        switch (cellType) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return DECIMAL_FORMAT.format(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return DECIMAL_FORMAT.format(cell.getNumericCellValue());
                } catch (IllegalStateException e) {
                    return cell.getStringCellValue().trim();
                }
            case BLANK:
                return "";
            default:
                return "Unsupported cell type";
        }
    }
}
