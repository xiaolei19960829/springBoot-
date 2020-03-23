package com.miaoshaProject.mapper;

import com.miaoshaProject.bean.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: XiaoLei
 * @Date Created in 10:36 2020/3/22
 */
@Mapper
public interface UserInfoMapper {
    int insert(UserInfo userInfo);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer id);

    UserInfo selectByTelphone(String telphone);

    int updateByPrimaryKeySelective(UserInfo userInfo);

    int updateByPrimaryKey(UserInfo userInfo);
}
