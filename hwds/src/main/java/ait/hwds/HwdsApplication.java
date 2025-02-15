package ait.hwds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HwdsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HwdsApplication.class, args);
    }

}
