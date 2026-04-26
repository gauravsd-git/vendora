package com.gaurav.vendora.payload.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestDto {

    private String customerName;

    @NotEmpty
    private List<OrderItemRequestDto> items;
}