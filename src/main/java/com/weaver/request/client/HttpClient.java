package com.weaver.request.client;

import java.util.concurrent.TimeUnit;

import com.weaver.request.RequestAuthorization;
import com.weaver.request.RequestBody;
import com.weaver.request.RequestHeader;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/***
 * Http网络请求构造器
 * 
 * @author saps.weaver
 *
 */
public abstract class HttpClient {
	protected RequestHeader header;
	protected RequestBody body;
	protected RequestAuthorization auth;

	protected OkHttpClient http = new OkHttpClient().newBuilder().connectTimeout(20, TimeUnit.SECONDS)
			.readTimeout(10, TimeUnit.MINUTES).build();

	protected OkHttpClient https = new OkHttpClient().newBuilder().connectTimeout(20, TimeUnit.SECONDS)
			.readTimeout(10, TimeUnit.MINUTES).sslSocketFactory(MySSLSocketFactory.build())
			.hostnameVerifier(new MyTrustHostnameVerifier()).build();

	protected String url;
	protected int method = 1;
	protected int contentType = 1;

	/***
	 * URL,必选
	 * 
	 * @param url
	 * @return
	 */
	public HttpClient url(String url) {
		this.url = url;
		return this;
	}

	/***
	 * method,可选,默认:Get方式
	 * 
	 * @param method
	 * @return
	 */
	public HttpClient method(int method) {
		this.method = method;
		return this;
	}

	/***
	 * contentType,可选,默认:none
	 * 
	 * @param contentType
	 * @return
	 */
	public HttpClient contentType(int contentType) {
		this.contentType = contentType;
		return this;
	}

	protected MediaType getMediaType() {
		MediaType mediaType = null;
		if (1 == contentType) {// none
			mediaType = MediaType.parse("text/plain");
		} else if (2 == contentType) {// form-data

		} else if (3 == contentType) {// x-www-form-urlencoded
			mediaType = MediaType.parse("application/x-www-form-urlencoded");
		} else if (4 == contentType) {// raw

		} else if (5 == contentType) {// binary

		}
		return mediaType;
	}
 

	public HttpClient setHeader(RequestHeader header) {
		this.header = header;
		return this;
	}
 

	public HttpClient setBody(RequestBody body) {
		this.body = body;
		return this;
	}

	public HttpClient setAuth(RequestAuthorization auth) {
		this.auth = auth;
		return this;
	}

}
