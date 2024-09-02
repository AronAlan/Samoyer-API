package com.samoyer.samoyerapicommon.service;

import com.samoyer.samoyerapicommon.model.entity.InterfaceInfo;

/**
* @author Samoyer
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-08-18 16:39:44
*/
public interface InnerInterfaceInfoService{
    /**
     * 从数据库中查模拟接口是否存在（请求路径，请求方法，请求参数）
     * @param path
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String path,String method);
}
