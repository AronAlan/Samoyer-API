package com.samoyer.samoyerapiclientsdk.client;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.samoyer.samoyerapiclientsdk.model.User;
import com.samoyer.samoyerapiclientsdk.utils.SignUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用第三方接口的客户端
 *
 * @author: Samoyer
 * @date: 2024-08-20
 */
public class SamoyerApiClient {
    private static final String GATEWAY_HOST="http://localhost:8080/api";

    private String accessKey;
    private String secretKey;

    public SamoyerApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * 发送调用第三方API的get请求
     */
    public String invokeInterfaceByGet(long id,String parameters,String path) throws UnsupportedEncodingException {
        String result=null;
        try (
            //使用HttpRequest工具发送post请求，并获取响应
            HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST+path)
                    //添加自定义构造的请求头
                    .header("Accept-Charset", CharsetUtil.UTF_8)
                    .addHeaders(constructHeader(id,parameters))
                    .body(parameters)
                    .execute();
        ) {
            result=JSONUtil.formatJsonStr(httpResponse.body());
        }
        return result;
    }

    /**
     * 发送调用第三方API的post请求
     */
    public String invokeInterfaceByPost(long id,String parameters,String path) throws UnsupportedEncodingException {
        String result=null;
        try (
                //使用HttpRequest工具发送post请求，并获取响应
                HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST+path)
                        //添加自定义构造的请求头
                        .header("Accept-Charset", CharsetUtil.UTF_8)
                        .addHeaders(constructHeader(id,parameters))
                        .body(parameters)
                        .execute();
        ) {
            result=JSONUtil.formatJsonStr(httpResponse.body());
        }
        return result;
    }

    /**
     * 构造请求头
     *
     * @return
     */
    private Map<String, String> constructHeader(long id,String body) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        //不能直接发送密钥
//        hashMap.put("secretKey", secretKey);
        //生成随机数,4个随机数的字符串
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("body",body);
        hashMap.put("timestamp",String.valueOf(System.currentTimeMillis()/1000));
        hashMap.put("sign", SignUtils.generateSign(body,secretKey));
        hashMap.put("interfaceId",String.valueOf(id));
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

    /*public String getUserNameByPost(User user) {
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
    }*/
}
