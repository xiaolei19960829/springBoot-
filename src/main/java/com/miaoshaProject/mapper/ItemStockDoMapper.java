package com.miaoshaProject.mapper;

import com.miaoshaProject.bean.ItemStockDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface ItemStockDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemStockDo record);

    int insertSelective(ItemStockDo record);

    ItemStockDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemStockDo record);

    int updateByPrimaryKey(ItemStockDo record);

    ItemStockDo selectByItemId(Integer itemId);

    int decreaseStock(@Param("itemId") Integer itemId,@Param("amount") Integer amount);
}