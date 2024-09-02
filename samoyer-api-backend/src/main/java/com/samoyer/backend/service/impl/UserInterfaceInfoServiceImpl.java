package com.samoyer.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.samoyer.backend.common.ErrorCode;
import com.samoyer.backend.exception.BusinessException;
import com.samoyer.backend.service.UserInterfaceInfoService;
import com.samoyer.backend.mapper.UserInterfaceInfoMapper;
import com.samoyer.samoyerapicommon.model.entity.UserInterfaceInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author XZC
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2024-08-26 14:15:42
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

//    @Resource
//    private

    /**
     * 校验接口信息
     * @param userInterfaceInfo
     * @param add
     */
    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 新增接口时，name参数不能为空
        if (add) {
            if (userInterfaceInfo.getInterfaceInfoId()<=0||userInterfaceInfo.getUserId()<=0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口或用户不存在");
            }
        }

        // 校验name,name不为空且长度大于50,则抛异常
        if (userInterfaceInfo.getLeftNum()<0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数不能小于 0");
        }
    }

    /**
     * 调用接口次数统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if (interfaceInfoId<=0||userId<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId",interfaceInfoId);
        updateWrapper.eq("userId",userId);
        //更新字段
        updateWrapper.setSql("leftNum=leftNum-1,totalNum=totalNum+1");

        return this.update(updateWrapper);
    }
}




