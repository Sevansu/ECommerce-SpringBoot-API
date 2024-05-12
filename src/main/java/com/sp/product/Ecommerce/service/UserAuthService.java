package com.sp.product.Ecommerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sp.product.Ecommerce.models.User;
import com.sp.product.Ecommerce.repo.UserRepo;

@Service
public class UserAuthService implements UserDetailsService {
	@Autowired
	private UserRepo userRepo;

	public User loadUserByUserID(Integer id) {
		Optional<User> user = userRepo.findById(id);
		if (user.isPresent())
			return user.get();
		else
			throw new UsernameNotFoundException("User ID not found");
	}

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepo.findByUsername(username);
		if (user.isPresent())
			return user.get();
		else
			throw new UsernameNotFoundException("User ID not found");
	}
}
