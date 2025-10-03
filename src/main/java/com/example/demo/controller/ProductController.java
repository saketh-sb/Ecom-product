package com.example.demo.controller;



import com.example.demo.entities.Product;
import com.example.demo.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return service.saveProduct(product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable("id") Long id) {
        return service.getProductById(id);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
        return service.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        service.deleteProduct(id);
        return "Product deleted successfully!";
    }
    
    @PutMapping("/{id}/reduceStock/{qty}")
    public String reduceStock(@PathVariable("id") Long id, @PathVariable("qty") int qty) {
        service.reduceStock(id, qty);
        return "Stock reduced!";
    }

    @PutMapping("/{id}/increaseStock/{qty}")
    public String increaseStock(@PathVariable("id") Long id, @PathVariable("qty") int qty) {
        service.increaseStock(id, qty);
        return "Stock increased!";
    }

    
}

