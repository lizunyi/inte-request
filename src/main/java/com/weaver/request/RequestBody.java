package com.weaver.request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weaver.request.util.StringUtil;

import okhttp3.MediaType;
import okhttp3.MultipartBody.Builder;

/***
 * 构造网络请求参数类
 * 
 * @author saps.weaver
 *
 */
public class RequestBody {

	private Logger logger = LoggerFactory.getLogger(RequestClient.class);
	private List<Map> params = new ArrayList();
	private String rowBody = "";
	private int rawFormat = 0;

	public void add(String $key, Object $value) {
		Map map = new HashMap();
		map.put("key", $key);
		map.put("value", $value);
		params.add(map);
	}

	public void addFile(String $key, String fileName, byte[] $value) {
		Map map = new HashMap();
		map.put("key", $key);
		map.put("fileName", fileName);
		map.put("value", $value);
		params.add(map);
	}

	public void set(int rawFormat, String rowBody) {
		this.rowBody = rowBody;
	}

	String getFormBody() {
		StringBuffer sb = new StringBuffer();
		String result = "";
		if (params != null && params.size() > 0) {
			for (Map param : params) {
				String key = StringUtil.ifNull(param.get("key"));
				String value = StringUtil.ifNull(param.get("value"));
				sb.append(String.format("&%s=%s", key, value));
			}
			result = sb.substring(1);
		}
		logger.debug("body:" + result);
		return result;
	}

	String getRawBody() {
		logger.debug("body:" + rowBody);
		return rowBody;
	}

	okhttp3.RequestBody getMultipartBody() throws IOException {
		Builder build = new Builder();
		build.setType(MediaType.parse("multipart/form-data"));
		if (params != null && params.size() > 0) {
			for (Map param : params) {
				String key = StringUtil.ifNull(param.get("key"));
				if (param.containsKey("fileName")) {// 文件
					String fileName = StringUtil.ifNull(param.get("fileName"));
					byte[] value = (byte[]) param.get("value");
					build.addFormDataPart(key, fileName,
							okhttp3.RequestBody.create(MediaType.parse("application/octet-stream"), value));
				} else {// 字符串
					String value = StringUtil.ifNull(param.get("value"));
					build.addFormDataPart(key, value);
				}
			}
		}
		return build.build();
	}

	MediaType getMediaType() {
		MediaType mediaType = null;
		// raw
		if (1 == rawFormat) {
			mediaType = MediaType.parse("text/plain");
		} else if (2 == rawFormat) {
			mediaType = MediaType.parse("application/json");
		} else if (3 == rawFormat) {
			mediaType = MediaType.parse("application/javascript");
		} else if (4 == rawFormat) {
			mediaType = MediaType.parse("application/xml");
		} else if (5 == rawFormat) {
			mediaType = MediaType.parse("text/xml");
		} else if (6 == rawFormat) {
			mediaType = MediaType.parse("text/html");
		}
		return mediaType;
	}
}
