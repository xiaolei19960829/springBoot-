package com.miaoshaProject.controller;

import com.alibaba.druid.util.StringUtils;
import com.miaoshaProject.controller.model.UserVo;
import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.error.EMBusinessError;
import com.miaoshaProject.response.CommonReturnType;
import com.miaoshaProject.service.UserService;
import com.miaoshaProject.service.model.UserInfoModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @Author: XiaoLei
 * @Date Created in 14:27 2020/3/19
 */
@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;//内部有threadlocal的方式来处理并发

    //用户登录接口
    @RequestMapping(value = "/login" ,method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name="telphone")String telphone,
                                  @RequestParam(name="password")String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, BusinessException {
        //入参校验
        if(StringUtils.isEmpty(telphone)||StringUtils.isEmpty(password)){
            throw new BusinessException(EMBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //用户登录服务,用来校验用户登录是否合法
        UserInfoModel userInfoModel=userService.validateLogin(telphone,this.EncodeByMd5(password));

        //加入到用户端登录成功的session
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userInfoModel);
        return CommonReturnType.create(null);
    }

    //用户注册接口
    @RequestMapping(value = "/register" ,method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam("telphone") String telphone,
                                     @RequestParam("otpCode") String otpCode,
                                     @RequestParam("name") String name,
                                     @RequestParam("gender") byte gender,
                                     @RequestParam("age") Integer age,
                                     @RequestParam("password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

        //验证手机号和otpCode姓名是否符合
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);
        System.out.println("aa="+inSessionOtpCode);
        if(!StringUtils.equals(otpCode,inSessionOtpCode)){
            throw  new BusinessException(EMBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
        }
        //用户的注册流程
        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setName(name);
        userInfoModel.setGender(gender);
        userInfoModel.setAge(age);
        userInfoModel.setTelphone(telphone);
        userInfoModel.setRegisterMode("byphone");
        userInfoModel.setEncrptPassword(this.EncodeByMd5(password));

        userService.register(userInfoModel);
        return CommonReturnType.create(null);
    }

    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedOperationException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //加密字符串
        String newstr=base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    /**
     * 用户获取otp短信接口
     * @param telphone
     * @return
     */
    @RequestMapping(value = "/getOtp",method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam("telphone") String telphone){
        //需要按照一定的规则生成otp验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);//此时的验证码为0-99999
        randomInt+=10000;
        String otpCode = String.valueOf(randomInt);
        //将otp验证码同对应用户的手机号关联:一般放在redis里面，天然适合做这种应用。目前使用httpSession的方式绑定验证码和手机号
        httpServletRequest.getSession().setAttribute(telphone,otpCode);
        System.out.println("我這otp能獲取："+httpServletRequest.getSession().getAttribute(telphone));

        //将otp验证码通过短信通道发送给用户，省略
        System.out.println("telphone= "+telphone+"&otpCode= "+otpCode);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam("id") Integer id) throws BusinessException {
        //调用service层获取对应的id的用户对象并返回给前端
        UserInfoModel userInfoModel = userService.getUserInfoById(id);

        //若获取的对应用户信息不存在，
        if(userInfoModel==null){
            userInfoModel.setEncrptPassword("123");
        }


        //将核心领域模型用户对象转化为可供UI使用的viewObject
        UserVo userVo = convertFromModel(userInfoModel);

        return CommonReturnType.create(userVo);
    }

    private UserVo convertFromModel(UserInfoModel userInfoModel){
        if(userInfoModel==null){
            return null;
        }
        UserVo userVo1 = new UserVo();
        BeanUtils.copyProperties(userInfoModel,userVo1);
        return userVo1;
    }

}
