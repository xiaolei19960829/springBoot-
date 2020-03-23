package com.miaoshaProject.validator;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: XiaoLei
 * @Date Created in 22:42 2020/3/21
 */

public class ValidationResult {
    //校验结果是否有错
    private boolean hasErrors=false;
    //错误信息
    private Map<String,String> errorMsgMap=new HashMap<>();

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public Map<String, String> getErrorMsgMap() {
        return errorMsgMap;
    }

    public void setErrorMsgMap(Map<String, String> errorMsgMap) {
        this.errorMsgMap = errorMsgMap;
    }

    //实现通用的格式化字符串信息获取错误结果的msg方法
    public String getErrorMsg(){
        return StringUtils.join(errorMsgMap.values().toArray(),",");
    }
}
