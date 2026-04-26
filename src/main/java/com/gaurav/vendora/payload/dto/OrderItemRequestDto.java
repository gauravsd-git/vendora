package com.gaurav.vendora.payload.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemRequestDto {

    @NotNull
    private Long productId;

    @Min(1)
    private Integer quantity;
}