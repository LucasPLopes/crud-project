package br.com.challenge.api.web;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.querydsl.core.types.dsl.BooleanExpression;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import br.com.challenge.api.domain.Product;
import br.com.challenge.api.repository.querydsl.ProductPredicatesBuilder;
import br.com.challenge.api.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/product")
public class ProductResource {

    private ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        return ResponseEntity.ok(this.productService.create(product));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(this.productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(this.productService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        this.productService.delete(id);
        return ResponseEntity.ok("");
    }

    @PutMapping
    public ResponseEntity<Product> update(@RequestBody Product product) {
        return ResponseEntity.ok(this.productService.update(product));
    }

    @GetMapping("/querydsl")
    public ResponseEntity<Iterable<Product>> search(@RequestParam(value = "search") String search) {
        ProductPredicatesBuilder builder = new ProductPredicatesBuilder();

        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");

            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                System.out.println("" + matcher.group(1) + " " + matcher.group(2) + " " + matcher.group(3));
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
        }
        BooleanExpression exp = builder.build();
        if (exp == null)
            return ResponseEntity.ok(null);
        return ResponseEntity.ok(this.productService.search(exp));

    }
}