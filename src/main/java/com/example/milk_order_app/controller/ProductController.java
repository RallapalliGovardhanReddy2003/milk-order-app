package com.example.milk_order_app.controller;

import com.example.milk_order_app.entity.AppUser;
import com.example.milk_order_app.entity.Product;
import com.example.milk_order_app.repository.ProductRepository;
import com.example.milk_order_app.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Transactional
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // ==========================================
    // ✅ CREATE PRODUCT
    // ==========================================
    @PostMapping("/saveproduct")
    public Product createProduct(@RequestBody Product product) {

        // Get logged-in username
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        product.setUser(user);

        return productRepository.save(product);
    }

    // ==========================================
    // ✅ GET ALL PRODUCTS
    // ==========================================
    @GetMapping("/getallproducts")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ==========================================
    // ✅ GET PRODUCT BY ID
    // ==========================================
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {

        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // ==========================================
    // ✅ UPDATE PRODUCT
    // ==========================================
    @PutMapping("/updating/{id}")
    public Product updateProduct(@PathVariable Long id,
                                 @RequestBody Product updatedProduct) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setProductName(updatedProduct.getProductName());
        product.setProductImage(updatedProduct.getProductImage());
        product.setQuantity(updatedProduct.getQuantity());
        product.setPrice(updatedProduct.getPrice());
        product.setDescription(updatedProduct.getDescription());
        product.setDeliveryAddress(updatedProduct.getDeliveryAddress());

        return productRepository.save(product);
    }

    // ==========================================
    // ✅ DELETE PRODUCT
    // ==========================================
    @DeleteMapping("/deleting/{id}")
    public String deleteProduct(@PathVariable Long id) {

        productRepository.deleteById(id);

        return "Product deleted successfully";
    }
}