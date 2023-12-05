package ru.clevertec.courses.reviewer.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.courses.reviewer.dto.ProductDto;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ReviewedAppClient {

    @GetMapping("/products")
   public List<ProductDto>  getProducts(){
        return new ArrayList<>();
    };


}
