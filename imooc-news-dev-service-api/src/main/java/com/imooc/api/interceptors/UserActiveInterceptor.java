package com.imooc.api.interceptors;

import com.imooc.api.interceptors.BaseInterceptor;
import com.imooc.enums.UserStatus;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户激活状态检查修改器
 */
public class UserActiveInterceptor extends BaseInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String userId = request.getHeader("headerUserId");
        String userJson = redisOperator.get(REDIS_USER_INFO + ":" + userId);
        AppUser user = null;

        if (StringUtils.isBlank(userJson)) {
            GraceException.display(ResponseStatusEnum.UN_LOGIN);
            return false;
        } else {
            user = JsonUtils.jsonToPojo(userJson, AppUser.class);
        }

        if (user.getActiveStatus() == null ||
                user.getActiveStatus() != UserStatus.ACTIVE.type) {
            GraceException.display(ResponseStatusEnum.USER_INACTIVE_ERROR);
            return false;
        }
        return true;
    }

    /**
     * 请求访问controller之后，渲染视图之前
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    /**
     * 渲染视图之后
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {

    }
}
