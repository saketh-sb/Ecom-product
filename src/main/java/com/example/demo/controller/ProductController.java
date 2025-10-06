package com.example.demo.controller;

import com.example.demo.entities.Product;
import com.example.demo.service.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "Product API", description = "Product management and inventory operations")
public class ProductController {
	
	private final ProductService service;

	public ProductController(ProductService service) {
		this.service = service;
	}

	@Operation(
		summary = "Create a new product",
		description = "Add a new product to the inventory"
	)
	@ApiResponse(
		responseCode = "201",
		description = "Product created successfully",
		content = @Content(schema = @Schema(implementation = Product.class))
	)
	@ApiResponse(responseCode = "400", description = "Invalid product data")
	@PostMapping
	public ResponseEntity<Product> addProduct(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
				description = "Product object to create",
				required = true,
				content = @Content(
					schema = @Schema(implementation = Product.class),
					examples = @ExampleObject(
						value = "{ \"name\": \"Laptop\", \"description\": \"High-performance laptop\", \"price\": 999.99, \"stockQuantity\": 50 }"
					)
				)
			)
			@Valid @RequestBody Product product) {
		Product savedProduct = service.saveProduct(product);
		return ResponseEntity.status(201).body(savedProduct);
	}

	@Operation(
		summary = "Get all products",
		description = "Retrieve a list of all products in the inventory"
	)
	@ApiResponse(
		responseCode = "200",
		description = "List of products retrieved successfully"
	)
	@GetMapping
	public List<Product> getAllProducts() {
		return service.getAllProducts();
	}

	@Operation(
		summary = "Get product by ID",
		description = "Retrieve a specific product using its ID"
	)
	@ApiResponse(
		responseCode = "200",
		description = "Product found",
		content = @Content(schema = @Schema(implementation = Product.class))
	)
	@ApiResponse(responseCode = "404", description = "Product not found")
	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(
			@Parameter(description = "ID of the product to retrieve", required = true, example = "1")
			@PathVariable("id") Long id) {
		Product product = service.getProductById(id);
		return ResponseEntity.ok(product);
	}

	@Operation(
		summary = "Update product",
		description = "Update an existing product's information"
	)
	@ApiResponse(
		responseCode = "200",
		description = "Product updated successfully",
		content = @Content(schema = @Schema(implementation = Product.class))
	)
	@ApiResponse(responseCode = "404", description = "Product not found")
	@ApiResponse(responseCode = "400", description = "Invalid product data")
	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(
			@Parameter(description = "ID of the product to update", required = true, example = "1")
			@PathVariable("id") Long id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
				description = "Updated product object",
				required = true,
				content = @Content(
					schema = @Schema(implementation = Product.class),
					examples = @ExampleObject(
						value = "{ \"name\": \"Gaming Laptop\", \"description\": \"High-end gaming laptop\", \"price\": 1499.99, \"stockQuantity\": 30 }"
					)
				)
			)
			@Valid @RequestBody Product product) {
		Product updatedProduct = service.updateProduct(id, product);
		return ResponseEntity.ok(updatedProduct);
	}

	@Operation(
		summary = "Delete product",
		description = "Delete a product from the inventory"
	)
	@ApiResponse(responseCode = "200", description = "Product deleted successfully")
	@ApiResponse(responseCode = "404", description = "Product not found")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(
			@Parameter(description = "ID of the product to delete", required = true, example = "1")
			@PathVariable("id") Long id) {
		service.deleteProduct(id);
		return ResponseEntity.ok("Product deleted successfully!");
	}

	// --------------------- Inventory Management Operations ---------------------

	@Operation(
		summary = "Reduce product stock",
		description = "Decrease the stock quantity of a product (e.g., after a sale)"
	)
	@ApiResponse(responseCode = "200", description = "Stock reduced successfully")
	@ApiResponse(responseCode = "404", description = "Product not found")
	@ApiResponse(responseCode = "400", description = "Insufficient stock or invalid quantity")
	@PutMapping("/{id}/reduceStock/{qty}")
	public ResponseEntity<String> reduceStock(
			@Parameter(description = "ID of the product", required = true, example = "1")
			@PathVariable("id") Long id,
			@Parameter(description = "Quantity to reduce", required = true, example = "5")
			@PathVariable("qty") int qty) {
		service.reduceStock(id, qty);
		return ResponseEntity.ok("Stock reduced!");
	}

	@Operation(
		summary = "Increase product stock",
		description = "Increase the stock quantity of a product (e.g., after restocking)"
	)
	@ApiResponse(responseCode = "200", description = "Stock increased successfully")
	@ApiResponse(responseCode = "404", description = "Product not found")
	@ApiResponse(responseCode = "400", description = "Invalid quantity")
	@PutMapping("/{id}/increaseStock/{qty}")
	public ResponseEntity<String> increaseStock(
			@Parameter(description = "ID of the product", required = true, example = "1")
			@PathVariable("id") Long id,
			@Parameter(description = "Quantity to add", required = true, example = "10")
			@PathVariable("qty") int qty) {
		service.increaseStock(id, qty);
		return ResponseEntity.ok("Stock increased!");
	}
}