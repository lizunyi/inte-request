package com.weaver.inte.request;

import java.util.Map;

import com.weaver.inte.request.enums.RequestContentType;
import com.weaver.inte.request.enums.RequestMethod;
import com.weaver.inte.request.enums.RequestSchema;
import com.weaver.inte.request.req.RequestBody;
import com.weaver.inte.request.req.RequestClient;
import com.weaver.inte.request.req.RequestHeader;

public class Test {
	public static void main(String[] args) {
		RequestClient client = new RequestClient();
		try {
			Map<String,Object> result = null;
			RequestHeader header = new RequestHeader();
			header.add("proxy", "http://192.168.1.3:8287");
			header.add("access_token", "himyidea_test_token");
			header.add("sign", "himyidea_test_sign");
			RequestBody body = new RequestBody();
			body.add("name", "lzy");
//			result = client.method(RequestMethod.POST).url("http://localhost/test2/get").header(header).contentType(RequestContentType.FORM).body(body).send();
//			System.out.println(result.get("header").toString());
//			System.out.println(result.get("body").toString().replaceAll("<br/>", "\n"));
//			System.out.println("---------------------------------------------------------");
			result = client
				.http()
				.url("http://pt.test.changfunfly.com/security/v1/hotel/platformCity")
				.method(RequestMethod.POST)
				.header(header)
				.send();
			System.out.println(result);
			System.out.println(result.get("header").toString());
			System.out.println(result.get("body").toString().replaceAll("<br/>", "\n"));
			System.out.println("---------------------------------------------------------");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
