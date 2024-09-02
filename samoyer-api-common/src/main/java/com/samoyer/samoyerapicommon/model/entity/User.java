package com.samoyer.samoyerapicommon.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 *
 * @author Samoyer
 *  
 */
@TableName(value = "user")
@Data
public class User implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户名
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 签名 accessKey
     */
    private String accessKey;

    /**
     * 签名 secretKey
     */
    private String secretKey;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}