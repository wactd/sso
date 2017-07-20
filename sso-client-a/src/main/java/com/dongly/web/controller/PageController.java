package com.dongly.web.controller;

import com.dongly.common.utils.BeanToMapUtil;
import com.dongly.constant.EnumSSO;
import com.dongly.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by tiger on 2017/7/19.
 */

@Controller
public class PageController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping(value = {"/", "/index"})
    public String index(ModelMap map) {
        map.put("abcd", "123132");
        return "index";
    }

    @GetMapping(value = "/baidu")
    public String baidu(HttpServletRequest request, ModelMap map) throws Exception {

        String token = request.getAttribute(EnumSSO.SSO_TOKEN.getValue()).toString();
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(token);

        User user = BeanToMapUtil.convertMap(User.class, operations.entries());
        map.addAttribute("username", user.getUsername());
        System.out.println(user);
        return "list";
    }

    @GetMapping(value = {"delete"})
    public String delete(HttpServletRequest request, ModelMap map) {


        // TODO cookie一定要判断null
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            // if ("SSO_TOKEN".equals(cookie.getName())) {
            //     cookie.setMaxAge(0);
            //     cookie.setPath("/");
            //     cookie.setDomain();
            //     break;
            // }
        }
        return "index";
    }

}
