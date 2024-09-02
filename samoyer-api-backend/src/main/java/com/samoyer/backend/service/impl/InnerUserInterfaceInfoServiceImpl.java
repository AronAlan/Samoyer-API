package com.samoyer.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.samoyer.backend.common.ErrorCode;
import com.samoyer.backend.exception.BusinessException;
import com.samoyer.backend.mapper.UserInterfaceInfoMapper;
import com.samoyer.backend.service.UserInterfaceInfoService;
import com.samoyer.samoyerapicommon.model.entity.UserInterfaceInfo;
import com.samoyer.samoyerapicommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 统计调用次数
 * @author: Samoyer
 * @date: 2024-08-29
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId,userId);
    }

    /**
     * 获取用户对于此接口的调用次数
     * 用于判断用户是否还有次数能够调用
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    @Override
    public Integer getLeftNum(long interfaceInfoId, long userId){
        if (interfaceInfoId<=0||userId<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //根据interfaceInfoId和userId查询
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interfaceInfoId",interfaceInfoId);
        queryWrapper.eq("userId",userId);
        return userInterfaceInfoMapper.selectOne(queryWrapper).getLeftNum();
    }
}
