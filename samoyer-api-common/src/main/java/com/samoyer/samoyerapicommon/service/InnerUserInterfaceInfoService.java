package com.samoyer.samoyerapicommon.service;

/**
* @author XZC
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2024-08-26 14:15:42
*/
public interface InnerUserInterfaceInfoService{
    /**
     * 调用接口次数统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId,long userId);

    /**
     * 获取用户对于此接口的调用次数
     * 用于判断用户是否还有次数能够调用
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    Integer getLeftNum(long interfaceInfoId,long userId);
}
