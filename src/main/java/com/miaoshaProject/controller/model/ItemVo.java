package com.miaoshaProject.controller.model;

import lombok.Data;
import org.joda.time.DateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author: XiaoLei
 * @Date Created in 9:53 2020/3/22
 */
@Data
public class ItemVo {
    private Integer id;
    private String title;//商品名
    private BigDecimal price;//商品价格
    private Integer stock;//商品库存
    private String description;//商品描述
    private Integer sales;//商品销量
    private String imgUrl;//商品描述图片
    private Integer promoStatus;//记录商品是否在秒杀活动中，0没，1待开始，2秒杀进行，3秒杀结束
    private BigDecimal promoPrice;
    private Integer promoId;
    private String startDate;//秒杀开始
}
