package com.samoyer.backend.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.samoyer.backend.annotation.AuthCheck;
import com.samoyer.backend.common.*;
import com.samoyer.backend.constant.CommonConstant;
import com.samoyer.backend.exception.BusinessException;
import com.samoyer.backend.exception.ThrowUtils;
import com.samoyer.backend.model.dto.interfaceinfo.*;
import com.samoyer.backend.model.enums.InterfaceInfoStatusEnum;
import com.samoyer.backend.model.vo.MyInterfaceInfoVO;
import com.samoyer.backend.service.InterfaceInfoService;
import com.samoyer.backend.service.UserInterfaceInfoService;
import com.samoyer.backend.service.UserService;
import com.samoyer.samoyerapiclientsdk.client.SamoyerApiClient;
import com.samoyer.samoyerapicommon.model.entity.InterfaceInfo;
import com.samoyer.samoyerapicommon.model.entity.User;
import com.samoyer.samoyerapicommon.model.entity.UserInterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.net.URI;

/**
 * 接口管理
 *
 * @author Samoyer
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

//    @Resource
//    private SamoyerApiClient samoyerApiClient;

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        //校验接口信息
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);

        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param interfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(@PathVariable long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String description = interfaceInfoQuery.getDescription();
        // content 需支持模糊搜索
        interfaceInfoQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceInfoPage);
    }

    @GetMapping("/list/my/page")
    public BaseResponse<Page<MyInterfaceInfoVO>> listMyInterfaceInfoByPage(MyInterfaceInfoQueryRequest myInterfaceInfoQueryRequest,
                                                                           HttpServletRequest request) {
        //获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        myInterfaceInfoQueryRequest.setUserId(loginUser.getId());

        UserInterfaceInfo userInterfaceInfoQuery = new UserInterfaceInfo();
        BeanUtils.copyProperties(myInterfaceInfoQueryRequest, userInterfaceInfoQuery);
        userInterfaceInfoQuery.setUserId(myInterfaceInfoQueryRequest.getUserId());

        long current = myInterfaceInfoQueryRequest.getCurrent();
        long size = myInterfaceInfoQueryRequest.getPageSize();

        //查询user_interface_info表，查出所有userId为id的数据
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfoQuery);
        queryWrapper.eq("userId", userInterfaceInfoQuery.getUserId());
        queryWrapper.orderByDesc("updateTime");

        Page<UserInterfaceInfo> userInterfaceInfoPage = userInterfaceInfoService.page(new Page<>(current, size), queryWrapper);

        //再根据UserInterfaceInfo中的interfaceInfoId，去InterfaceInfo中查询name
        List<Long> interfaceInfoIdList = userInterfaceInfoPage
                .getRecords()
                .stream()
                .map(UserInterfaceInfo::getInterfaceInfoId).collect(Collectors.toList());
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService
                .list(new QueryWrapper<InterfaceInfo>()
                        .in("id", interfaceInfoIdList));

        // 创建Page<MyInterfaceInfoVO>对象
        Page<MyInterfaceInfoVO> myInterfaceInfoVOPage = new Page<>();
        myInterfaceInfoVOPage.setRecords(new ArrayList<>());

        // 遍历 UserInterfaceInfoPage 中的记录
        for (UserInterfaceInfo userInterfaceInfo : userInterfaceInfoPage.getRecords()) {
            // 根据 interfaceInfoId 找到对应的 InterfaceInfo
            Optional<InterfaceInfo> optionalInterfaceInfo = interfaceInfoList.stream()
                    .filter(interfaceInfo -> interfaceInfo.getId().equals(userInterfaceInfo.getInterfaceInfoId()))
                    .findFirst();

            if (optionalInterfaceInfo.isPresent()) {
                InterfaceInfo interfaceInfo = optionalInterfaceInfo.get();
                // 创建 MyInterfaceInfoVO 实例并添加到结果列表
                MyInterfaceInfoVO vo = new MyInterfaceInfoVO(interfaceInfo.getName(), userInterfaceInfo.getLeftNum());
                myInterfaceInfoVOPage.getRecords().add(vo);
            }
        }

        return ResultUtils.success(myInterfaceInfoVOPage);
    }

    /**
     * 发布接口
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        //为null或者id<=0
        if (idRequest == null || idRequest.getId() <= 0) {
            //请求参数错误
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //1.校验接口是否存在
        long id = idRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            //请求数据不存在
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        //2. 判断接口是否可以调用
        User loginUser = userService.getLoginUser(request);
        //创建一个临时的SamoyerApiClient对象，并传入ak和sk
        SamoyerApiClient tempClient = new SamoyerApiClient(loginUser.getAccessKey(), loginUser.getSecretKey());
        //使用字段testSample里的例子进行连通性测试
        String testSample = oldInterfaceInfo.getTestSample();
        if (testSample == null) {
            testSample = "";
        }
        String method = oldInterfaceInfo.getMethod();
        String path = "/general/api";
        String res = "";
        try {
            if ("GET".equals(method)) {
                //发送到网关：http://localhost:8080/api/general/api/get
                res = tempClient.invokeInterfaceByGet(id, testSample, path + "/get");
            } else if ("POST".equals(method)) {
                //http://localhost:8080/api/general/api/post
                res = tempClient.invokeInterfaceByPost(id, testSample, path + "/post");
            }
        } catch (Exception e) {
            log.error("接口连通性验证失败");
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口连通性验证失败，调用接口失败");
        }


        if (StrUtil.isBlank(res)) {
            //系统内部异常
            log.error("接口连通性验证结果为空");
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口连通性验证失败，接口返回结果为空");
        }
        log.info("接口连通性验证成功，可以上线");

        //更新接口状态
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 下线接口
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/offline")
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        //为null或者id<=0
        if (idRequest == null || idRequest.getId() <= 0) {
            //请求参数错误
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //1.校验接口是否存在
        long id = idRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            //请求数据不存在
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 调用真实接口
     *
     * @param interfaceInfoInvokeRequest
     * @return
     */
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest,
                                                    HttpServletRequest request) {
        System.out.println("-----" + interfaceInfoInvokeRequest);
        //为null或者id<=0
        if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0) {
            //请求参数错误
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //1.校验接口是否存在
        long id = interfaceInfoInvokeRequest.getId();
        //提取请求参数
        String userRequestParams = interfaceInfoInvokeRequest.getUserRequestParams();
        if (userRequestParams == null) {
            userRequestParams = "";
        }

        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        //判断是否存在这个接口
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //判断接口是否处于下线状态
        if (interfaceInfo.getStatus() == InterfaceInfoStatusEnum.OFFLINE.getValue()) {
            throw new BusinessException(ErrorCode.OFFLINED_ERROR, "接口已下线");
        }

        //当前用户想要发送请求到接口，进行调用
        //先获取当前登录的用户，并获取其ak和sk
        //获取用户的ak和sk
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();

        //创建一个临时的SamoyerApiClient对象，并传入ak和sk
        SamoyerApiClient tempClient = new SamoyerApiClient(accessKey, secretKey);

        //调用方法，传入对象，获取用户名
        //getUserNameByPost中会构建请求信息，比如构建请求头，比如对请求体加密为sign
        //构建完后使用HttpRequest发送请求到网关
        //先被网关拦截到，再由网关进行转发，其中网关会对请求体中的sign进行校验
        String method = interfaceInfo.getMethod();
        String path = "/general/api";

        String result = null;
        try {
            if ("GET".equals(method)) {
                //发送到网关：http://localhost:8080/api/general/api/get
                result = tempClient.invokeInterfaceByGet(id, userRequestParams, path + "/get");
            } else if ("POST".equals(method)) {
                //http://localhost:8080/api/general/api/post
                result = tempClient.invokeInterfaceByPost(id, userRequestParams, path + "/post");
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return ResultUtils.success(result);
    }
}
