package com.sp.product.Ecommerce.models;

public class JWTresponse {
	String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public JWTresponse(String token) {
		super();
		this.token = token;
	}

	public JWTresponse() {
		super();
	}
}
