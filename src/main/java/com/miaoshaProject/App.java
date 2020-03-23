package com.miaoshaProject;

import com.miaoshaProject.bean.UserInfo;
import com.miaoshaProject.mapper.UserInfoMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */

@SpringBootApplication(scanBasePackages = {"com.miaoshaProject"})
@MapperScan("com.miaoshaProject.mapper")
@RestController
public class App 
{

    public static void main( String[] args )
    {
        SpringApplication.run(App.class,args);
    }
}
