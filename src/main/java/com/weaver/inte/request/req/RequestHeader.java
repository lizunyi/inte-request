package com.weaver.inte.request.req;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.weaver.inte.utils.StringExtUtils;

import okhttp3.Request.Builder;

public class RequestHeader {
	List<Map<String,Object>> params = new ArrayList<>();

	public RequestHeader add(String key, Object value) {
		Map<String,Object> map = new HashMap<>();
		map.put("key", key);
		map.put("value", value);
		params.add(map);
		
		return this;
	}
	
	public Object get(String key) {
		return params.stream().filter(x-> x.get("key").toString().equalsIgnoreCase(key)).findFirst().orElse(new HashMap()).get("value");
	}

	public RequestHeader add(Map<String,Object> values) {
		params.add(values);
		return this;
	}


	void newBuild(Builder build) {
		if (params != null && params.size() > 0) {
			params.forEach(map -> {
				String key = StringExtUtils.ifNull(map.get("key"));
				String value = StringExtUtils.ifNull(map.get("value"));
				build.addHeader(key, value);
			});
		}
	}
}
