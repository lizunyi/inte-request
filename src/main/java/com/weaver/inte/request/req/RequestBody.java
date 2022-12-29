package com.weaver.inte.request.req;

import com.weaver.inte.request.enums.RequestContentType;
import com.weaver.inte.request.enums.RequestRawFormat;
import com.weaver.inte.utils.StringExtUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody.Builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestBody {

    private List<Map<String, Object>> params = new ArrayList<>();
    private String rowBody = "";
    private RequestRawFormat rawFormat = null;
    public RequestContentType contentType = null;

    public RequestBody add(String key, Object value) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("value", value);
        params.add(map);
        if (contentType == null)
            contentType = RequestContentType.FORM;
        return this;
    }

    public RequestBody addFile(String key, String fileName, byte[] value) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("fileName", fileName);
        map.put("value", value);
        params.add(map);
        contentType = RequestContentType.MULTIPART;
        return this;
    }

    public RequestBody raw(RequestRawFormat rawFormat, String rowBody) {
        this.rawFormat = rawFormat;
        this.rowBody = rowBody;
        if (contentType == null)
            contentType = RequestContentType.RAW;
        return this;
    }

    public RequestBody binary(byte[] value) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("value", value);
        params.add(map);
        if (contentType == null)
            contentType = RequestContentType.BINARY;
        return this;
    }

    String getFormBody() {
        String result = "";
        if (params != null && params.size() > 0) {
            result = params.stream().map(x -> {
                String key = StringExtUtils.ifNull(x.get("key"));
                String value = StringExtUtils.ifNull(x.get("value"));
                return key + "=" + value;
            }).collect(Collectors.joining("&"));
        }
        return result;
    }

    String getRawBody() {
        return rowBody;
    }

    byte[] getBinaryBody() {
        return (byte[]) params.get(0).get("value");
    }

    okhttp3.RequestBody getMultipartBody() {
        Builder build = new Builder();
        build.setType(MediaType.parse("multipart/form-data"));
        if (params != null && params.size() > 0) {
            for (Map param : params) {
                String key = StringExtUtils.ifNull(param.get("key"));
                if (param.containsKey("fileName")) {
                    //文件
                    String fileName = StringExtUtils.ifNull(param.get("fileName"));
                    byte[] value = (byte[]) param.get("value");
                    build.addFormDataPart(key, fileName, okhttp3.RequestBody.create(MediaType.parse("application/octet-stream"), value));
                } else {
                    // 字符串
                    String value = StringExtUtils.ifNull(param.get("value"));
                    build.addFormDataPart(key, value);
                }
            }
        }
        return build.build();
    }

    MediaType getMediaType() {
        MediaType mediaType = null;
        // raw
        if (RequestRawFormat.TEXT == rawFormat) {
            mediaType = MediaType.parse("text/plain");
        } else if (RequestRawFormat.JSON == rawFormat) {
            mediaType = MediaType.parse("application/json");
        } else if (RequestRawFormat.JAVASCRIPT == rawFormat) {
            mediaType = MediaType.parse("application/javascript");
        } else if (RequestRawFormat.APPLICATIONXML == rawFormat) {
            mediaType = MediaType.parse("application/xml");
        } else if (RequestRawFormat.TEXTXML == rawFormat) {
            mediaType = MediaType.parse("text/xml");
        } else if (RequestRawFormat.HTML == rawFormat) {
            mediaType = MediaType.parse("text/html");
        }
        return mediaType;
    }
}
