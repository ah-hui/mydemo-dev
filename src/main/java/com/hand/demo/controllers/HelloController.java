package com.hand.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hand.demo.dto.User;
import com.hand.demo.service.ISysUserService;

/**
 * @author Jessey
 */
@Controller
public class HelloController {

    @Autowired
    private ISysUserService userService;

    /**
     * 登录
     */
    // rememberme-bug，rememberme的cookie不会删除？
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

    @RequestMapping(value = "/index")
    @ResponseBody
    public ModelAndView index() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ModelAndView model = new ModelAndView();
        model.setViewName("index");
        List<User> list = new ArrayList<User>();
        list = userService.selectUsers();
        model.addObject("users", list);
        return model;
    }

    // @RequestMapping(value="/logout")
    // public String logoutPage (HttpServletRequest request, HttpServletResponse
    // response) {
    // Authentication auth =
    // SecurityContextHolder.getContext().getAuthentication();
    // if (auth != null){
    // new SecurityContextLogoutHandler().logout(request, response, auth);
    // }
    // return "redirect:/login?logout";
    // }

    private String getPrincipal() {
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

}