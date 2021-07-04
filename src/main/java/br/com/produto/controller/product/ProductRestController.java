package br.com.produto.controller.product;

import br.com.produto.util.AbstractController;
import br.com.produto.dto.product.ProductDTO;
import br.com.produto.service.product.ProductService;
import br.com.produto.util.CustomExeption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/products")
public class ProductRestController extends AbstractController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public @ResponseBody ResponseEntity<Object> saveProduct(@RequestBody ProductDTO productDTO) {

        try {

            return created(productService.save(productDTO));

        } catch (CustomExeption e) {

            return error(e.getError());
        }
    }

    @PutMapping(path = "{id}")
    public @ResponseBody ResponseEntity<Object> updateProduct(@PathVariable String id, @RequestBody ProductDTO productDTO) {

        try {

            return ok(productService.update(id, productDTO));

        } catch (CustomExeption e) {

            return error(e.getError());
        }
    }

    @GetMapping(path = "{id}")
    public @ResponseBody ResponseEntity<Object> findById(@PathVariable String id) {

        try {

            return ok(productService.findById(id));

        } catch (CustomExeption e) {

            return error(e.getError());
        }
    }

    @GetMapping
    public @ResponseBody ResponseEntity<Object> findByAll() {

        try {

            return ok(productService.findAll());

        } catch (CustomExeption e) {

            return error(e.getError());
        }
    }

    @GetMapping(path = "/search")
    public @ResponseBody ResponseEntity<Object> search(@RequestParam(name = "q", required = false) String q,
                             @RequestParam(name = "min_price", required = false) BigDecimal minPrice,
                             @RequestParam(name = "max_price", required = false) BigDecimal maxPrice) {

        try {

            return ok(productService.findProducts(q, minPrice, maxPrice));

        } catch (CustomExeption e) {

            return error(e.getError());
        }
    }

    @DeleteMapping(path = "{id}")
    public @ResponseBody ResponseEntity<Object> deleteById(@PathVariable String id) {

        try {

            productService.deleteById(id);

            return ok("");

        } catch (CustomExeption e) {

            return error(e.getError());
        }
    }
}
