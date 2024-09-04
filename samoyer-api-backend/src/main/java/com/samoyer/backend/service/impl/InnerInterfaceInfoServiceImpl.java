package com.samoyer.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.samoyer.backend.common.ErrorCode;
import com.samoyer.backend.exception.BusinessException;
import com.samoyer.backend.mapper.InterfaceInfoMapper;
import com.samoyer.samoyerapicommon.model.entity.InterfaceInfo;
import com.samoyer.samoyerapicommon.service.InnerInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 获取内部接口的信息
 * @author: Samoyer
 * @date: 2024-08-29
 */
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {
    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    /**
     * 根据url和请求方法，获取内部接口的信息
     * @return
     */
    @Override
    public InterfaceInfo getInterfaceInfo(Integer interfaceId) {
        //interfaceId校验
        if (interfaceId == null || interfaceId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"getInterfaceInfo的interfaceId参数错误");
        }
        //创建查询
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",interfaceId);

        //返回根据url和请求方法，查询到的 内部接口的信息
        return interfaceInfoMapper.selectOne(queryWrapper);
    }
}
