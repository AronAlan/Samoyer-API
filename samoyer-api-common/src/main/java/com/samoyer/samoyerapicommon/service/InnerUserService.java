package com.samoyer.samoyerapicommon.service;

import com.samoyer.samoyerapicommon.model.entity.User;

/**
 * 用户服务
 *
 * @author Samoyer
 *  
 */
public interface InnerUserService{
    /**
     * 数据库中查是否已经分配给用户密钥（accessKey，secretKey）
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);

}
