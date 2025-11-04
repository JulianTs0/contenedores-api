package backend.grupo130.contenedores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ContenedoresApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContenedoresApplication.class, args);
	}

}
