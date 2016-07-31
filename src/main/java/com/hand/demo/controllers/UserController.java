package com.hand.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hand.demo.constant.UserConstants;
import com.hand.demo.dto.ResponseData;
import com.hand.demo.dto.User;
import com.hand.demo.service.ISysUserService;

/**
 * @author Jessey
 */
@Controller
@RequestMapping(value = "/user") // 这里的RequestMapping,绑定类,可加可不加
public class UserController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 创建
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData insert(HttpServletRequest request, @RequestBody User user) {
        ResponseData result = new ResponseData();
        List<User> list = new ArrayList<User>();
        list.add(userService.saveUser(user));
        result.setRows(list);
        return result;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, Long userId) {
        userService.deleteUser(userId);
        String key = generateBaseToken();
        redisTemplate.opsForValue().set(UserConstants.REDIS_DEC_KEY_USER_DELETE + ":" + key,
                userId.toString(), UserConstants.KEY_EXPIRE_SIXTY_MIN, TimeUnit.SECONDS);
        String uk = redisTemplate.opsForValue().get(UserConstants.REDIS_DEC_KEY_USER_DELETE + ":" + key);
        System.out.println(uk);
        return new ResponseData();
    }

    /**
     * 批量删除
     */
    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData batchDelete(HttpServletRequest request, @RequestBody List<User> users) {
        userService.batchDeleteUser(users);
        return new ResponseData();
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody User user) {
        ResponseData result = new ResponseData();
        List<User> list = new ArrayList<User>();
        list.add(userService.saveUser(user));
        result.setRows(list);
        return result;
    }

    /**
     * 查询
     */
    @RequestMapping(value = "/select")
    @ResponseBody
    public ResponseData select(HttpServletRequest request,
            @RequestParam(value = "userId", required = false) Long userId) {
        ResponseData result = new ResponseData();
        List<User> list = new ArrayList<User>();
        if (userId == null) {
            list = userService.selectUsers();
        } else {
            list.add(userService.selectByPrimaryKey(userId));
        }
        result.setRows(list);
        return result;
    }

    /**
     * 生成baseToken.
     * 
     * @return 返回 UUID 形式的 baseToken
     */
    private String generateBaseToken() {
        return UUID.randomUUID().toString();
    }
}