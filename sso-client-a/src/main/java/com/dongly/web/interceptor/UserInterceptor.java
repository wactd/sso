package com.dongly.web.interceptor;

import com.dongly.common.utils.BeanToMapUtil;
import com.dongly.constant.EnumSSO;
import com.dongly.web.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by tiger on 2017/7/19.
 */
public class UserInterceptor extends HandlerInterceptorAdapter {

    private RedisTemplate<String, String> redisTemplate;

    public UserInterceptor(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String[] noFilters = {"/baidu"};

        String uri = request.getRequestURI();

        boolean beFilter = true;
        for (String s : noFilters) {
            if (uri.contains(s)) {
                beFilter = false;
            }
        }

        if (beFilter) {
            return true;
        }

        Cookie[] cookies = request.getCookies();

        if (cookies == null || cookies.length == 0) {
            response.sendRedirect(EnumSSO.SSO_REDIRECT_URL.getValue() + request.getRequestURL());
            return false;
        }

        Cookie cookie = Arrays.stream(cookies).filter(co-> EnumSSO.SSO_TOKEN.getValue()
                .equals(co.getName())).findFirst().get();

        if (Objects.isNull(cookie) || !redisTemplate.hasKey(cookie.getValue())) {
            response.sendRedirect(EnumSSO.SSO_REDIRECT_URL.getValue() + request.getRequestURL());
            return false;
        }

        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cookie.getValue());
        Map<Object, Object> entries = operations.entries();

        User user = BeanToMapUtil.convertMap(User.class, entries);

        request.setAttribute(EnumSSO.SSO_TOKEN.getValue(), cookie.getValue());

        System.out.println(user);

        return true;
    }

}
