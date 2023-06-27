package dev.blazo.crud.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO {
    @NotBlank
    private String name;

    @NotBlank
    private Float price;

    public ProductDTO(String name, @Min(0) Float price) {
        this.name = name;
        this.price = price;
    }
}
