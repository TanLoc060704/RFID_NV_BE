package namviet.rfid_api.utils;

import namviet.rfid_api.entity.SanPham;
import namviet.rfid_api.exception.CustomException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class Encoder {

    /**
     * Hàm tạo EPC theo chuẩn GS1 cho sản phẩm.
     *
     * Hàm này sẽ tạo một EPC 128-bit từ các thông tin của sản phẩm bao gồm:
     * - Header (8 bit)
     * - Filter (3 bit)
     * - Partition (3 bit)
     * - Company (40, 37, 34, 30, 27, 24, hoặc 20 bit tùy theo partition)
     * - Item Reference (4, 7, 10, 14, 17, 20, hoặc 24 bit tùy theo partition)
     * - Serial (38 bit)
     *
     * @param sanPham Sản phẩm DTO chứa thông tin cần thiết để tạo EPC.
     * @return EPC dạng chuỗi nhị phân (128-bit).
     */
    public static String taoEPCtheoChuanGS1(SanPham sanPham) {
        String upc = sanPham.getUpc().getUpc();

        sanPham.getUpc().setUpc(Encoder.removeCheckDigit(validateAndRetrieveUPC(sanPham)));
        String company = processUPCCompany(sanPham.getUpc().getUpc(), sanPham.getPartition());
        String itemReference = processUPCItem(sanPham.getUpc().getUpc(), sanPham.getPartition());
        Long serial = sanPham.getUpc().getSerial();
        int header = 48;
        int filter = sanPham.getFilter();
        int partition = sanPham.getPartition();

        String binaryHeader = String.format("%8s", Integer.toBinaryString(header)).replace(' ', '0');
        String binaryFilter = String.format("%3s", Integer.toBinaryString(filter)).replace(' ', '0');
        String binaryPartition = String.format("%3s", Integer.toBinaryString(partition)).replace(' ', '0');

        int companyBits = 0;
        int itemReferenceBits = 0;
        switch (partition) {
            case 0: companyBits = 40; itemReferenceBits = 4; break;
            case 1: companyBits = 37; itemReferenceBits = 7; break;
            case 2: companyBits = 34; itemReferenceBits = 10; break;
            case 3: companyBits = 30; itemReferenceBits = 14; break;
            case 4: companyBits = 27; itemReferenceBits = 17; break;
            case 5: companyBits = 24; itemReferenceBits = 20; break;
            case 6: companyBits = 20; itemReferenceBits = 24; break;
            default: throw new CustomException("Partition không hợp lệ", HttpStatus.BAD_REQUEST);
        }

        String binaryCompany = String.format("%" + companyBits + "s", Integer.toBinaryString(Integer.parseInt(company))).replace(' ', '0');
        String binaryItemReference = String.format("%" + itemReferenceBits + "s", Integer.toBinaryString(Integer.parseInt(itemReference))).replace(' ', '0');
        String binarySerial = String.format("%38s", Long.toBinaryString(serial)).replace(' ', '0');
        String epcBinary = binaryHeader + binaryFilter + binaryPartition + binaryCompany + binaryItemReference + binarySerial;
        sanPham.getUpc().setUpc(upc);
        return binaryToHex(epcBinary);
    }


    /**
     * Hàm kiểm tra tính hợp lệ của UPC và trả về UPC đã qua kiểm tra.
     * @param sanPham Sản phẩm DTO
     * @return UPC đã kiểm tra
     */
    public static String validateAndRetrieveUPC(SanPham sanPham) {
        if (sanPham == null) {
            throw new CustomException("Sản phẩm không được null", HttpStatus.BAD_REQUEST);
        }

        String upc = sanPham.getUpc().getUpc();
        if (upc == null || upc.isEmpty()) {
            throw new CustomException("UPC không được để trống", HttpStatus.BAD_REQUEST);
        }

        if (upc.length() > 14) {
            throw new CustomException("UPC không được quá 14 ký tự", HttpStatus.BAD_REQUEST);
        }

        return String.format("%014d", Long.parseLong(upc));
    }

    /**
     * Hàm loại bỏ ký tự kiểm tra (check digit) cuối cùng của UPC.
     *
     * @param upc Chuỗi UPC cần xử lý.
     * @return Chuỗi UPC sau khi loại bỏ ký tự kiểm tra.
     * @throws CustomException Nếu UPC null, rỗng hoặc có độ dài không hợp lệ.
     */
    public static String removeCheckDigit(String upc) {
        if (upc == null || upc.isEmpty()) {
            throw new CustomException("UPC không được để trống", HttpStatus.BAD_REQUEST);
        }

        if (upc.length() != 14) {
            throw new CustomException("UPC phải có đúng 14 ký tự", HttpStatus.BAD_REQUEST);
        }

        return upc.substring(0, upc.length() - 1);
    }

    /**
     * Xử lý chuỗi UPC dựa trên partition.
     *
     * @param upc       Chuỗi UPC ban đầu (13 ký tự).
     * @param partition Giá trị partition từ 0 đến 6.
     * @return Chuỗi UPC được xử lý.
     * @throws CustomException Nếu partition không hợp lệ hoặc upc không đủ độ dài.
     */
    private static String processUPCCompany(String upc, int partition) {
        if (upc == null || upc.length() != 13) {
            throw new CustomException("UPC phải có đúng 13 ký tự", HttpStatus.BAD_REQUEST);
        }

        if (partition < 0 || partition > 6) {
            throw new CustomException("Partition phải nằm trong khoảng từ 0 đến 6", HttpStatus.BAD_REQUEST);
        }

        int cutLength = 12 - partition;
        String extracted = upc.substring(1, 1 + cutLength);

        return extracted;
    }

    /**
     * Xử lý chuỗi UPC dựa trên partition.
     *
     * @param upc       Chuỗi UPC ban đầu (13 ký tự).
     * @param partition Giá trị partition từ 0 đến 6.
     * @return Chuỗi UPC được xử lý.
     * @throws CustomException Nếu partition không hợp lệ hoặc upc không đủ độ dài.
     */
    private static String processUPCItem(String upc, int partition) {
        if (upc == null || upc.length() != 13) {
            throw new CustomException("UPC phải có đúng 13 ký tự", HttpStatus.BAD_REQUEST);
        }

        if (partition < 0 || partition > 6) {
            throw new CustomException("Partition phải nằm trong khoảng từ 0 đến 6", HttpStatus.BAD_REQUEST);
        }

        int cutLength = 12 - partition;
        String remaining = upc.substring(1 + cutLength);

        char firstChar = upc.charAt(0);
        return firstChar + remaining;
    }

    /**
     * Hàm chuyển đổi chuỗi nhị phân sang hex.
     *
     * @param binary Chuỗi nhị phân cần chuyển đổi.
     * @return Chuỗi hex tương ứng.
     */
    private static String binaryToHex(String binary) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 4) {
            String chunk = binary.substring(i, i + 4);
            int hexValue = Integer.parseInt(chunk, 2);
            hexString.append(Integer.toHexString(hexValue).toUpperCase());
        }
        return hexString.toString();
    }
}
