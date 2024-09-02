package com.samoyer.backend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 发布或下线接口，接收id请求
 *
 * @author Samoyer
 *  
 */
@Data
public class IdRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}