package com.hand.demo.mapper;

import java.util.List;

import com.hand.demo.dto.User;

public interface UserMapper {

    int insertUser(User record);

    int deleteUser(Long userId);
    
    int batchDeleteUser(List<User> users);

    int updateByPrimaryKeySelective(User record);

    User selectByPrimaryKey(Long userId);

    List<User> selectUsers();

}