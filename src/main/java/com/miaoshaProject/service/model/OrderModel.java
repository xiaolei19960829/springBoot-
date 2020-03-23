package com.miaoshaProject.service.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: XiaoLei
 * @Date Created in 21:58 2020/3/22
 * 交易模型
 */
@Data
public class OrderModel {
    //202001251200
    private String id;//订单号
    private Integer userId;//用户id
    private Integer itemId;//商品id
    private Integer amount;
    private BigDecimal itemPrice;//单价
    private BigDecimal orderAmount;//购买金额
    private Integer promoId;//若非空，则表示以秒杀商品方式下单
}
