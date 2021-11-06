package com.imooc.user.controller;

import com.imooc.pojo.bo.RegisterLoginBO;
import com.imooc.api.BaseController;
import com.imooc.api.controller.user.PassportControllerApi;
import com.imooc.enums.UserStatus;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.user.service.UserService;
import com.imooc.utils.IPUtil;
import com.imooc.utils.SMSUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

@RestController
public class PassportController extends BaseController implements PassportControllerApi {

    static final Logger logger = LoggerFactory.getLogger(PassportController.class);

    @Autowired
    private SMSUtils smsUtils;
    @Autowired
    private UserService userService;

    @Override
    public GraceJSONResult getSMSCode(String mobile, HttpServletRequest request) {
        // 获得用户IP
        String userIp = IPUtil.getRequestIp(request);

        // 根据用户IP进行限制，限制用户在60s之内只能发送一次
        redisOperator.setnx60s(MOBILE_SMSCODE + ":" + userIp, userIp);

//        String random = (int) ((Math.random() * 9 + 1) * 10000) + "";
        String random = "123456";
        smsUtils.sendSMS(" ", random);

        // 将验证码存入redis，用于后续验证
        redisOperator.set(MOBILE_SMSCODE + ":" + mobile, random, 30 * 60);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult doLogin(RegisterLoginBO registerLoginBO,
                                   BindingResult result,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        if (result.hasErrors()) {
            Map<String, String> errorMap = getError(result);
            return GraceJSONResult.errorMap(errorMap);
        }

        String mobile = registerLoginBO.getMobile();
        String smsCode = registerLoginBO.getSmsCode();

        // 1 验证验证码是否匹配
        String redisSMSCode = redisOperator.get(MOBILE_SMSCODE + ":" + mobile);
        if (StringUtils.isBlank(redisSMSCode) ||
                !redisSMSCode.equals(smsCode)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }

        // 2 登录或者注册
        AppUser user = userService.queryMobileIsExists(mobile);
        if (user != null &&
                user.getActiveStatus() == 2) {
            // 用户账号已经被冻结，直接抛出异常
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_FROZEN);
        } else if (user == null) {
            user = userService.createUser(mobile);
        }

        // 3 保存用户的分布式会话到redis
        Integer activeStatus = user.getActiveStatus();
        if (activeStatus != UserStatus.FROZEN.type) {
            // 保存token到redis
            String uToken = UUID.randomUUID().toString();
            redisOperator.set(REDIS_USER_TOKEN + ":" + user.getId(), uToken);

            // 保存用户的id和token到cookie中
            setCookie(request, response, "utoken", uToken, COOKIE_MONTH);
            setCookie(request, response, "uid", user.getId(), COOKIE_MONTH);
        }

        // 4 用户登录成功，删除redis中短信验证码
        redisOperator.del(MOBILE_SMSCODE + ":" + mobile);

        // 5 返回用户状态
        return GraceJSONResult.ok(activeStatus);
    }

    @Override
    public GraceJSONResult logout(String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        // 退出登录：1 删除存储在redis中的token 2 删除存储在cookie中的信息
        redisOperator.del(REDIS_USER_TOKEN + ":" + userId);
        setCookie(request, response, "utoken", "", COOKIE_DELETE);
        setCookie(request, response, "uid", "", COOKIE_DELETE);

        return GraceJSONResult.ok();
    }
}
