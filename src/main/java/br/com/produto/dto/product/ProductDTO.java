package br.com.produto.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDTO {

    private String id;

    private String name;

    private String description;

    private BigDecimal price;
}
