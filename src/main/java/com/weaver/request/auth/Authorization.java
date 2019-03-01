package com.weaver.request.auth;

import java.util.Base64;

/***
 * type:1 Bearer Token 
 * type:2 Basic Auth 
 * type:3 Digest Auth 
 * type:4 OAuth 1.0
 * type:5 OAuth 2.0 
 * type:6 Hawk Auth 
 * type:7 AWS Signature 
 * type:8 NTLM Auth
 * 
 * @author Saps.Weaver
 *
 */
public class Authorization {

	public static String auth(String token) {
		return "Bearer " + token;
	}

	public static String auth(int type, String username, String password) {
		switch (type) {
		case 1:
			return Base64.getEncoder().encodeToString(("Basic " + username + ":" + password).getBytes());
		case 2:
			return "";
		}
		return "";
	}
}
