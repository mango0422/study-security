package mango.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InmemoryResourceServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(InmemoryResourceServerApplication.class, args);
    }
}
