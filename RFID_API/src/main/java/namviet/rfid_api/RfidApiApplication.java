package namviet.rfid_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "namviet.rfid_api")
public class RfidApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RfidApiApplication.class, args);
    }

}
