package com.samoyer.backend.model.dto.interfaceinfo;

import com.samoyer.backend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author Samoyer
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MyInterfaceInfoQueryRequest extends PageRequest implements Serializable {
    /**
     * 主键
     */
    private Long userId;
}