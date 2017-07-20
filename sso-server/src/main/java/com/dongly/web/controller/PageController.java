package com.dongly.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by tiger on 2017/7/19.
 */

@Controller
public class PageController {

    @GetMapping(value = {"/", "/login", "/index"})
    public String login(HttpServletRequest request, String redirectUrl, ModelMap map) {

        if (StringUtils.isEmpty(redirectUrl)) {
            map.addAttribute("redirectUrl", request.getHeader("referer"));
        } else {
            map.addAttribute("redirectUrl", redirectUrl);
        }
        return "login";
    }



    @GetMapping(value = {"/register"})
    public String register() {
        return "register";
    }

}
