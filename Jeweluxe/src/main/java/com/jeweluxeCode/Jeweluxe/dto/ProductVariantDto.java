package com.jeweluxeCode.Jeweluxe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantDto {

    private Long id;
    private String color;
    private String size;
    private Integer stockQuantity;
}

