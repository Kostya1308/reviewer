package ru.clevertec.courses.reviewer.client;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "reviewed-app", url = "${app.reviewed-app}")
public interface ReviewedAppClient {


}
