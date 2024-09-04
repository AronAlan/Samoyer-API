package com.samoyer.backend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.samoyer.backend.common.ErrorCode;
import com.samoyer.backend.exception.BusinessException;
import com.samoyer.backend.mapper.UserMapper;
import com.samoyer.samoyerapicommon.model.entity.User;
import com.samoyer.samoyerapicommon.service.InnerUserService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 获取调用接口的用户信息
 * @author: Samoyer
 * @date: 2024-08-29
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {
    @Resource
    private UserMapper userMapper;

    /**
     * 根据密钥accessKey获取内部用户信息
     * @param accessKey
     * @return
     */
    @Override
    public User getInvokeUser(String accessKey) {
        //参数校验
        if (StrUtil.isBlank(accessKey)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"accessKey为空");
        }

        //创建查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey",accessKey);

        //返回根据accessKey查到的用户
        return userMapper.selectOne(queryWrapper);
    }
}
