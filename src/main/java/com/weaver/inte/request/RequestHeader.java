package com.weaver.inte.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.weaver.inte.request.util.StringUtil;

import okhttp3.Request.Builder;

public class RequestHeader {
	private Logger logger = LoggerFactory.getLogger(RequestClient.class);
	List<Map> params = new ArrayList();

	public void add(String $key, Object $value) {
		Map map = new HashMap();
		map.put("key", $key);
		map.put("value", $value);
		params.add(map);
	}

	void build(Builder build) {
		if (params != null && params.size() > 0) {
			params.forEach(map -> {
				String key = StringUtil.ifNull(map.get("key"));
				String value = StringUtil.ifNull(map.get("value"));
				build.addHeader(key, value);
				logger.debug(key + ":" + value);
			});
		}
	}
}