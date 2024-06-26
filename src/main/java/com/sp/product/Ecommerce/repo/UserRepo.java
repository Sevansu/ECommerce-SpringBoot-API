package com.sp.product.Ecommerce.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sp.product.Ecommerce.models.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
	Optional<User> findByUsername(String username);
}