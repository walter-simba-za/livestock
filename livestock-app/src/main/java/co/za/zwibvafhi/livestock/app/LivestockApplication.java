package co.za.zwibvafhi.livestock.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main application class for the Livestock Management System.
 */
@SpringBootApplication
@EnableFeignClients
@EnableCaching
@ComponentScan(basePackages = {"co.za.zwibvafhi.livestock.app", "co.za.zwibvafhi.livestock.core"})
@EnableJpaRepositories(basePackages = "co.za.zwibvafhi.livestock.persistence.repository")
@EntityScan(basePackages = "co.za.zwibvafhi.livestock.persistence.entity")
public class LivestockApplication {

  public static void main(String[] args) {
    SpringApplication.run(LivestockApplication.class, args);
  }
}