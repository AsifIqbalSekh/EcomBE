package com.asifiqbalsekh.EcomBE.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long id;

    @NotEmpty
    @Size(min = 3, message = "must be more than 3 character")
    private String name;

    private String description;

    @NotNull(message = "cannot be null")
    @PositiveOrZero(message = "must be zero or positive")
    private Integer quantity;

    @NotNull(message = "cannot be null")
    @PositiveOrZero(message = "must be zero or positive")
    private Double price;

    private Double discount;
    private Double specialPrice;

    private String image;

    private CategoryDTO category;

}
