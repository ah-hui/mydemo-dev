package com.hand.demo.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ModelAndView select(HttpServletRequest request) {
        return new ModelAndView("login");
    }

}