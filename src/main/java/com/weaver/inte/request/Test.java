package com.weaver.inte.request;

import java.util.Map;

import com.weaver.inte.request.enums.RequestContentType;
import com.weaver.inte.request.enums.RequestMethod;
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
			RequestBody body = new RequestBody();
			body.add("name", "lzy");
//			result = client.method(RequestMethod.POST).url("http://localhost/test2/get").header(header).contentType(RequestContentType.FORM).body(body).send();
//			System.out.println(result.get("header").toString());
//			System.out.println(result.get("body").toString().replaceAll("<br/>", "\n"));
//			System.out.println("---------------------------------------------------------");
			result = client
					.method(RequestMethod.POST)
					.url("http://192.168.1.3:8286/xxx/test/get")
					.header(header)
					.contentType(RequestContentType.FORM)
					.body(body)
					.proxy("http", "192.168.1.3", 80)
					.send();
			System.out.println(result.get("header").toString());
			System.out.println(result.get("body").toString().replaceAll("<br/>", "\n"));
			System.out.println("---------------------------------------------------------");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
