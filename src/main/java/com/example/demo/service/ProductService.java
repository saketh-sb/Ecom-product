package com.example.demo.service;



import com.example.demo.entities.Product;
import com.example.demo.exception.OutOfStockException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product saveProduct(Product product) {
        return repository.save(product);
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product getProductById(Long id) {
        return repository.findById(id) .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        if (product == null) return null;

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStockQuantity(productDetails.getStockQuantity());
        return repository.save(product);
    }

    public void deleteProduct(Long id) {
    	Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        repository.delete(product);
       
    }
    
    public void reduceStock(Long productId, int qty) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));

        if (product.getStockQuantity() < qty) {
            throw new OutOfStockException("Not enough stock for product " + product.getName());
        }

        product.setStockQuantity(product.getStockQuantity() - qty);
        repository.save(product);
    }

    public void increaseStock(Long productId, int qty) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));

        product.setStockQuantity(product.getStockQuantity() + qty);
        repository.save(product);
    }

    
}

