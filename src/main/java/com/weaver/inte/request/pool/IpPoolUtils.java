package com.weaver.inte.request.pool;

import com.alibaba.fastjson.JSONObject;
import com.weaver.inte.request.enums.RequestMethod;
import com.weaver.inte.request.req.RequestClient;
import com.weaver.inte.utils.StringExtUtils;
import com.weaver.inte.utils.json.JsonExtUtils;

/**
 * @author: saps.weaver
 * @create: 2022-12-22 10:31
 **/
public class IpPoolUtils {
    private static final String UUUrl = "http://api2.uuhttp.com:39002/index/api/return_data?mode=http&count=1&b_time=80&return_type=4&line_break=6&balance=0&secert=MTg2MTgzOTE1MzA6MjFlNjlmZDMwMmZiNzJlZTc5OWI1N2RmZDM0OWU3MzY%3D"; //API链接

    /***
     * 从IP池获取ip
     * @return
     * @throws Exception
     */
    public static String getIpByPool() throws Exception {
        int i = 0;
        RequestClient client = new RequestClient();
        String ipAddress = "";
        do {
            String result = client.url(UUUrl).method(RequestMethod.GET).result();
            if (StringExtUtils.isNotNull(result)) {
                JSONObject resJson = JSONObject.parseObject(result);
                JSONObject ipJson = JsonExtUtils.getObjectByKeysInArrayFirst(resJson, "data", "list");
                String ip = JsonExtUtils.getStringByJson(ipJson, "ip");
                String port = JsonExtUtils.getStringByJson(ipJson, "port");
                if (StringExtUtils.isNotNull(ip) && StringExtUtils.isNotNull(port)) {
                    ipAddress = ip + ":" + port;
                    break;
                }
            }
            i++;
            if (i > 3) {
                break;
            }
        } while (true);
        return ipAddress;
    }
}
