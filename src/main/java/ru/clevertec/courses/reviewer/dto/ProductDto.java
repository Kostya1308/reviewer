package ru.clevertec.courses.reviewer.dto;

import lombok.Data;

@Data
public class ProductDto {

    private Integer id;
    private String description;
    private Float price;
    private Integer quantity;
    private Boolean isWholesale;

}
