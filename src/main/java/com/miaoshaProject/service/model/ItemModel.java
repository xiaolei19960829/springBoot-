package com.miaoshaProject.service.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author: XiaoLei
 * @Date Created in 21:52 2020/3/21
 */
@Data
public class ItemModel {
    private Integer id;
    @NotBlank(message = "商品名称不为空")
    private String title;//商品名
    private BigDecimal price;//商品价格
    @NotNull(message = "库存不能不填")
    private Integer stock;//商品库存
    @NotBlank(message = "商品描述信息不能为空")
    private String description;//商品描述
    private Integer sales;//商品销量
    @NotBlank(message = "图片信息不能为空")
    private String imgUrl;//商品描述图片

    //使用聚合模型,如果promoModel不为空，则表示其拥有还未结束的秒杀活动
    private PromoModel promoModel;

}
