package com.samoyer.samoyerinterface;

import com.samoyer.samoyerapiclientsdk.client.SamoyerApiClient;
import com.samoyer.samoyerapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class SamoyerInterfaceApplicationTests {
    @Resource
    private SamoyerApiClient samoyerApiClient;
    @Test
    void contextLoads() {
//        String result = samoyerApiClient.getNameByGet("samoyer");
//        System.out.println(result);

        User user = new User();
        user.setUsername("buyi");
        String userNameByPost = samoyerApiClient.getUserNameByPost(user);
        System.out.println(userNameByPost);

    }

}
