package com.weaver.request.client;

import java.util.concurrent.TimeUnit;

import com.weaver.request.RequestAuthorization;
import com.weaver.request.RequestBody;
import com.weaver.request.RequestHeader;
import com.weaver.request.constants.RequestContentType;
import com.weaver.request.constants.RequestMethod;

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

	protected OkHttpClient http = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
			.readTimeout(3, TimeUnit.MINUTES).build();

	protected OkHttpClient https = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
			.readTimeout(3, TimeUnit.MINUTES).sslSocketFactory(MySSLSocketFactory.build())
			.hostnameVerifier(new MyTrustHostnameVerifier()).build();

	protected String url;
	protected RequestMethod method = RequestMethod.GET;
	protected RequestContentType contentType = RequestContentType.NONE;

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
	public HttpClient method(RequestMethod method) {
		this.method = method;
		return this;
	}
	public HttpClient method(int method) {
		return method(RequestMethod.values()[method]);
	}

	/***
	 * contentType,可选,默认:none
	 * 
	 * @param contentType
	 * @return
	 */
	public HttpClient contentType(RequestContentType contentType) {
		this.contentType = contentType;
		return this;
	}
	public HttpClient contentType(int contentType) {
		return contentType(RequestContentType.values()[contentType]);
	}

	protected MediaType getMediaType() {
		MediaType mediaType = null;

		if (RequestContentType.NONE == contentType) {// none
			mediaType = MediaType.parse("text/plain");
		} else if (RequestContentType.MULTIPART == contentType) {// form-data

		} else if (RequestContentType.FORM == contentType) {// x-www-form-urlencoded
			mediaType = MediaType.parse("application/x-www-form-urlencoded");
		} else if (RequestContentType.RAW == contentType) {// raw

		} else if (RequestContentType.BINARY == contentType) {// binary

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