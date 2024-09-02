package com.samoyer.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.samoyer.samoyerapicommon.model.entity.InterfaceInfo;

/**
* @author Samoyer
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-08-18 16:39:44
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    /**
     * 校验接口信息
     * @param interfaceInfo
     * @param add
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
