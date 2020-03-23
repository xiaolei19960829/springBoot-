package com.miaoshaProject.controller;

import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.error.EMBusinessError;
import com.miaoshaProject.response.CommonReturnType;
import com.miaoshaProject.service.OrderService;
import com.miaoshaProject.service.OrderServiceImpl;
import com.miaoshaProject.service.model.OrderModel;
import com.miaoshaProject.service.model.UserInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: XiaoLei
 * @Date Created in 10:10 2020/3/23
 */
@Controller("/order")
@RequestMapping("/order")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class OrderController extends BaseController {
    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    //创建商品的controller
    @RequestMapping(value = "/createorder" ,method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name="itemId")Integer itemId,@RequestParam(name="promoId",required = false)Integer promoId,@RequestParam(name = "amount")Integer amount) throws BusinessException {

        //获取用户端 登录信息
        Boolean is_login = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if(is_login==null||!is_login.booleanValue()){
            throw new BusinessException(EMBusinessError.USER_NOT_LOGIN,"用户未登录");
        }
        //获取用户的登录信息
        UserInfoModel userInfoModel=(UserInfoModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");

        OrderModel order = orderService.createOrder(userInfoModel.getId(), itemId,promoId, amount);

        return CommonReturnType.create(null);
    }

}
