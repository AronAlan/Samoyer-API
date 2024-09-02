package com.samoyer.samoyerapigateway.enums;

/**
 * 自定义响应状态枚举
 *
 * @author Samoyer
 *  
 */
public enum ResponseStatusEnum {

    //接口调用次数不足 剩余次数不足
    LEFT_NUM_INSUFFICIENT("The left num is insufficient", 403);

    private final String message;
    private final int code;

    ResponseStatusEnum(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
