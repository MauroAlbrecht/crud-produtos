package br.com.produto.repository.product.helper;

import br.com.produto.domain.product.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepositoryQueries {

    List<Product> findProducts(String q, BigDecimal minPrice, BigDecimal maxPrice);
}
