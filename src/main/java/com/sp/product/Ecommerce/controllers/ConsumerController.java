package com.sp.product.Ecommerce.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sp.product.Ecommerce.config.JwtUtil;
import com.sp.product.Ecommerce.models.Cart;
import com.sp.product.Ecommerce.models.CartProduct;
import com.sp.product.Ecommerce.models.Product;
import com.sp.product.Ecommerce.models.User;
import com.sp.product.Ecommerce.repo.CartProductRepo;
import com.sp.product.Ecommerce.repo.CartRepo;
import com.sp.product.Ecommerce.repo.CategoryRepo;
import com.sp.product.Ecommerce.repo.ProductRepo;
import com.sp.product.Ecommerce.repo.UserRepo;

@RestController
@RequestMapping("/api/auth/consumer")
public class ConsumerController {

	@Autowired
	CartRepo cartRepo;
	@Autowired
	CategoryRepo categoryRepo;
	@Autowired
	UserRepo userRepo;
	@Autowired
	ProductRepo productRepo;
	@Autowired
	CartProductRepo cpRepo;
	@Autowired
	private JwtUtil jwtUtil;

	@GetMapping("/cart")
	public ResponseEntity<Object> getCart(@RequestHeader("JWT") String jwt) {
		String username = jwtUtil.extractUsername(jwt);
		System.out.println(cartRepo.findByUserUsername(username).toString());

		return ResponseEntity.ok(cartRepo.findByUserUsername(username));
	}

	@PostMapping("/cart")
	public ResponseEntity<Object> postCart(@RequestHeader("JWT") String jwt, @RequestBody Product product) {
		String username = jwtUtil.extractUsername(jwt);
		Cart cart = cartRepo.findByUserUsername(username).get();
		Product prod = productRepo.findById(product.getProductId()).get();
		if (!cart.getCartProducts().stream().anyMatch(n -> n.getProduct().equals(prod))) {
			CartProduct cp = new CartProduct();
			cp.setCart(cart);
			cp.setProduct(prod);
			cp.setQuantity(1);
			cart.getCartProducts().add(cp);
			cart.updateTotalAmount(prod.getPrice() * cp.getQuantity());
			cartRepo.save(cart);
			return ResponseEntity.ok(cart);
		} else {
			return ResponseEntity.status(409).build();
		}
	}

	@PutMapping("/cart")
	public ResponseEntity<Object> putCart(@RequestHeader("JWT") String jwt, @RequestBody CartProduct cartProd) {
		String username = jwtUtil.extractUsername(jwt);
		User user = userRepo.findByUsername(username).orElse(null);
		Cart cart = cartRepo.findByUserUsername(username).orElse(null);
		if (user == null) {
			return ResponseEntity.badRequest().body("No user found");
		}
		Product prod = productRepo.findById(cartProd.getProduct().getProductId()).orElse(null);
		if (prod == null) {
			return ResponseEntity.badRequest().body("No product found");
		}
		CartProduct cp = cart.getCartProducts().stream().filter(n -> n.getProduct().equals(prod)).findFirst()
				.orElse(null);
		if (cp == null) {
			if (cartProd.getQuantity() > 0) {
				cp = new CartProduct();
				cp.setProduct(prod);
				cp.setQuantity(cartProd.getQuantity());
				cart.updateTotalAmount(prod.getPrice() * cartProd.getQuantity());
				cp.setCart(cart);
				cart.getCartProducts().add(cp);
			}
		} else {
			if (cartProd.getQuantity() == 0) {
				cart.getCartProducts().remove(cp);
				System.out.println(prod.getPrice() + "abc " + cartProd.getQuantity());
				cart.updateTotalAmount(-prod.getPrice() * cp.getQuantity());
				cpRepo.delete(cp);
			} else {
				cart.updateTotalAmount(prod.getPrice() * (cartProd.getQuantity() - cp.getQuantity()));
				cart.getCartProducts().get(cart.getCartProducts().indexOf(cp)).setQuantity(cartProd.getQuantity());
			}
		}
		cartRepo.saveAndFlush(cart);
		return ResponseEntity.ok(cart);

	}

	@DeleteMapping("/cart")
	public ResponseEntity<Object> deleteCart(@RequestHeader("JWT") String jwt, @RequestBody Product prod) {
		String username = jwtUtil.extractUsername(jwt);
		Cart cart = cartRepo.findByUserUsername(username).orElse(null);
		if (cart == null) {
			return ResponseEntity.badRequest().body("No cart found");
		}
		CartProduct cp = cart.getCartProducts().stream()
				.filter(n -> n.getProduct().getProductId().equals(prod.getProductId())).findFirst().orElse(null);
		if (cp == null) {
			return ResponseEntity.badRequest().body("No product found");
		}
		cart.getCartProducts().remove(cp);
		cart.updateTotalAmount(-cp.getProduct().getPrice() * cp.getQuantity());
		cpRepo.delete(cp);
		cartRepo.save(cart);
		return ResponseEntity.ok(cart);
	}

}