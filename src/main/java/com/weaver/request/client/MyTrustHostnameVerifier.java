package com.weaver.request.client;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class MyTrustHostnameVerifier implements HostnameVerifier {
	@Override
	public boolean verify(String hostname, SSLSession session) {
		return true;
	}
}
