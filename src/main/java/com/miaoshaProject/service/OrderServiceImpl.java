package com.miaoshaProject.service;

import com.miaoshaProject.bean.OrderDo;
import com.miaoshaProject.bean.SequenceDO;
import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.error.EMBusinessError;
import com.miaoshaProject.mapper.OrderDoMapper;
import com.miaoshaProject.mapper.SequenceDOMapper;
import com.miaoshaProject.service.model.ItemModel;
import com.miaoshaProject.service.model.OrderModel;
import com.miaoshaProject.service.model.UserInfoModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: XiaoLei
 * @Date Created in 22:25 2020/3/22
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDoMapper orderDoMapper;
    @Autowired
    private SequenceDOMapper sequenceDOMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId,Integer promoId, Integer amount) throws BusinessException {
        //1、校验下单状态，下单的商品是否存在，用户是否合法，购买数量是否正确
        ItemModel itemModel = itemService.getItemById(itemId);
        if(itemModel==null){
            throw new BusinessException(EMBusinessError.PARAMETER_VALIDATION_ERROR,"商品信息不存在");
        }
        UserInfoModel userInfoById = userService.getUserInfoById(userId);
        if(userInfoById==null){
            throw new BusinessException(EMBusinessError.PARAMETER_VALIDATION_ERROR,"用户信息不存在");
        }
        if(amount<=0||amount>99){
            throw new BusinessException(EMBusinessError.PARAMETER_VALIDATION_ERROR,"数量信息不正确");
        }

        if(promoId!=null){
            //教研对于活动是否存在这个商品
            if(promoId.intValue()!=itemModel.getPromoModel().getId()){
                throw new BusinessException(EMBusinessError.PARAMETER_VALIDATION_ERROR,"活动信息不正确");

            }else if(itemModel.getPromoModel().getStatus().intValue()!=2){
                throw new BusinessException(EMBusinessError.PARAMETER_VALIDATION_ERROR,"活动为开始");
            }
        }

        //2、落单减库存，支付减库存。采用落单减库存
        boolean result = itemService.decreaseStock(itemId, amount);
        if(!result){
            throw new BusinessException(EMBusinessError.STOCK_NOT_ENOUGH);
        }
        //3、订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setPromoId(promoId);
        orderModel.setAmount(amount);
        if(promoId!=null){
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else{
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setOrderAmount(orderModel.getItemPrice().multiply(new BigDecimal(amount)));//避免自动转型
        orderModel.setId(generateOrderNo());
        //生成交易流水号

        OrderDo orderDo=convertFromOrderModel(orderModel);
        System.out.println(orderDo);
        orderDoMapper.insertSelective(orderDo);
        //加上商品的销量
        itemService.increaseSales(itemId,amount);
        //4、返回前端
        return orderModel;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)//只要执行完这代码，就提交
    private String generateOrderNo(){
        //订单号有16位,前八位为时间信息，年月日 ；
        StringBuilder stringBuilder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        String nowDate=now.format(DateTimeFormatter.ISO_DATE).replace("-","");
        stringBuilder.append(nowDate);
        //中间6为为自增序列，
        int sequence=0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        sequence=sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue()+sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);
        //sequece是惟一的 ，拼接它
        String sequenceStr=String.valueOf(sequence);
        for (int i = 0; i < 6 - sequenceStr.length(); i++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);

        // 最后2位为分库分表
        stringBuilder.append("00");
        return stringBuilder.toString();
    }

    private OrderDo convertFromOrderModel(OrderModel orderModel){
        OrderDo orderDo = new OrderDo();
        if(orderModel==null){
            return null;
        }
        BeanUtils.copyProperties(orderModel,orderDo);
        orderDo.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDo.setOrderPrice(orderModel.getOrderAmount().doubleValue());
        return orderDo;
    }
}
