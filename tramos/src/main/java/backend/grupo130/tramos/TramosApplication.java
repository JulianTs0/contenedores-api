package backend.grupo130.tramos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TramosApplication {

	public static void main(String[] args) {
		SpringApplication.run(TramosApplication.class, args);
	}

}
