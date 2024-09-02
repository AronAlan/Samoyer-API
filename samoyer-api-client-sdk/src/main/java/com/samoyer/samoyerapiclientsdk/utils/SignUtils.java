package com.samoyer.samoyerapiclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * 生成签名 工具
 * @author: Samoyer
 * @date: 2024-08-21
 */
public class SignUtils {
    /**
     * 生成签名
     * @param body
     * @param secretKey
     * @return
     */
    public static String generateSign(String body,String secretKey){
        //使用SHA256算法的Digester
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        //使用body内容加上.secretKey密钥 来加密
        String content=body+"."+secretKey;
        return md5.digestHex(content);
    }
}
