package com.miaoshaProject.error;

/**
 * @Author: XiaoLei
 * @Date Created in 16:27 2020/3/19
 */
public interface CommonError {
    public int getErrorCode();
    public String getErrorMsg();
    public CommonError setErrMsg(String errMsg);


}
