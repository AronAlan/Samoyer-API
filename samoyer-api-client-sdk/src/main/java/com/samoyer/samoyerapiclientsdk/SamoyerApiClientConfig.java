package com.samoyer.samoyerapiclientsdk;

import com.samoyer.samoyerapiclientsdk.client.SamoyerApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 客户端配置类
 * @author: Samoyer
 * @date: 2024-08-21
 */
@Configuration
@ConfigurationProperties("samoyer.client")
@Data
@ComponentScan
public class SamoyerApiClientConfig {
    private String accessKey;
    private String secretKey;

    /**
     * 从配置中读取到accessKey和secretKey，并创建客户端实例
     * @return
     */
    @Bean
    public SamoyerApiClient samoyerApiClient(){
        //使用ak和sk创建一个SamoyerApiClient实例
        return new SamoyerApiClient(accessKey,secretKey);
    }
}
