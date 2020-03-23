package com.miaoshaProject.service;

import com.miaoshaProject.bean.UserInfo;
import com.miaoshaProject.bean.UserPassword;
import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.error.EMBusinessError;
import com.miaoshaProject.mapper.UserInfoMapper;
import com.miaoshaProject.mapper.UserPasswordMapper;
import com.miaoshaProject.service.model.UserInfoModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: XiaoLei
 * @Date Created in 14:29 2020/3/19
 */
@Service
public class UserSerciceImpl implements UserService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserPasswordMapper userPasswordMapper;



    @Override
    public UserInfoModel getUserInfoById(Integer id) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
        if(userInfo==null){
            return null;
        }
        //通过用户id，获取对应的用户加密密码信息
        UserPassword userPassword = userPasswordMapper.selectByUserId(id);
        return convertFromDataObject(userInfo,userPassword);

    }

    @Override
    public void register(UserInfoModel userInfoModel) throws BusinessException {
        if(userInfoModel==null){
            throw new BusinessException(EMBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        if(StringUtils.isEmpty(userInfoModel.getName())
                        ||userInfoModel.getGender()==null
                        ||userInfoModel.getAge()==null
                        ||StringUtils.isEmpty(userInfoModel.getTelphone())
                ){
            throw new BusinessException(EMBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //把註冊信息分為兩張表填写
        UserInfo userInfo = convertFromModel(userInfoModel);
        //实现model->dataobject的方法
        userInfoMapper.insertSelective(userInfo);
        userInfoModel.setId(userInfo.getId());
        UserPassword userPassword=convertPasswordFromModel(userInfoModel);
        userPasswordMapper.insertSelective(userPassword);

    }

    @Override
    public UserInfoModel validateLogin(String telphone, String password) throws BusinessException {
        //通过用户的手机获取用户信息
        UserInfo userInfo = userInfoMapper.selectByTelphone(telphone);
        if(userInfo==null){
            throw new BusinessException(EMBusinessError.USER_LOGIN__FAIL);
        }
        UserPassword userPassword=userPasswordMapper.selectByUserId(userInfo.getId());
        if(userPassword==null){
            throw new BusinessException(EMBusinessError.USER_LOGIN__FAIL);
        }
        UserInfoModel userInfoModel=convertFromDataObject(userInfo,userPassword);
        //比对用户信息内加密的密码是否和传输进来的密码相匹配
        if(!StringUtils.equals(password,userInfoModel.getEncrptPassword())){
            throw new BusinessException(EMBusinessError.USER_LOGIN__FAIL);
        }
        return userInfoModel;
    }

    private UserPassword convertPasswordFromModel(UserInfoModel userInfoModel){
        if(userInfoModel==null) {
            return null;
        }
            UserPassword userPassword = new UserPassword();
            userPassword.setEncrptPassword(userInfoModel.getEncrptPassword());
            userPassword.setUserId(userInfoModel.getId());
            return userPassword;
    }

    private UserInfo convertFromModel(UserInfoModel userInfoModel){
        if(userInfoModel==null){
            return null;
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoModel,userInfo);
        return userInfo;
    }

    private UserInfoModel convertFromDataObject(UserInfo userInfo, UserPassword userPassword){
        if(userInfo==null){
            return null;
        }
        UserInfoModel userInfoModel = new UserInfoModel();
        BeanUtils.copyProperties(userInfo,userInfoModel);
        if(userPassword!=null){
            userInfoModel.setEncrptPassword(userPassword.getEncrptPassword());
        }
        return userInfoModel;
    }
}
