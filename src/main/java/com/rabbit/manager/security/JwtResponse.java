package com.rabbit.manager.security;

import com.rabbit.manager.model.User;

public class JwtResponse {
    private String token;
    private User user;

    public String getToken() {
        return this.token;
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public JwtResponse(String token, User user) {
		super();
		this.token = token;
		this.user = user;
	}
}

