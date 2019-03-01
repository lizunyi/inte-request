package com.weaver.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weaver.request.client.HttpClient;
import com.weaver.request.constants.RequestMethod;

import net.sf.json.JSONObject;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

/***
 * 
 * @author saps.weaver
 *
 */
public class RequestClient extends HttpClient {

	private Logger logger = LoggerFactory.getLogger(RequestClient.class);

	private Request common(RequestHeader header, RequestBody requestBody) throws Exception {
		String $med = RequestMethod.values()[method].name();
		okhttp3.RequestBody request = null;

		logger.debug("url:" + url);
		logger.debug("method:" + $med);

		if (requestBody != null && !(RequestMethod.GET.ordinal() == method || RequestMethod.HEAD.ordinal() == method)) {
			if (2 == contentType) {
				request = requestBody.getMultipartBody();
			} else if (3 == contentType) {
				request = okhttp3.RequestBody.create(getMediaType(), requestBody.getFormBody());
			} else if (4 == contentType) {
				request = okhttp3.RequestBody.create(requestBody.getMediaType(), requestBody.getRawBody());
			}
		}
		Builder builder = new Request.Builder().method($med, request).url(url);
		if (header != null) {
			header.build(builder);
		}
		return builder.build();
	}

	/***
	 * 发送请求-同步
	 * 
	 * @param header 请求头部
	 * @param body   请求数据
	 * @return
	 * @throws Exception
	 */
	public JSONObject send(RequestHeader header, RequestBody requestBody) throws Exception {
		JSONObject result = new JSONObject();
		Request req = common(header, requestBody);
		Response res = https.newCall(req).execute();
		String responseBody = res.body().string();
		result.put("body", responseBody);
		JSONObject responseHeader = ResponseHeader.getJson(res);
		result.put("header", responseHeader);
		return result;
	}

	/***
	 * 发送请求-同步
	 * 
	 * @param header 请求头部
	 * @param body   请求数据
	 * @return
	 * @throws Exception
	 */
	public JSONObject send() throws Exception {
		JSONObject result = new JSONObject();
		Request req = common(header, body);
		Response res = https.newCall(req).execute();
		String responseBody = res.body().string();
		result.put("body", responseBody);
		JSONObject responseHeader = ResponseHeader.getJson(res);
		result.put("header", responseHeader);
		return result;
	}

	/***
	 * 发送请求-异步
	 * 
	 * @param header 请求头部
	 * @param body   请求数据
	 * @return
	 * @throws Exception
	 */
	public void send(RequestHeader header, RequestBody requestBody, Callback callback) throws Exception {
		Request req = common(header, requestBody);
		https.newCall(req).enqueue(callback);
	}

	/***
	 * 发送请求-异步
	 * 
	 * @param header 请求头部
	 * @param body   请求数据
	 * @return
	 * @throws Exception
	 */
	public void send(Callback callback) throws Exception {
		Request req = common(header, body);
		https.newCall(req).enqueue(callback);
	}
}
