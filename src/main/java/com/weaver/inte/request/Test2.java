package com.weaver.inte.request;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weaver.inte.request.enums.RequestMethod;
import com.weaver.inte.request.enums.RequestRawFormat;
import com.weaver.inte.request.pool.IpPoolUtils;
import com.weaver.inte.request.req.RequestBody;
import com.weaver.inte.request.req.RequestClient;
import com.weaver.inte.utils.ReadWriteUtils;
import com.weaver.inte.utils.StringExtUtils;
import com.weaver.inte.utils.json.JsonExtUtils;
import org.apache.commons.io.IOUtils;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test2 {

    public static void main(String[] args) throws Exception {
        String resStr = ReadWriteUtils.read("F:\\d2.txt");
        JSONObject res = JSONObject.parseObject(resStr);
        String seasonId = JsonExtUtils.getStringByJson(res, "seasonId");//赛季 ID
        String matchType = JsonExtUtils.getStringByJson(res, "matchType");//比赛类型  1 常规赛，2 季后赛，3 季前赛，4 全明星、星锐，0 其他
        String operation = JsonExtUtils.getStringByJson(res, "operation");//1.发布2.删除
        //足球数据处理
        JSONArray footStagesArray = JsonExtUtils.getArrayByKeys(res, "data", "football", "stages");
        if (footStagesArray != null) {
            try {
                for (int i = 0; i < footStagesArray.size(); i++) {
                    JSONObject stages = footStagesArray.getJSONObject(i);
                    if (stages != null) {
                        JSONArray outRankArray = JsonExtUtils.getArrayByKeys(stages, "data", "ranks");
                        if (outRankArray != null) {
                            for (int j = 0; j < outRankArray.size(); j++) {
                                JSONObject dataJson = outRankArray.getJSONObject(j);
                                String teamId = JsonExtUtils.getStringByJson(dataJson, "teamId");
                                JSONObject rowData = new JSONObject();
                                //数据
                                rowData.put("teamId", teamId);//主键
                                rowData.put("rank", JsonExtUtils.getStringByJson(dataJson, "rank"));
                                rowData.put("matchNum", JsonExtUtils.getStringByJson(dataJson, "matchNum"));
                                rowData.put("winNum", JsonExtUtils.getStringByJson(dataJson, "winNum"));
                                rowData.put("drawNum", JsonExtUtils.getStringByJson(dataJson, "drawNum"));
                                rowData.put("loseNum", JsonExtUtils.getStringByJson(dataJson, "loseNum"));
                                rowData.put("goalsNum", JsonExtUtils.getStringByJson(dataJson, "goalsNum"));
                                rowData.put("loseGoalsNum", JsonExtUtils.getStringByJson(dataJson, "loseGoalsNum"));
                                rowData.put("score", JsonExtUtils.getStringByJson(dataJson, "score"));
                                rowData.put("seasonId", seasonId);
                            }
                        }
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    private static String searchCtripHotelId(String keyword) {
        try {
            RequestClient client = new RequestClient();
            String script = IOUtils.toString(Class.class.getResourceAsStream("/searchRequest.json"));
            script = script.replaceAll("\\$\\{keyword\\}", keyword);

            RequestBody body = new RequestBody();
            body.raw(RequestRawFormat.JSON, script);

//            String ipAddress = IpPoolUtils.getIpByPool();
            String result = client
                    .http()
                    .url("https://m.ctrip.com/restapi/soa2/21881/json/gaHotelSearchEngine")
//                    .proxy("http", ipAddress.split(":")[0], Integer.parseInt(ipAddress.split(":")[1]))
                    .method(RequestMethod.POST)
                    .body(body)
                    .result();
            JSONObject resJson = JSONObject.parseObject(result);
            JSONObject hotelJson = JsonExtUtils.getObjectByKeysInArrayFirst(resJson, "Response", "searchResults");
            String ctripHotelId = JsonExtUtils.getStringByJson(hotelJson, "id");
            if (StringExtUtils.isNull(ctripHotelId) || "0".equalsIgnoreCase(ctripHotelId)) {
                return "";
            }
            String displayName = JsonExtUtils.getStringByJson(hotelJson, "displayName");
            return ctripHotelId + "\t" + displayName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String searchCtripHotelId_setup2(String keyword) {
        try {
            RequestClient client = new RequestClient();
//            String ipAddress = IpPoolUtils.getIpByPool();
            String result = client
                    .http()
                    .url("https://m.ctrip.com/restapi/h5api/globalsearch/search?action=online&source=globalonline&keyword=" + URLEncoder.encode(keyword, "utf-8") + "&t=" + System.currentTimeMillis())
//                    .proxy("http", ipAddress.split(":")[0], Integer.parseInt(ipAddress.split(":")[1]))
                    .method(RequestMethod.GET)
                    .result();
            JSONObject resJson = JSONObject.parseObject(result);
            JSONArray array = JsonExtUtils.getArrayByKeys(resJson, "data");
            for (int i = 0; i < array.size(); i++) {
                JSONObject item = array.getJSONObject(i);
                String code = item.getString("code");
                if (StringExtUtils.isNotNull(code) && code.startsWith("hotel_")) {
                    String ctripHotelId = JsonExtUtils.getStringByJson(item, "id");
                    if (StringExtUtils.isNotNull(ctripHotelId) && !"0".equalsIgnoreCase(ctripHotelId)) {
                        String displayName = JsonExtUtils.getStringByJson(item, "address");
                        return ctripHotelId + "\t" + displayName;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return " \t ";
    }
}
