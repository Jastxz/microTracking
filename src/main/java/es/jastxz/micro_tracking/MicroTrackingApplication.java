package es.jastxz.micro_tracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MicroTrackingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroTrackingApplication.class, args);
	}

}
