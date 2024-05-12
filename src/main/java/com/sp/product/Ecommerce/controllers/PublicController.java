package com.sp.product.Ecommerce.controllers;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sp.product.Ecommerce.config.JwtUtil;
import com.sp.product.Ecommerce.models.JWTrequest;
import com.sp.product.Ecommerce.models.Product;
import com.sp.product.Ecommerce.repo.ProductRepo;

@RestController
@RequestMapping("/api/public")
public class PublicController {

	@Autowired
	AuthenticationManager auth;

	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	ProductRepo prodRepo;

	@GetMapping("/product/search")
	public List<Product> getProducts(@RequestParam("keyword") String keyword) {
 		return prodRepo.findByProductNameContainingIgnoreCaseOrCategoryCategoryNameContainingIgnoreCase(keyword, keyword);
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody JWTrequest request) {
 		try {
			auth.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		String token = jwtUtil.generateToken(request.getUsername());
		return ResponseEntity.ok(token);
	}

}