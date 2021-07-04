package br.com.produto.service.product;

import br.com.produto.domain.error.Error;
import br.com.produto.domain.product.Product;
import br.com.produto.dto.product.ProductDTO;
import br.com.produto.repository.product.ProductRepository;
import br.com.produto.util.CustomExeption;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product save(Product product) {

        try {

            return productRepository.save(product);

        } catch (Exception e) {

            Error error = new Error("Falha ao gravar dados do produto", HttpStatus.BAD_REQUEST);

            throw new CustomExeption(error);
        }
    }

    public Product save(ProductDTO productDTO) {

        Product product = new Product();

        BeanUtils.copyProperties(productDTO, product);

        return save(product);

    }

    public Product update(String id, ProductDTO productDTO) {

        Product product = findById(id.trim());

        BeanUtils.copyProperties(productDTO, product);

        product.setId(id);

        return save(product);
    }

    public Product findById(String id) {

        Optional<Product> product = productRepository.findById(id);

        throwErroProdutoNaoEncontrado(product);

        return product.get();
    }

    private void throwErroProdutoNaoEncontrado(Optional<Product> product) {

        if (!product.isPresent()) {

            Error error = new Error("Produto não encontrado", HttpStatus.NOT_FOUND);

            throw new CustomExeption(error);
        }
    }

    public List<Product> findAll() {

        return productRepository.findAll();
    }

    public List<Product> findProducts(String q, BigDecimal minPrice, BigDecimal maxPrice) {

        return productRepository.findProducts(q, minPrice, maxPrice);
    }

    public void deleteById(String id) {

        Product product = findById(id);

        productRepository.delete(product);
    }

    public void deleteAll() {

        productRepository.deleteAll();
    }
}
