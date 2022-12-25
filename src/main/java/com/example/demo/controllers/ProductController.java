package com.example.demo.controllers;

import com.example.demo.models.Product;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    List<Product> products = new ArrayList<>(List.of(
            new Product(1, "A30", "Samsung"),
            new Product(2, "Iphone 11 Pro Max", "Apple"),
            new Product(3, "Acer Aspire", "Acer")
    ));

    @GetMapping
    public List<Product> getAll() {
        return products;
    }

    @GetMapping("/{id}")
    public Product getOne(@PathVariable("id") int id) {
        return products
                .stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping
    public void create(@RequestBody Product product) {
        products.add(product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        products
                .removeIf(product -> product.getId().equals(id));
    }
}
