package br.com.produto.repository.product;

import br.com.produto.domain.product.Product;
import br.com.produto.repository.product.helper.ProductRepositoryQueries;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String>, ProductRepositoryQueries {

    Optional<Product> findById(String id);
}
