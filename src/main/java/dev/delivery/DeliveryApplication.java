package dev.delivery;

import dev.delivery.services.InitializerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeliveryApplication {
    public static InitializerService initializerService;

    @Autowired
    public void setInitialLoader(InitializerService initializerService) {
        DeliveryApplication.initializerService = initializerService;
    }

    //todo инициалайзер
    public static void main(String[] args) {
        SpringApplication.run(DeliveryApplication.class, args);
//        initializerService.init();
    }

}
