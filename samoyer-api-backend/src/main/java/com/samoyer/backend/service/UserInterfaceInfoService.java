package com.samoyer.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.samoyer.samoyerapicommon.model.entity.UserInterfaceInfo;

/**
* @author XZC
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2024-08-26 14:15:42
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    /**
     * 校验接口信息
     * @param userInterfaceInfo
     * @param add
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 调用接口次数统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId,long userId);
}
