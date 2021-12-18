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
    protected static final String REDIS_USER_TOKEN = "redis_user_token";
    protected static final String REDIS_ADMIN_TOKEN = "redis_admin_token";
    protected static final String REDIS_USER_INFO = "redis_user_info";

    protected static final String REDIS_ALL_CATEGORY = "redis_all_category";

    protected static final String REDIS_WRITER_FANS_COUNTS = "redis_writer_fans_counts";
    protected static final String REDIS_MY_FOLLOW_COUNTS = "redis_my_follow_counts";

    protected static final String REDIS_ARTICLE_READ_COUNTS = "redis_article_read_counts";
    protected static final String REDIS_ALREADY_READ = "redis_already_read";

    protected static final String REDIS_ARTICLE_COMMENT_COUNTS = "redis_article_comment_counts";

    protected static final int COOKIE_MONTH = 30 * 24 * 60 * 60;
    protected static final int COOKIE_DELETE = 0;


    protected static final Integer COMMON_START_PAGE = 1;
    protected static final Integer COMMON_PAGE_SIZE = 10;

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

    protected void deleteCookie(HttpServletRequest request,
                             HttpServletResponse response,
                             String cookieName) {
        try {
            String deleteValue = URLEncoder.encode("", "utf-8");
            setCookieValue(request, response, cookieName, deleteValue, COOKIE_DELETE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}
