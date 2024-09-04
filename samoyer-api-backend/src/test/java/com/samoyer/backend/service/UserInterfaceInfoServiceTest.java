package com.samoyer.backend.service;

import cn.hutool.http.HttpUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserInterfaceInfoServiceTest {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Test
    void invokeCount() {
        boolean b = userInterfaceInfoService.invokeCount(1L, 1L);
        Assertions.assertTrue(b);
    }

    @Test
    void invokeRemote(){
        //向https://api.btstu.cn/yan/api.php发送get请求
        String url = "https://api.btstu.cn/yan/api.php";
        String result = HttpUtil.get(url);
        System.out.println(result);
    }
}