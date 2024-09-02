package com.samoyer.backend.model.vo;

import com.samoyer.backend.common.PageRequest;
import com.samoyer.samoyerapicommon.model.entity.InterfaceInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 我的 接口信息封装视图
 *
 * @author Samoyer
 */
@Data
@AllArgsConstructor
public class MyInterfaceInfoVO extends PageRequest implements Serializable {

    /**
     * 接口名字
     */
    private String name;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    private static final long serialVersionUID = 1L;
}
