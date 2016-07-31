package com.hand.demo.service;

import java.util.List;

import com.hand.demo.dto.User;

public interface ISysUserService {

    /**
     * 创建和更新user
     */
    User saveUser(User user);

    /**
     * 删除user
     */
    int deleteUser(Long userId);
    
    /**
     * 批量删除user
     */
    int batchDeleteUser(List<User> users);

    /**
     * 查询所有user
     */
    List<User> selectUsers();

    /**
     * 根据id查询user
     */
    User selectByPrimaryKey(Long userId);
    
    /**
     * 根据id查询user
     */
    User selectBySelective(User user);

}