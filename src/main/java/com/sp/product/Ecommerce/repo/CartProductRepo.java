package com.sp.product.Ecommerce.repo;

import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sp.product.Ecommerce.models.CartProduct;

@Repository
public interface CartProductRepo extends JpaRepository<CartProduct, Integer> {
	Optional<CartProduct> findByCartUserUserIdAndProductProductId(Integer userId, Integer productId);

	@Transactional
	void deleteByCartUserUserIdAndProductProductId(Integer userId, Integer productId);
}