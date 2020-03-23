package com.miaoshaProject.mapper;

import com.miaoshaProject.bean.SequenceDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SequenceDOMapper {
    int deleteByPrimaryKey(String name);

    int insert(SequenceDO record);

    int insertSelective(SequenceDO record);

    SequenceDO selectByPrimaryKey(String name);

    int updateByPrimaryKeySelective(SequenceDO record);

    SequenceDO getSequenceByName(String name);

    int updateByPrimaryKey(SequenceDO record);
}