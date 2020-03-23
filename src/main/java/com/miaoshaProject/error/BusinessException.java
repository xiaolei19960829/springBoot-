package com.miaoshaProject.error;

/**
 * @Author: XiaoLei
 * @Date Created in 16:38 2020/3/19
 */
//设计模式：包装器业务异常实现
public class BusinessException extends Exception implements CommonError {

    private CommonError commonError;

    /**
     * 构造方法的意义：直接接收EmBusinessError的传参用于构造业务异常
     * @param commonError
     */
    public BusinessException(CommonError commonError){
        super();
        this.commonError=commonError;
    }

    /**
     * 接收自定义errmsg的方式构造业务异常
     * @return
     */
    public BusinessException(CommonError commonError,String errMsg){
        super();
        this.commonError=commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrorCode() {
        return this.commonError.getErrorCode();
    }

    @Override
    public String getErrorMsg() {
        return this.commonError.getErrorMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
         this.commonError.setErrMsg(errMsg);
        return this;
    }
}
