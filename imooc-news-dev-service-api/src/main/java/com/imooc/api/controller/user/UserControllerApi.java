package com.imooc.api.controller.user;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.RegisterLoginBO;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "User信息controller", tags = {"User信息相关的功能"})
@RequestMapping("user")
public interface UserControllerApi {


    @ApiOperation(value = "获取用户基本信息", notes = "获取用户基本信息", httpMethod = "POST")
    @PostMapping("/getUserInfo")
    public GraceJSONResult getUserInfo(@RequestParam String userId);

    @ApiOperation(value = "获取用户账户信息", notes = "获取用户账户信息", httpMethod = "POST")
    @PostMapping("/getAccountInfo")
    public GraceJSONResult getAccountInfo(@RequestParam String userId);

    @ApiOperation(value = "完善用户信息", notes = "完善用户信息", httpMethod = "POST")
    @PostMapping("/updateUserInfo")
    public GraceJSONResult updateUserInfo(@RequestBody @Valid UpdateUserInfoBO updateUserInfoBO,
                                          BindingResult result);

//    @ApiOperation(value = "redis方法的接口", notes = "redis方法的借口", httpMethod = "GET")
//    @GetMapping("/redis")
//    public Object redis();


}
