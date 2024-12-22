package ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class KbeRestBreweryApplication {

    public static void main(String[] args) {
        log.info("Starting Spring 6 Template Application...");
        SpringApplication.run(KbeRestBreweryApplication.class, args);
    }

}
