/*
 * #{copyright}#
 */
package com.hand.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.demo.dto.User;
import com.hand.demo.mapper.UserMapper;
import com.hand.demo.service.IUserService;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User saveUser(User user) {
        if(user.getId() == null){
            userMapper.insertUser(user);
        }else{
            userMapper.updateByPrimaryKeySelective(user);
        }
        return user;
    }

    @Override
    public int deleteUser(Long userId) {
        return userMapper.deleteUser(userId);
    }

    @Override
    public List<User> selectUsers() {
        return userMapper.selectUsers();
    }

    @Override
    public User selectByPrimaryKey(Long userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public int batchDeleteUser(List<User> users) {
        return userMapper.batchDeleteUser(users);
    }

    @Override
    public User selectBySelective(User user) {
        return userMapper.selectBySelective(user);
    }
}