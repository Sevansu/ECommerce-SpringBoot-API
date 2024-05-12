package com.sp.product.Ecommerce.models;

public class JWTrequest {
	String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	String password;

	public JWTrequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public JWTrequest() {
		super();
	}
}
