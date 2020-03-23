package com.miaoshaProject.controller;

import com.miaoshaProject.controller.model.ItemVo;
import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.response.CommonReturnType;
import com.miaoshaProject.service.ItemService;
import com.miaoshaProject.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: XiaoLei
 * @Date Created in 9:52 2020/3/22
 */
@Controller("/item")
@RequestMapping("/item")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    //创建商品的controller
    @RequestMapping(value = "/create" ,method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name="title") String title,
                                       @RequestParam(name = "description") String description,
                                       @RequestParam(name="price")BigDecimal price,
                                       @RequestParam(name="stock")Integer stock,
                                       @RequestParam(name="imgUrl")String imgUrl) throws BusinessException {
        //封装service请求用来创建商品
        ItemModel itemModel=new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);

        ItemModel itemModelForReturn = itemService.createItem(itemModel);

        ItemVo itemVo =convertVoFromModel(itemModelForReturn);
        return CommonReturnType.create(itemVo);

    }
    //创建商品的controller
    @RequestMapping(value = "/list" ,method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType listItem(){
        List<ItemModel> itemModelList=itemService.listItem();
        //使用stream api
        List<ItemVo> collect = itemModelList.stream().map(itemModel -> {
            ItemVo itemVo = this.convertVoFromModel(itemModel);
            return itemVo;
        }).collect(Collectors.toList());
        return CommonReturnType.create(collect);
    }
    @RequestMapping(value = "/get" ,method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name = "id")Integer id){
        ItemModel itemModel = itemService.getItemById(id);

        ItemVo itemVo=convertVoFromModel(itemModel);
        return CommonReturnType.create(itemVo);
    }


    private ItemVo convertVoFromModel(ItemModel itemModel){
        if(itemModel==null){
            return null;
        }
        ItemVo itemVo = new ItemVo();
        BeanUtils.copyProperties(itemModel,itemVo);
        if(itemModel.getPromoModel()!=null){
            itemVo.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVo.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVo.setPromoId(itemModel.getPromoModel().getId());
            itemVo.setStartDate(itemModel.getPromoModel().getStartTime().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            System.out.println(itemModel.getPromoModel().getStartTime().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVo.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else{
            itemVo.setPromoStatus(0);
        }
        return itemVo;
    }
}
