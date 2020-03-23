package com.miaoshaProject.service;

import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.service.model.OrderModel;

/**
 * @Author: XiaoLei
 * @Date Created in 22:23 2020/3/22
 */
public interface OrderService {
    OrderModel createOrder(Integer userId,Integer itemId,Integer promoId,Integer amount) throws BusinessException;
}
