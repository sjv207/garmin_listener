package uk.ac.exeter.feele.garminlistener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GarminListenerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GarminListenerApplication.class, args);
	}

}
