package com.miaoshaProject.service;

import com.miaoshaProject.bean.OrderDo;
import com.miaoshaProject.bean.PromoDO;
import com.miaoshaProject.mapper.PromoDOMapper;
import com.miaoshaProject.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Author: XiaoLei
 * @Date Created in 18:27 2020/3/23
 */
@Service
public class PromoServiceImpl implements PromoService {
    @Autowired
    private PromoDOMapper promoDOMapper;
    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        //获取对应商品的秒杀活动信息
        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);
        PromoModel promoModel=convertFromDataObject(promoDO);
        if(promoModel==null){
            return null;
        }

        //判断当前时间是否秒杀活动即将开始或正在进行
        DateTime dateTime = new DateTime();
        System.out.println("datetime="+dateTime);
        if(promoModel.getStartTime().isAfterNow()){
            promoModel.setStatus(1);
        }else if(promoModel.getEndTime().isBeforeNow()){
            promoModel.setStatus(3);
        }else{
            promoModel.setStatus(2);
        }
        return promoModel;
    }
    private PromoModel convertFromDataObject(PromoDO promoDO){
        if(promoDO==null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO,promoModel);
        promoModel.setPromoItemPrice(new BigDecimal(promoDO.getPromoItemPrice()));
        promoModel.setStartTime(new DateTime(promoDO.getStartTime()));
        promoModel.setEndTime(new DateTime(promoDO.getEndTime()));
        return promoModel;
    }
}
