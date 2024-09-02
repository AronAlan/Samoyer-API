package com.samoyer.samoyerinterface.controller;

import com.samoyer.samoyerapiclientsdk.model.User;
import com.samoyer.samoyerapiclientsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 名称 接口
 *
 * @author: Samoyer
 * @date: 2024-08-20
 */
@Slf4j
@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping
    public String getNameByGet() {
        log.info("接收到Get调用");
        return "GET 你的名字是: samoyer";
    }

    @PostMapping
    public String getNameByPost(@RequestParam String name) {
        log.info("接收到Post调用，name:{}",name);
        return "POST 你的名字是:" + name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) {
        log.info("接收到Post调用，user:{}",user);
        return "POST 你的名字是:" + user.getUsername();
    }
}
