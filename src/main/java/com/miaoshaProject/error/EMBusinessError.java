package com.miaoshaProject.error;

import com.miaoshaProject.response.CommonReturnType;

/**
 * @Author: XiaoLei
 * @Date Created in 16:29 2020/3/19
 */
public enum  EMBusinessError implements CommonError{
    //通用错误信息 10001开头
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKNOW_ERROR(10002,"未知错误"),


    //20000开头为用户信息相关错误定义
    USER_NOT_EXIST(20001,"用户不存在"),
    USER_LOGIN__FAIL(20002,"用户手机号或密码不正确"),
    USER_NOT_LOGIN(20003,"用户还未登录"),

    //30000开头为交易型错误定义
    STOCK_NOT_ENOUGH(30001,"库存不足")
    ;




    private int errCode;
    private String errMsg;

    private EMBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrorCode() {
        return this.errCode;
    }

    @Override
    public String getErrorMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
         this.errMsg=errMsg;
         return this;
    }
}
