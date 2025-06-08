package co.za.zwibvafhi.livestock.client;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configuration for Feign client. */
@Configuration
public class FeignConfig {

  @Bean
  public ErrorDecoder errorDecoder() {
    return new LivestockFeignErrorDecoder();
  }
}
