package com.miaoshaProject.service;

import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.service.model.UserInfoModel;
import org.springframework.stereotype.Service;

/**
 * @Author: XiaoLei
 * @Date Created in 14:29 2020/3/19
 */

public interface UserService {

    //通过用户id 获取用户对象的方法
    UserInfoModel getUserInfoById(Integer id);

    //注册
    void register(UserInfoModel userInfoModel) throws BusinessException;

    //登录 password s加密后密码
    UserInfoModel validateLogin(String telphone,String password) throws BusinessException;
}
