package com.miaoshaProject.service;

import com.miaoshaProject.bean.ItemDo;
import com.miaoshaProject.bean.ItemStockDo;
import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.error.EMBusinessError;
import com.miaoshaProject.mapper.ItemDoMapper;
import com.miaoshaProject.mapper.ItemStockDoMapper;
import com.miaoshaProject.service.model.ItemModel;
import com.miaoshaProject.service.model.PromoModel;
import com.miaoshaProject.validator.ValidationResult;
import com.miaoshaProject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: XiaoLei
 * @Date Created in 22:30 2020/3/21
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private ItemDoMapper itemDoMapper;
    @Autowired
    private ItemStockDoMapper itemStockDoMapper;
    @Autowired
    private PromoService promoService;
    
    private ItemDo convertItemDoFromItemModel(ItemModel itemModel){
        if(itemModel==null){
            return null;
        }
        ItemDo itemDo = new ItemDo();
        BeanUtils.copyProperties(itemModel,itemDo);
        itemDo.setPrice(itemModel.getPrice().doubleValue());
        return itemDo;
    }

    private ItemStockDo convertItemStockFromItemModel(ItemModel itemModel){
        if(itemModel==null){
            return null;
        }
        ItemStockDo itemStockDo = new ItemStockDo();
        itemStockDo.setStock(itemModel.getStock());
        itemStockDo.setItemId(itemModel.getId());
        return itemStockDo;
    }


    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        //校验入参
        ValidationResult result = validator.validate(itemModel);
        if(result.isHasErrors()){
            throw new BusinessException(EMBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrorMsg());
        }
        //转化itemmodel->dataobject
        ItemDo itemDo=this.convertItemDoFromItemModel(itemModel);
        //写入数据库
        itemDoMapper.insertSelective(itemDo);
        itemModel.setId(itemDo.getId());
        //返回创建完成的对象
        ItemStockDo itemStockDo=this.convertItemStockFromItemModel(itemModel);
        itemStockDoMapper.insertSelective(itemStockDo);
        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        //从两张表中返回的是模型
        List<ItemDo> itemDos = itemDoMapper.listItem();
        List<ItemModel> collect = itemDos.stream().map(itemDo -> {
            ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDo.getId());
            ItemModel itemModel = this.convertModelFromDataObject(itemDo, itemStockDo);
            return itemModel;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDo itemDo = itemDoMapper.selectByPrimaryKey(id);
        if(itemDo==null) return null;
        //拿到库存
        ItemStockDo itemStockDo = itemStockDoMapper.selectByItemId(itemDo.getId());
        //将dataobject转为model
        ItemModel itemModel=convertModelFromDataObject(itemDo,itemStockDo);

        //判断商品是否在秒杀活动范围内
        PromoModel promoModel = promoService.getPromoByItemId(itemModel.getId());
        if(promoModel!=null&&promoModel.getStatus().intValue()!=3){
            itemModel.setPromoModel(promoModel);
        }
        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) {
        int i = itemStockDoMapper.decreaseStock(itemId, amount);
        if(i==0){
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
        int i = itemDoMapper.increaseSales(itemId, amount);
    }

    private ItemModel convertModelFromDataObject(ItemDo itemDo,ItemStockDo itemStockDo){
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDo,itemModel);
        itemModel.setPrice(new BigDecimal(itemDo.getPrice()));
        itemModel.setStock(itemStockDo.getStock());
        return itemModel;
    }

}
