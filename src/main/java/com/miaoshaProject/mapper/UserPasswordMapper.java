package com.miaoshaProject.mapper;


import com.miaoshaProject.bean.UserPassword;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: XiaoLei
 * @Date Created in 10:37 2020/3/22
 */
@Mapper
public interface UserPasswordMapper {
    int insert(UserPassword record);

    int insertSelective(UserPassword record);

    UserPassword selectByPrimaryKey(Integer id);

    UserPassword selectByUserId(Integer userId);

    int updateByPrimaryKeySelective(UserPassword record);


    int updateByPrimaryKey(UserPassword record);
}
