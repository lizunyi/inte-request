package com.weaver.inte.request.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import okhttp3.Headers;

public class RequestUtils {
	public static Map<String,String> getHeaderMap(Headers heads) {
		Map<String,String> result = new HashMap<>();
		if (heads != null && heads.size() > 0) {
			result = heads.names().stream().collect(Collectors.toMap(Function.identity(),  new Function<String, String>() {
				@Override
				public String apply(String t) {
					return String.join(";", heads.values(t));
				}
			}));
		}
		return result;
	}
}
