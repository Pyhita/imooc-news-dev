package com.imooc.api;

import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseController {

    protected static final String MOBILE_SMSCODE = "mobile:smscode";
    protected static final String REDIS_USER_TOKEN = "redis:user:token";
    protected static final int COOKIE_MONTH = 30 * 24 * 60 * 60;
    protected static final int COOKIE_DELETE = 0
            ;
    protected static final String REDIS_USER_INFO = "redis:user:info";

    @Value("${website.domain-name}")
    private String DOMAIN_NAME;

    @Autowired
    protected RedisOperator redisOperator;

    protected Map<String, String> getError(BindingResult bindingResult) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errors = bindingResult.getFieldErrors();

        for (FieldError error : errors) {
            // 发生错误时对应的某个属性
            String field = error.getField();
            // 发生错误时，对应的错误信息
            String message = error.getDefaultMessage();
            map.put(field, message);
        }

        return map;
    }

    protected void setCookie(HttpServletRequest request,
                             HttpServletResponse response,
                             String cookieName,
                             String cookieValue,
                             Integer maxAge) {

        try {
            cookieValue = URLEncoder.encode(cookieValue, "UTF-8");
            setCookieValue(request, response, cookieName, cookieValue, maxAge);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    protected void setCookieValue(HttpServletRequest request,
                             HttpServletResponse response,
                             String cookieName,
                             String cookieValue,
                             Integer maxAge) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(maxAge);
//        cookie.setDomain("imoocnews.com");
        cookie.setDomain(DOMAIN_NAME);
        cookie.setPath("/");

        response.addCookie(cookie);
    }


}
