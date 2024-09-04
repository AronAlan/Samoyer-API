package com.samoyer.backend.service.impl;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.samoyer.backend.common.ErrorCode;
import com.samoyer.backend.exception.BusinessException;
import com.samoyer.backend.mapper.InterfaceInfoMapper;
import com.samoyer.backend.service.InterfaceInfoService;
import com.samoyer.samoyerapicommon.model.entity.InterfaceInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Samoyer
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2024-08-18 16:39:44
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {
    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {

        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //获取接口信息的名称
        String name = interfaceInfo.getName();

        // 新增接口时，name参数不能为空
        if (add) {
            if (StringUtils.isAnyBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }

        // 校验name,name不为空且长度大于50,则抛异常
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }

}




