package com.imooc.api.interceptors;

import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.utils.RedisOperator;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import sun.security.provider.PolicyParser;

public class BaseInterceptor {

    protected static final String REDIS_USER_INFO = "redis:user:info";

    @Autowired
    protected RedisOperator redisOperator;

    protected boolean verifyIdAndToken(String id,
                                       String token,
                                       String prefix) {
        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(token)) {
            String redisToken = redisOperator.get(REDIS_USER_INFO + ":" + prefix);
            if (StringUtils.isBlank(redisToken)) {
                // 说明token过期了
                GraceException.display(ResponseStatusEnum.UN_LOGIN);
                return false;
            } else {
                if (!redisToken.equals(token)) {
                    GraceException.display(ResponseStatusEnum.TICKET_INVALID);
                    return false;
                }
            }

        } else {
            GraceException.display(ResponseStatusEnum.UN_LOGIN);
            return false;
        }

        return true;
    }


}
