package com.sp.product.Ecommerce.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sp.product.Ecommerce.config.JwtUtil;
import com.sp.product.Ecommerce.models.Category;
import com.sp.product.Ecommerce.models.Product;
import com.sp.product.Ecommerce.models.User;
import com.sp.product.Ecommerce.repo.CartProductRepo;
import com.sp.product.Ecommerce.repo.CartRepo;
import com.sp.product.Ecommerce.repo.CategoryRepo;
import com.sp.product.Ecommerce.repo.ProductRepo;
import com.sp.product.Ecommerce.repo.UserRepo;

@RestController
@RequestMapping("/api/auth/seller")
public class SellerController {

	@Autowired
	ProductRepo productRepo;
	@Autowired
	CartRepo cartRepo;
	@Autowired
	CategoryRepo categoryRepo;
	@Autowired
	UserRepo userRepo;
	@Autowired
	CartProductRepo cpRepo;
	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/product")
	public ResponseEntity<Object> postProduct(@RequestHeader("JWT") String jwt, @RequestBody Product product) {
		User user = userRepo.findByUsername(jwtUtil.extractUsername(jwt)).orElse(null);
		if (user == null) {
			return ResponseEntity.badRequest().body("No user found");
		}
		Category category = categoryRepo.findByCategoryName(product.getCategory().getCategoryName()).get();
		if (category == null) {
			return ResponseEntity.badRequest().body("No user found");
		}
		product.setSeller(user);
		product.setCategory(category);
		productRepo.saveAndFlush(product);
		return ResponseEntity.ok(product);
//		return ResponseEntity.status(HttpStatus.CREATED).body("http://localhost/api/auth/seller/product/" + product.getProductId());

	}

	@GetMapping("/product")
	public ResponseEntity<Object> getAllProducts(@RequestHeader("JWT") String jwt) {
		String username = jwtUtil.extractUsername(jwt);
		User user = userRepo.findByUsername(username).orElse(null);
		if (user == null) {
			return ResponseEntity.badRequest().body("No user found");
		}
		List<Product> list = productRepo.findBySellerUserId(user.getUserId());
		return ResponseEntity.ok(list);
	}

	@GetMapping("/product/{productId}")
	public ResponseEntity<Object> getProduct(@RequestHeader("JWT") String jwt, @PathVariable Integer productId) {
		String username = jwtUtil.extractUsername(jwt);
		User user = userRepo.findByUsername(username).orElse(null);
		if (user == null) {
			return ResponseEntity.badRequest().body("No user found");
		}
		Product prod = productRepo.findBySellerUserIdAndProductId(user.getUserId(), productId).orElse(null);
		return ResponseEntity.ok(prod);
	}

	@PutMapping("/product")
	public ResponseEntity<Object> putProduct(@RequestHeader("JWT") String jwt, @RequestBody Product product) {
		String username = jwtUtil.extractUsername(jwt);
		User user = userRepo.findByUsername(username).orElse(null);
		if (user == null) {
			return ResponseEntity.badRequest().body("No user found");
		}
		Product prod = productRepo.findBySellerUserIdAndProductId(user.getUserId(), product.getProductId())
				.orElse(null);
		Category category = categoryRepo.findByCategoryName(product.getCategory().getCategoryName()).get();
		if (prod == null) {
			return ResponseEntity.badRequest().body("No product found");
		}
		if (category == null) {
			return ResponseEntity.badRequest().body("No Category found");
		}
		prod.setProductName(product.getProductName());
		prod.setPrice(product.getPrice());
		prod.setCategory(category);
		productRepo.saveAndFlush(prod);
		return ResponseEntity.ok(prod);
	}

	@DeleteMapping("/product/{productId}")
	public ResponseEntity<Product> deleteProduct(@RequestHeader("JWT") String jwt, @PathVariable Integer productId) {
		String username = jwtUtil.extractUsername(jwt);
		User user = userRepo.findByUsername(username).orElse(null);
		if (user == null) {
			return ResponseEntity.badRequest().body(null);
		}
		System.out.println(productId);
		Product product = productRepo.findBySellerUserIdAndProductId(user.getUserId(), productId).orElse(null);
		if (product == null) {
			return ResponseEntity.status(404).body(null);
		}
		productRepo.delete(product);
		return ResponseEntity.ok(product);

	}
}