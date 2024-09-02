package com.samoyer.samoyerinterface;

import com.samoyer.samoyerapiclientsdk.SamoyerApiClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SamoyerApiClientConfig.class)
public class SamoyerInterfaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SamoyerInterfaceApplication.class, args);
    }

}
