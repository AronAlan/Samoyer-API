package com.samoyer.samoyerinterface.controller;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.samoyer.samoyerapiclientsdk.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一接口
 *
 * @author: Samoyer
 * @date: 2024-08-20
 */
@Slf4j
@RestController
@RequestMapping("/general/api")
public class GeneralController {
    @GetMapping("/get")
    public String generalGet(HttpServletRequest request) {
        log.info("接收到generalGet请求");
        String url = request.getHeader("url");
        String result = null;
        try (HttpResponse httpResponse = HttpRequest.get(url).execute()) {
            result = httpResponse.body();
        }
        return result;
    }

    @PostMapping("/post")
    public String generalPost(HttpServletRequest request) {
        log.info("接收到generalPost请求");
        String result = null;
        String url = request.getHeader("url");
        String body = URLUtil.decode(request.getHeader("body"), CharsetUtil.CHARSET_UTF_8);
        try (HttpResponse httpResponse = HttpRequest.post(url)
                // 处理中文编码
                .header("Accept-Charset", CharsetUtil.UTF_8)
                // 传递参数
                .body(body)
                .execute()) {
            result = httpResponse.body();
        }
        return result;
    }


}
