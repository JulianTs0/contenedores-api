package backend.grupo130.camiones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CamionesApplication {
    public static void main(String[] args) {
        SpringApplication.run(CamionesApplication.class, args);
    }
}
