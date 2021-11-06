package com.imooc.api.controller.user;

import com.imooc.pojo.bo.RegisterLoginBO;
import com.imooc.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "用户注册登录Controller", tags = {"用户注册登录"})
@RequestMapping("passport")
public interface PassportControllerApi {

    /**
     * api的作用：其他的服务层都是实现
     * 所有的api接口同一在这里进行管理
     *
     * 所有的接口在这里暴露，实现在各自的微服务中
     * 微服务之间的调用都是基于接口
     * @return
     */
    @ApiOperation(value = "获得短信验证码", notes = "获得短信验证码", httpMethod = "GET")
    @GetMapping("/getSMSCode")
    public GraceJSONResult getSMSCode(@RequestParam String mobile, HttpServletRequest request);


    @ApiOperation(value = "一键注册登录接口", notes = "一键注册登录接口", httpMethod = "POST")
    @PostMapping("/doLogin")
    public GraceJSONResult doLogin(@RequestBody RegisterLoginBO registerLoginBO,
                                   BindingResult result,
                                   HttpServletRequest request,
                                   HttpServletResponse response);


    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public GraceJSONResult logout(@RequestParam String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response);




}
