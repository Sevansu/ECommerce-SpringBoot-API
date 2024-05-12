package com.sp.product.Ecommerce.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sp.product.Ecommerce.models.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {
	Optional<Category> findByCategoryName(String category);
}