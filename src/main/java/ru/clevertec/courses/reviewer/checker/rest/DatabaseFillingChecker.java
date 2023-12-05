package ru.clevertec.courses.reviewer.checker.rest;

import org.springframework.stereotype.Component;
import ru.clevertec.courses.reviewer.client.ReviewedAppClient;
import ru.clevertec.courses.reviewer.dto.ProductDto;
import ru.clevertec.courses.reviewer.exception.DatabaseFillingException;

import java.util.List;

@Component
public class DatabaseFillingChecker extends AbstractRestChecker {

    public DatabaseFillingChecker(ReviewedAppClient reviewedAppClient) {
        super(reviewedAppClient);
    }

    @Override
    public void check() {
        List<ProductDto> products = reviewedAppClient.getProducts();
        products.stream()
                .findAny();
    }

}
