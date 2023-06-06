package com.weaver.inte.request;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.weaver.inte.request.enums.RequestContentType;
import com.weaver.inte.request.enums.RequestMethod;
import com.weaver.inte.request.enums.RequestRawFormat;
import com.weaver.inte.request.enums.RequestSchema;
import com.weaver.inte.request.req.RequestBody;
import com.weaver.inte.request.req.RequestClient;
import com.weaver.inte.request.req.RequestHeader;

public class Test {
    public static void main(String[] args) {
        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("F:\\miku\\咪咕\\数据备份\\player_stat.json")))) {
                String jsonText = null;
                while ((jsonText = reader.readLine()) != null) {
                    RequestClient client = new RequestClient();
                    String r = client
                            .http()
                            .url("http://180.184.78.246:18080/miku/api/test/kafka/add")
                            .method(RequestMethod.POST)
                            .body(new RequestBody().raw(RequestRawFormat.JSON, jsonText.substring(20)))
                            .result();
                    System.out.println(r);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
