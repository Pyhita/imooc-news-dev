package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.UserControllerApi;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.pojo.vo.AppUserVo;
import com.imooc.user.service.UserService;
import com.imooc.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.vo.UserAccountInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class UserController extends BaseController implements UserControllerApi {

    @Autowired
    private UserService userService;

    @Override
    public GraceJSONResult getUserInfo(String userId) {
        //判断参数不能为空
        if (StringUtils.isEmpty(userId)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_STATUS_ERROR);
        }
        //根据userId查询用户信息
        AppUser user = getUser(userId);
        //返回用户信息
        AppUserVo appUserVo = new AppUserVo();
        BeanUtils.copyProperties(user, appUserVo);

        return GraceJSONResult.ok(appUserVo);
    }

    @Override
    public GraceJSONResult getAccountInfo(String userId) {
        // 0.判断参数不能为空
        if (StringUtils.isBlank(userId)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }
        // 1. 根据userid查询用户信息
        AppUser user = getUser(userId);

        // 2.返回用户信息
        UserAccountInfoVo infoVo = new UserAccountInfoVo();
        BeanUtils.copyProperties(user, infoVo);

        return GraceJSONResult.ok(infoVo);
    }

    @Override
    public GraceJSONResult updateUserInfo(@Valid UpdateUserInfoBO updateUserInfoBO, BindingResult result) {
        // 0 校验BO
        if (result.hasErrors()) {
            Map<String, String> errorMap = getError(result);
            return GraceJSONResult.errorMap(errorMap);
        }

        // 1 执行更新操作
        userService.updateUserInfo(updateUserInfoBO);
        return GraceJSONResult.ok();
    }

    private AppUser getUser(String userId) {
        // 查询时，先判断redis中有没有
        String jsonstring = redisOperator.get(REDIS_USER_INFO + ":" + userId);
        AppUser user = null;
        if (StringUtils.isNotBlank(jsonstring)) {
            user = JsonUtils.jsonToPojo(jsonstring, AppUser.class);
        } else {
            // 对于用户信息这种不经常改变的数据，完全可以在第一次
            // 查询之后，存储在redis中，减轻db的压力
            user = userService.getUser(userId);
            redisOperator.set(REDIS_USER_INFO + ":" + userId, JsonUtils.objectToJson(user));
        }

        return user;
    }
}
