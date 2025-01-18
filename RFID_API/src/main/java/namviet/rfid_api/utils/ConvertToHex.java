package namviet.rfid_api.utils;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ConvertToHex {

    public static String convertToHexadecimal(String epcValue) {
        StringBuilder sb = new StringBuilder();
        for (char ch : epcValue.toCharArray()) {
            sb.append(String.format("%02X", (int) ch));
        }
        return sb.toString();
    }
}
