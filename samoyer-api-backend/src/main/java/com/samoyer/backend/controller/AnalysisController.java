package com.samoyer.backend.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.samoyer.backend.annotation.AuthCheck;
import com.samoyer.backend.common.BaseResponse;
import com.samoyer.backend.common.ErrorCode;
import com.samoyer.backend.common.ResultUtils;
import com.samoyer.backend.constant.UserConstant;
import com.samoyer.backend.exception.BusinessException;
import com.samoyer.backend.mapper.UserInterfaceInfoMapper;
import com.samoyer.backend.model.vo.InterfaceInfoVO;
import com.samoyer.backend.service.InterfaceInfoService;
import com.samoyer.samoyerapicommon.model.entity.InterfaceInfo;
import com.samoyer.samoyerapicommon.model.entity.UserInterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分析控制器
 *
 * @author Samoyer
 */
@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {
    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo() {
        //查询调用次数最多的接口列表（为了查调用次数）
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(3);
        //接口按照接口id分组，以便于关联查询。（其实上一步就是查3个数据。已经式group by interfaceInfoId limit 3 了）
        Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjMap = userInterfaceInfoList.stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));

        //根据这些接口的id,查询这些接口信息（VO不只显示调用次数，还要返回给前端interfaceInfo的全部信息，所以要查InterfaceInfo）
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",interfaceInfoIdObjMap.keySet());
        List<InterfaceInfo> list = interfaceInfoService.list(queryWrapper);
        //判空
        if (CollUtil.isEmpty(list)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        //构建接口信息VO列表，使用流式处理将接口信息映射为VO对象，并加入列表中
        List<InterfaceInfoVO> interfaceInfoVOList=list.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo,interfaceInfoVO);
            Integer totalNum = interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoVO.setTotalNum(totalNum);
            return interfaceInfoVO;
        }).collect(Collectors.toList());

        return ResultUtils.success(interfaceInfoVOList);
    }

}
