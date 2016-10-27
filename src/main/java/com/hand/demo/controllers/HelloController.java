package com.hand.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Jessey
 */
@Controller
public class HelloController {

    /**
     * 登录
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    public ModelAndView login(@RequestParam(value = "error", required = false) String error) {
        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("message", "Username or password not recognised - please try again.");
        }
        model.setViewName("login");
        return model;
    }

    /**
     * 注册
     */
    @RequestMapping(value = "/registerUser")
    @ResponseBody
    public ModelAndView registerUser(@RequestParam(value = "loginName") String loginName,
            @RequestParam(value = "email") String email, @RequestParam(value = "password") String password,
            @RequestParam(value = "rptPassword") String rptPassword) {
        ModelAndView model = new ModelAndView();
        // 创建用户
        model.setViewName("redirect:login.html");
        return model;
    }

}