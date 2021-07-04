package br.com.produto.productms;

import br.com.produto.domain.product.Product;
import br.com.produto.dto.product.ProductDTO;
import br.com.produto.service.product.ProductService;
import br.com.produto.util.CustomExeption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class ProductServiceTests {

    @Autowired
    ProductService productService;

    @Test
    @DisplayName("Gravando um produto")
    void gravandoUmProduto() {

        Product productGravar = mockProduto();

        Product produtoGravado = productService.save(productGravar);

        Assertions.assertNotNull(produtoGravado.getId());
        Assertions.assertEquals(produtoGravado.getDescription(), productGravar.getDescription());
        Assertions.assertEquals(produtoGravado.getName(), productGravar.getName());
        Assertions.assertEquals(produtoGravado.getPrice(), productGravar.getPrice());
    }

    @Test
    @DisplayName("Gravando um produto sem Preço")
    void gravandoUmProdutoSemPreco() {

        Product productGravar = new Product();
        productGravar.setDescription("Descrição");
        productGravar.setName("Nome");

        validaExeptionAoGravarProduto(productGravar);
    }

    @Test
    @DisplayName("Gravando um produto sem Nome")
    void gravandoUmProdutoSemNome() {

        Product productGravar = new Product();
        productGravar.setDescription("Descrição");
        productGravar.setPrice(new BigDecimal("40.00"));

        validaExeptionAoGravarProduto(productGravar);
    }

    @Test
    @DisplayName("Gravando um produto sem Descrição")
    void gravandoUmProdutoSemDescricao() {

        Product productGravar = new Product();
        productGravar.setName("Nome");
        productGravar.setPrice(new BigDecimal("40.00"));

        validaExeptionAoGravarProduto(productGravar);
    }

    @Test
    @DisplayName("Gravando um produto com Preço negativo")
    void gravandoUmProdutoComPrecoNegativo() {

        Product productGravar = new Product();
        productGravar.setName("Nome");
        productGravar.setPrice(new BigDecimal("-44.00"));

        validaExeptionAoGravarProduto(productGravar);
    }

    @Test
    @DisplayName("Atualizando produto")
    void atualizandoUmProduto() {

        Product productGravar = mockProduto();

        Product produtoGravado = productService.save(productGravar);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setDescription("Descrição alterada");
        productDTO.setName("Nome alterado");
        productDTO.setPrice(new BigDecimal("88.29"));

        Product produtoAtualizado = productService.update(produtoGravado.getId(), productDTO);

        Assertions.assertNotNull(produtoAtualizado.getId());
        Assertions.assertEquals(produtoAtualizado.getDescription(), productDTO.getDescription());
        Assertions.assertEquals(produtoAtualizado.getName(), productDTO.getName());
        Assertions.assertEquals(produtoAtualizado.getPrice(), productDTO.getPrice());
    }

    @Test
    @DisplayName("Atualizando produto que não existe")
    void atualizandoUmProdutoQueNaoExiste() {

        Assertions.assertThrows(CustomExeption.class,
                () -> {
                    productService.update("0000000000000000000", null);
                });
    }

    @Test
    @DisplayName("Pesquisa produto pelo código")
    void findProdutoById() {

        Product productGravar = mockProduto();

        Product produtoGravado = productService.save(productGravar);

        Product produtoPesquisado = productService.findById(produtoGravado.getId());

        Assertions.assertNotNull(produtoPesquisado.getId());
        Assertions.assertEquals(produtoGravado.getId(), produtoPesquisado.getId());
        Assertions.assertEquals(produtoGravado.getDescription(), produtoPesquisado.getDescription());
        Assertions.assertEquals(produtoGravado.getName(), produtoPesquisado.getName());
        Assertions.assertEquals(produtoGravado.getPrice(), produtoPesquisado.getPrice());
    }

    @Test
    @DisplayName("Pesquisa produto com um código inválido")
    void pesquisaProdutoComCodigoIvalido() {

        CustomExeption thrown = Assertions.assertThrows(
                CustomExeption.class,
                () -> productService.findById("0000000000000")
        );

        Assertions.assertEquals(thrown.getError().getStatusCode(), HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Busca todos produtos")
    void buscaTodosProdutos() {

        Product productGravar = mockProduto();

        productService.save(productGravar);

        List<Product> products = productService.findAll();

        Assertions.assertTrue(!products.isEmpty());
    }

    @Test
    @DisplayName("Busca todos produtos lista vazia")
    void buscaTodosProdutosListaVazia() {

        productService.deleteAll();

        List<Product> products = productService.findAll();

        Assertions.assertTrue(products.isEmpty());
    }

    @Test
    @DisplayName("Deletando produto pelo código")
    void deletandoProdutoPeloCodigo() {

        //limpa a base
        productService.deleteAll();

        Product productGravar = mockProduto();

        Product productGravado = productService.save(productGravar);

        productService.deleteById(productGravado.getId());

        List<Product> products = productService.findAll();

        Assertions.assertTrue(products.isEmpty());
    }

    @Test
    @DisplayName("Deletando produto que não existe")
    void deletandoProdutoQueNaoExiste() {

        CustomExeption thrown = Assertions.assertThrows(
                CustomExeption.class,
                () -> productService.deleteById("0000000000000")
        );

        Assertions.assertEquals(thrown.getError().getStatusCode(), HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Buscado produtos pelo Name ou Description com valor válido")
    void buscaTodosProdutosPeloNameOuDescriptionComValorValido() {

        Product productGravar = mockProduto();

        productService.save(productGravar);

        List<Product> products = productService.findProducts(productGravar.getName(), null, null);

        Assertions.assertTrue(!products.isEmpty());

        products = productService.findProducts(productGravar.getDescription(), null, null);

        Assertions.assertTrue(!products.isEmpty());
    }

    @Test
    @DisplayName("Buscado produtos pelo Name ou Description com valor inválido")
    void buscaTodosProdutosPeloNameOuDescriptionComValorInvalido() {

        List<Product> products = productService.findProducts("AAAAAAAAAAA", null, null);

        Assertions.assertTrue(products.isEmpty());
    }

    @Test
    @DisplayName("Buscando produtos pelo MinPrice e MaxPrice com valor válido")
    void buscaTodosProdutosPeloMinPriceComValorValido() {

        Product productGravar = mockProduto();

        productService.save(productGravar);

        List<Product> products = productService.findProducts(null, productGravar.getPrice(), productGravar.getPrice());

        Assertions.assertTrue(!products.isEmpty());

        products = productService.findProducts(null, null, productGravar.getPrice());

        Assertions.assertTrue(!products.isEmpty());

        products = productService.findProducts(null, productGravar.getPrice(), null);

        Assertions.assertTrue(!products.isEmpty());
    }

    @Test
    @DisplayName("Buscando produtos pelo MinPrice e MaxPrice com valor inválido")
    void buscaTodosProdutosPeloMinPriceComValorInvalido() {

        Product productGravar = mockProduto();

        productService.save(productGravar);

        List<Product> products = productService.findProducts(null, new BigDecimal("10000"), new BigDecimal("10000"));

        Assertions.assertTrue(products.isEmpty());

        products = productService.findProducts(null, null, new BigDecimal("10000"));

        Assertions.assertTrue(!products.isEmpty());

        products = productService.findProducts(null, new BigDecimal("10000"), null);

        Assertions.assertTrue(products.isEmpty());

        products = productService.findProducts(null, null, null);

        Assertions.assertTrue(!products.isEmpty());
    }

    private void validaExeptionAoGravarProduto(Product productGravar) {
        Assertions.assertThrows(CustomExeption.class,
                () -> {
                    productService.save(productGravar);
                });
    }

    private Product mockProduto() {

        Product productGravar = new Product();

        productGravar.setDescription("Descrição");
        productGravar.setName("Nome");
        productGravar.setPrice(new BigDecimal("99.99"));

        return productGravar;
    }
}
