package co.za.zwibvafhi.livestock.core.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for caching using Caffeine.
 * */
@Configuration
@EnableCaching
public class CacheConfig {

  /**
   * Configures the cache manager with Caffeine.
   *
   * @return Cache manager instance
   */
  @Bean
  public CaffeineCacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager("counts", "events");
    cacheManager.setCaffeine(
        Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).maximumSize(1000));
    return cacheManager;
  }
}
