package com.cimb.tokolapak.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cimb.tokolapak.dao.EmployeeRepo;
import com.cimb.tokolapak.dao.ProductRepo;
import com.cimb.tokolapak.entity.Employee;
import com.cimb.tokolapak.entity.Product;
import com.cimb.tokolapak.service.ProductService;

@RestController
@CrossOrigin
public class ProductController {
	
	@Autowired
	private ProductRepo productRepo;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private EmployeeRepo employeeRepo;
	
	@GetMapping("/products")
	public Iterable<Product> getProducts() {
		return productService.getProducts();
	}
	
	@GetMapping("/products/{id}")
	public Product getProductById(@PathVariable int id) {
		return productService.getProductById(id).get();
	}
	
	@PostMapping("/products")
	public Product addProduct(@RequestBody Product product) {
		return productService.addProduct(product);
	}
	
	@DeleteMapping("/products/{id}")
	public void deleteProductById(@PathVariable int id) {
		productService.deleteProductById(id);
	}
	
	@PutMapping("/products")
	public Product updateProduct(@RequestBody Product product) {
		return productService.updateProduct(product);
	}
	
	
	@GetMapping("/productName/{productName}")
	public Product getProductByProductName(@PathVariable String productName) {
		return productRepo.findByProductName(productName);
	}

	@GetMapping("products/custom")
	public Iterable<Product> customQueryGet(@RequestParam double maxPrice, @RequestParam String productName) {
		return productRepo.findProductByMaxPrice(maxPrice, productName);
	}
	
}
