package postly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PostlyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostlyApplication.class, args);
	}

}
