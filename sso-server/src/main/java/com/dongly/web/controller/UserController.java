package com.dongly.web.controller;

import com.dongly.web.model.ResultModel;
import com.dongly.web.model.User;
import com.dongly.web.utils.CookieUtils;
import org.omg.CORBA.TIMEOUT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by tiger on 2017/7/19.
 */

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private static final String TOKEN_NAME = "SSO_TOKEN";
    private static final Integer MAX_AGE = 60 * 60;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping(value = "/logout/{token}")
    public Object logout(@PathVariable String token, String callback) {

        redisTemplate.delete(token);
        ResultModel<Object> resultModel = ResultModel.ok(null);
        if (callback == null || "".equals(callback.trim())) {
            return resultModel;
        } else {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(resultModel);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
    }

    @GetMapping(value = "/token/{token}")
    public Object getUserByToken(HttpServletRequest request, HttpServletResponse response,
                                 @PathVariable String token, String callback) {
        Boolean hasKey = redisTemplate.hasKey(token);
        ResultModel<Object> resultModel;
        if (hasKey) {
            BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(token);
            operations.expire(1, TimeUnit.HOURS);
            resultModel = ResultModel.ok(operations.get("username"));
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (TOKEN_NAME.equals(cookie.getName())) {
                        cookie.setMaxAge(MAX_AGE);
                    }
                }
            }
        } else {
            resultModel = ResultModel.error(null);
        }

        if (StringUtils.isEmpty(callback)) {

            return resultModel;
        } else {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(resultModel);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
    }


    @PostMapping(value = "/login")
    public ResultModel<String> login(HttpServletRequest request, HttpServletResponse response,
                                     @Valid User user, BindingResult result) {
        // TODO 暂时不考虑错误
        if (result.hasErrors()) {
            System.out.println(result.getModel());
        }

        String token = UUID.randomUUID().toString().toString().replaceAll("-", "");

        BoundHashOperations<String, Object, Object> ssoUser = redisTemplate.boundHashOps(token);
        ssoUser.put("username", user.getUsername());
        ssoUser.put("gender", "Female");

        ssoUser.expire(1, TimeUnit.HOURS);

        Cookie cookie = new Cookie(TOKEN_NAME, token);
        cookie.setPath("/");
        cookie.setDomain("dongly.com");
        cookie.setMaxAge(MAX_AGE);
        response.addCookie(cookie);

        cookie = new Cookie(TOKEN_NAME, token);
        cookie.setPath("/");
        cookie.setDomain("xiaoxiao.com");
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);
        return ResultModel.ok(token);
    }

    @PostMapping("/register")
    public ResultModel<User> register(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.getModel());
        }
        System.out.println(user);
        return ResultModel.ok(null);
    }
}
