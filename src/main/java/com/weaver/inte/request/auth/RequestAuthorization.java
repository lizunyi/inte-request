package com.weaver.inte.request.auth;

import java.util.Base64;

public class RequestAuthorization {

	public String bearerToken(String token) {
		return "Bearer " + token;
	}

	public String basicAuth(String username, String password) {
		return Base64.getEncoder().encodeToString(("Basic " + username + ":" + password).getBytes());
	}
}