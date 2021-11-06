package com.imooc.user.controller;

import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.IMOOCJSONResult;
import com.imooc.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloControllerApi {

    static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private RedisOperator redisOperator;

    public Object redis() {
        redisOperator.set("age", "23");
        String age = redisOperator.get("age");

        return GraceJSONResult.ok(age);
    }


    public Object hello() {
        return IMOOCJSONResult.ok();
    }


}
