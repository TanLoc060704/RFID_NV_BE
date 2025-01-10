package namviet.rfid_api.utils;

import namviet.rfid_api.entity.SanPham;
import namviet.rfid_api.entity.Upc;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Decoder {


    public static SanPham epcToDulieu(String epc) {
        String binary = hexToBinary(epc);
//        String header = binaryToHex(binary,0,8);
        String filter = binaryToHex(binary,8,11);
        String serial = binaryToHex(binary,binary.length()-38,binary.length());
        String partition = binaryToHex(binary,11,14);

        String company = catChuoiGtin14ToCompany(binary,Integer.parseInt(partition));
        String item = catChuoiGtin14ToItem(binary,Integer.parseInt(partition));

        String formart = formartIndicatorCompanyItem(company,item);
        int checkDigit =  calculateCheckDigit(formart);

        formart = formart + checkDigit;
        return new SanPham(Integer.parseInt(partition),Integer.parseInt(filter),new Upc(formart, Long.parseLong(serial)));
    }

    /**
     * Hàm chuyển chuỗi epc sang binary
     * @param epc kiểu dữ liệu là string
     * @return chuỗi binary đã decoder
     * **/
    public static String hexToBinary(String epc) {
        String binary = new java.math.BigInteger(epc, 16).toString(2);
        return String.format("%96s", binary).replace(' ', '0');
    }

    /**
     * Hàm chuyển binary sang hex
     * @param binary start digit
     * @return chuỗi **/
    public static String binaryToHex(String binary,int start,int digit) {
        String firstBits = binary.substring(start, digit);
        return String.valueOf(Long.parseLong(firstBits, 2));
    }

    /**
     * Hàm trích xuất và chuyển đổi chuỗi nhị phân thành giá trị Company từ GTIN-14.
     *
     * @param binary   Chuỗi nhị phân cần xử lý.
     * @param partition Giá trị xác định số lượng bit của trường Company và Item.
     *                  Các giá trị hợp lệ: 0, 1, 2, 3, 4, 5, 6.
     * @return Giá trị Company sau khi chuyển đổi từ chuỗi nhị phân sang số thập phân.
     * @throws IllegalArgumentException nếu partition không hợp lệ.
     */
    public static String catChuoiGtin14ToCompany(String binary, int partition) {
        int length;
        int companyBits = switch (partition) {
            case 0 ->{
                length = 12;
                yield 40;
            }
            case 1 -> {
                length = 11;
                yield  37;
            }
            case 2 ->{
              length = 10;
              yield 34;
            }
            case 3 -> {
                length = 9;
                yield 30;
            }
            case 4 -> {
                length = 8;
                yield 27;
            }
            case 5 -> {
                length = 7;
                yield 24;
            }
            case 6 -> {
                length = 6;
                yield 20;
            }
            default -> throw new IllegalArgumentException("Partition không hợp lệ: " + partition);
        };
        String companyBinary = binary.substring(14, 14 + companyBits);
        long companyInt = Long.parseLong(companyBinary,2);

        String companyString = String.valueOf(companyInt);
        if(companyString.length() < length){
            companyString = String.format("%0"+length+"d",companyInt);
        }
        return companyString;
    }


    /**
     * Hàm trích xuất và chuyển đổi chuỗi nhị phân thành giá trị Item từ GTIN-14.
     *
     * @param binary   Chuỗi nhị phân cần xử lý.
     * @param partition Giá trị xác định số lượng bit của trường Company và Item.
     *                  Các giá trị hợp lệ: 0, 1, 2, 3, 4, 5, 6.
     * @return Giá trị Item sau khi chuyển đổi từ chuỗi nhị phân sang số thập phân.
     * @throws IllegalArgumentException nếu partition không hợp lệ.
     */
    public static String catChuoiGtin14ToItem(String binary, int partition) {
        int companyBits, itemBits, length;
        itemBits = switch (partition) {
            case 0 -> {
                companyBits = 40;
                length = 1;
                yield 4;
            }
            case 1 -> {
                companyBits = 37;
                length = 2;
                yield 7;
            }
            case 2 -> {
                companyBits = 34;
                length = 3;
                yield 10;
            }
            case 3 -> {
                companyBits = 30;
                length = 4;
                yield 14;
            }
            case 4 -> {
                companyBits = 27;
                length = 5;
                yield 17;
            }
            case 5 -> {
                companyBits = 24;
                length = 6;
                yield 20;
            }
            case 6 -> {
                companyBits = 20;
                length = 7;
                yield 24;
            }
            default -> throw new IllegalArgumentException("Partition không hợp lệ: " + partition);
        };
        String itemBinary = binary.substring(14 + companyBits, 14 + companyBits + itemBits);
        long itemDecimal = Long.parseLong(itemBinary,2);

        String itemDecimalStr = String.valueOf(itemDecimal);
        if(itemDecimalStr.length() < length){
            itemDecimalStr = String.format("%0"+length+"d", itemDecimal);
        }
        return itemDecimalStr;
    }

    public static String formartIndicatorCompanyItem(String copany, String item){
        String indicator = item.substring(0,1);
        String itemRemoveIndicator = item.substring(1);
        return indicator+copany+itemRemoveIndicator;
    }

    public static int calculateCheckDigit(String gtin14) {
        if (gtin14.length() != 13 && gtin14.length() != 14) {
            throw new IllegalArgumentException("Chuỗi đầu vào phải có độ dài 13 (UPC/EAN-13) hoặc 14 (GTIN-14) ký tự.");
        }

        int sumChan = 0;
        int sumLe = 0;

        for (int i = 0; i < gtin14.length(); i++) {
            int digit = Character.getNumericValue(gtin14.charAt(i));
            if (i % 2 == 0) {
                sumLe += digit;
            } else {
                sumChan += digit;
            }
        }
        int tongLe = sumLe * 3;
        int total = sumChan + tongLe;

        return (10 - (total % 10)) % 10;
    }

}
