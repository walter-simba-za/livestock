package co.za.zwibvafhi.livestock.client;

import co.za.zwibvafhi.livestock.api.contract.LivestockApi;
import org.springframework.cloud.openfeign.FeignClient;

/** Feign client for livestock API. */
@FeignClient(name = "livestock", url = "${livestock.api.url}", configuration = FeignConfig.class)
public interface LivestockClient extends LivestockApi {}
