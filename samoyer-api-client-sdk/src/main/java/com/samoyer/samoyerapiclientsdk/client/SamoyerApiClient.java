package com.samoyer.samoyerapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.samoyer.samoyerapiclientsdk.model.User;
import com.samoyer.samoyerapiclientsdk.utils.SignUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用第三方接口的客户端
 *
 * @author: Samoyer
 * @date: 2024-08-20
 */
public class SamoyerApiClient {
    private static final String GATEWAY_HOST="http://localhost:8080";

    private String accessKey;
    private String secretKey;

    public SamoyerApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * 构造请求头
     *
     * @return
     */
    private Map<String, String> constructHeader(String body) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        //不能直接发送密钥
//        hashMap.put("secretKey", secretKey);
        //生成随机数,4个随机数的字符串
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("body",body);
        hashMap.put("timestamp",String.valueOf(System.currentTimeMillis()/1000));
        hashMap.put("sign", SignUtils.generateSign(body,secretKey));
        return hashMap;
    }

    public String getNameByGet() {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("accessKey", accessKey);
        paramMap.put("nonce", RandomUtil.randomNumbers(4));
        paramMap.put("timestamp",String.valueOf(System.currentTimeMillis()/1000));
        paramMap.put("sign", SignUtils.generateSign("get",secretKey));

        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST+"/api/name")
                //添加自定义构造的请求头
                .addHeaders(paramMap)
                .execute();
        String result=httpResponse.body();
        System.out.println(result);
        return result;
    }

    public String getNameByPost(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        //发送post请求,并获取响应结果
        String result = HttpUtil.post(GATEWAY_HOST+"/api/name", paramMap);
        System.out.println(result);
        return result;
    }

    public String getUserNameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        //使用HttpRequest工具发送post请求，并获取响应
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST+"/api/name/user")
                //添加自定义构造的请求头
                .addHeaders(constructHeader(json))
                .body(json)
                .execute();
        System.out.println(httpResponse.getStatus());
        String result = httpResponse.body();
        System.out.println(result);
        return result;
    }
}
