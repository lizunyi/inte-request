package com.weaver.inte.request.req;

import com.weaver.inte.request.auth.RequestAuthorization;
import com.weaver.inte.request.enums.RequestContentType;
import com.weaver.inte.request.enums.RequestMethod;
import com.weaver.inte.request.enums.RequestSchema;
import com.weaver.inte.request.https.MySSLSocketFactory;
import com.weaver.inte.request.https.MyTrustCerts;
import com.weaver.inte.request.https.MyTrustHostnameVerifier;
import com.weaver.inte.request.utils.RequestUtils;
import okhttp3.*;
import okhttp3.Request.Builder;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/***
 * Http网络请求构造器
 *
 * @author saps.weaver
 *
 */
public class RequestClient {

    private static final OkHttpClient.Builder builderHttp = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(240, TimeUnit.SECONDS).retryOnConnectionFailure(true);

    private static final OkHttpClient.Builder builderHttps = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(240, TimeUnit.SECONDS).sslSocketFactory(MySSLSocketFactory.build(), new MyTrustCerts())
            .hostnameVerifier(new MyTrustHostnameVerifier()).retryOnConnectionFailure(true);

    private String url;
    private OkHttpClient commonClient;
    private RequestMethod method = RequestMethod.GET;
    private RequestSchema schema;
    private RequestHeader header;
    private RequestBody body;
    private RequestAuthorization authorization;

    /***
     * URL,必选
     *
     * @param url
     * @return
     */
    public RequestClient url(String url) {
        this.url = url;
        return this;
    }

    /***
     * method,可选,默认:Get方式
     *
     * @param method
     * @return
     */
    public RequestClient method(RequestMethod method) {
        this.method = method;
        return this;
    }

    public RequestClient header(RequestHeader header) {
        this.header = header;
        return this;
    }


    public RequestClient followRedirects(boolean followRedirects) {
        if (schema == RequestSchema.https) {
            builderHttps.followRedirects(followRedirects);
        } else {
            builderHttp.followRedirects(followRedirects);
        }
        return this;
    }


    public RequestClient proxy(String sma, String host, int port) {
        Proxy proxy = new Proxy(Proxy.Type.valueOf(sma.toUpperCase()), new InetSocketAddress(host, port));
        if (schema == RequestSchema.https) {
            builderHttps.proxy(proxy);
        } else {
            builderHttp.proxy(proxy);
        }
        return this;
    }

    public RequestClient body(RequestBody body) {
        this.body = body;
        return this;
    }

    public RequestClient auth(RequestAuthorization authorization) {
        this.authorization = authorization;
        return this;
    }

    private Request common(RequestHeader header, RequestBody requestBody) throws Exception {
        okhttp3.RequestBody request = null;
        RequestContentType contentType = null;
        if (requestBody != null) {
            contentType = requestBody.contentType;
        } else {
            contentType = RequestContentType.FORM;
        }
        if (requestBody != null && !(RequestMethod.GET == method || RequestMethod.HEAD == method)) {
            if (RequestContentType.MULTIPART == contentType) {
                request = requestBody.getMultipartBody();
            } else if (RequestContentType.FORM == contentType) {
                request = okhttp3.RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), requestBody.getFormBody());
            } else if (RequestContentType.RAW == contentType) {
                request = okhttp3.RequestBody.create(requestBody.getMediaType(), requestBody.getRawBody());
            } else if (RequestContentType.BINARY == contentType) {
                request = okhttp3.RequestBody.create(MediaType.get("application/octet-stream"), requestBody.getBinaryBody());
            }
        }
        Builder builder = new Request.Builder().method(method.name(), request).url(url);
        if (header != null) {
            header.addHeader2Builder(builder);
        }
        return builder.build();
    }

    /***
     * 发送请求-同步
     *
     * @param header 请求头部
     * @param requestBody 请求数据
     * @return
     * @throws Exception
     */
    public Map<String, Object> send(RequestHeader header, RequestBody requestBody) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Request req = common(header, requestBody);
        Response res = getClient().newCall(req).execute();
        String responseBody = res.body().string();
        result.put("body", responseBody);
        Map<String, String> responseHeader = RequestUtils.getHeaderMap(res.headers());
        result.put("header", responseHeader);
        return result;
    }

    /***
     * 发送请求-同步
     * @return
     * @throws Exception
     */
    public String result() throws Exception {
        Request req = common(header, body);
        Response res = getClient().newCall(req).execute();
        return res.body().string();
    }

    /***
     * 发送请求-同步
     * @return
     * @throws Exception
     */
    public Map<String, Object> send() throws Exception {
        Map<String, Object> result = new HashMap<>();
        Request req = common(header, body);
        Response res = getClient().newCall(req).execute();
        String responseBody = res.body().string();
        result.put("body", responseBody);
        Map<String, String> responseHeader = RequestUtils.getHeaderMap(res.headers());
        result.put("header", responseHeader);
        return result;
    }

    public Response res() throws Exception {
        Request req = common(header, body);
        Response res = getClient().newCall(req).execute();
        return res;
    }

    /***
     * 发送请求-异步
     *
     * @param header 请求头部
     * @param requestBody   请求数据
     * @return
     * @throws Exception
     */
    public void send(RequestHeader header, RequestBody requestBody, Callback callback) throws Exception {
        Request req = common(header, requestBody);
        commonClient.newCall(req).enqueue(callback);
    }

    /***
     * 发送请求-异步
     * @return
     * @throws Exception
     */
    public void send(Callback callback) throws Exception {
        Request req = common(header, body);
        commonClient.newCall(req).enqueue(callback);
    }


    public RequestClient http() {
        schema = RequestSchema.http;
        return this;
    }

    public RequestClient https() {
        schema = RequestSchema.https;
        return this;
    }

    private OkHttpClient getClient() {
        if (commonClient == null) {
            if (schema == RequestSchema.https) {
                commonClient = builderHttps.build();
            } else {
                commonClient = builderHttp.build();
            }
        }
        return commonClient;
    }
}
