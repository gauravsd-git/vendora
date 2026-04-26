package com.gaurav.vendora.payload.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private Long id;
    private String name;
    private double price;
    private int quantity;
    private Long storeId;
}