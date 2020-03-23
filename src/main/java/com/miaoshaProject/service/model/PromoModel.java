package com.miaoshaProject.service.model;

import lombok.Data;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: XiaoLei
 * @Date Created in 18:02 2020/3/23
 */
@Data
public class PromoModel {
    private Integer id;

    private Integer status;//秒杀活动状态：1 未开始，2 表示进行中，3表示已结束

    private String promoName;//秒杀活动名称

    private DateTime startTime;//秒杀开始时间

    private DateTime endTime;//秒杀开始时间

    private Integer itemId;//秒杀活动的适用商品

    private BigDecimal promoItemPrice;//秒杀活动的商品价格

}
