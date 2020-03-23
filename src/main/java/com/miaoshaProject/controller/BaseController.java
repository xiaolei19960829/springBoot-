package com.miaoshaProject.controller;

import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.error.EMBusinessError;
import com.miaoshaProject.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @Author: XiaoLei
 * @Date Created in 21:51 2020/3/19
 */
public class BaseController {
    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";
    /**
     * 定义exceptionhandler解决未被controller层吸收的exception
     * 当这个异常处理器收到什么类的异常信息时才能进入这个方法，在这里，我们让所有的异常都进入
     * 处理后，返回的状态码设为正常的200
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handleException(HttpServletRequest request, Exception ex){
        HashMap<String, Object> responseData = new HashMap<>();
        if(ex instanceof BusinessException){
            BusinessException businessException= (BusinessException) ex;
            responseData.put("errorCode",businessException.getErrorCode());
            responseData.put("errMsg",businessException.getErrorMsg());
        }else{
            responseData.put("errorCode", EMBusinessError.UNKNOW_ERROR.getErrorCode());
            responseData.put("errMsg",EMBusinessError.UNKNOW_ERROR.getErrorMsg());
        }
        return CommonReturnType.create(responseData,"fail");
    }
}
