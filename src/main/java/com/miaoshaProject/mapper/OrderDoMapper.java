package com.miaoshaProject.mapper;

import com.miaoshaProject.bean.OrderDo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDoMapper {
    int deleteByPrimaryKey(String id);

    int insert(OrderDo record);

    int insertSelective(OrderDo record);

    OrderDo selectByPrimaryKey(String id);


    int updateByPrimaryKeySelective(OrderDo record);

    int updateByPrimaryKey(OrderDo record);
}