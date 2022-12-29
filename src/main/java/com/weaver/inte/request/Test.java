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
        try {
            RequestClient client = new RequestClient();
            client
                    .http()
                    .url("http://xxx")
                    .method(RequestMethod.POST)
                    .header(new RequestHeader())
                    .body(new RequestBody().addFile("$fileKey", "$fileName", null).add("$key", "$val"))
                    .send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
