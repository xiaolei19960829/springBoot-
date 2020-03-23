package com.miaoshaProject.service;

import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.service.model.ItemModel;

import java.util.List;

/**
 * @Author: XiaoLei
 * @Date Created in 22:25 2020/3/21
 */
public interface ItemService {
    //创建商品  :为什么要返回一个这个呢
    ItemModel createItem(ItemModel itemModel) throws BusinessException;
    //商品列表浏览
    List<ItemModel> listItem();
    //商品详情浏览
    ItemModel getItemById(Integer id);

    //库存扣减
    boolean decreaseStock(Integer itemId,Integer amount);
    //商品销量增加
    void increaseSales(Integer itemId,Integer amount) throws BusinessException;
}
