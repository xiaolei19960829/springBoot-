package com.miaoshaProject.service;

import com.miaoshaProject.service.model.PromoModel;

/**
 * @Author: XiaoLei
 * @Date Created in 18:25 2020/3/23
 */
public interface PromoService {
    PromoModel getPromoByItemId(Integer itemId);
}
