/*
 * #{copyright}#
 */
package com.hand.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.demo.dto.User;
import com.hand.demo.mapper.UserMapper;
import com.hand.demo.security.PasswordManager;
import com.hand.demo.service.ISysUserService;

@Service("sysUserService")
@Transactional
public class SysUserServiceImpl implements ISysUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordManager passwordManager;

    @CacheEvict(value = { "selectUsers", "selectByPrimaryKey", "selectBySelective" }, allEntries = true)
    @Override
    public User saveUser(User user) {
        if (user.getId() == null) {
            user.setHashedPassword(passwordManager.encode(user.getHashedPassword()));
            user.setEnabled(true);
            user.setAccountExpired(false);
            user.setAccountLocked(false);
            user.setPasswordExpired(false);
            userMapper.insertUser(user);
        } else {
            userMapper.updateByPrimaryKeySelective(user);
        }
        return user;
    }

    @CacheEvict(value = { "selectUsers", "selectByPrimaryKey", "selectBySelective" }, allEntries = true)
    @Override
    public int deleteUser(Long userId) {
        return userMapper.deleteUser(userId);
    }

    /**
     * CRUD (Create 创建，Retrieve 读取，Update 更新，Delete 删除) 操作中，除了 R
     * 具备幂等性，其他三个发生的时候都可能会造成缓存结果和数据库不一致。为了保证缓存数据的一致性，在进行 CUD
     * 操作的时候我们需要对可能影响到的缓存进行更新或者清除
     * @Cacheable --- 对结果缓存
     * @CacheEvict --- 清楚缓存 delete
     * @CachePut --- 更新缓存 update
     * 如果你的 CUD 能够返回 City 实例，也可以使用 @CachePut 更新缓存策略
     */

    @Cacheable("selectUsers") // R 使用redis缓存方法执行结果
    @Override
    public List<User> selectUsers() {
        return userMapper.selectUsers();
    }

    @Cacheable("selectByPrimaryKey") // R 使用redis缓存方法执行结果
    @Override
    public User selectByPrimaryKey(Long userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @CacheEvict(value = { "selectUsers", "selectByPrimaryKey", "selectBySelective" }, allEntries = true)
    @Override
    public int batchDeleteUser(List<User> users) {
        return userMapper.batchDeleteUser(users);
    }

    @Cacheable("selectBySelective") // R 使用redis缓存方法执行结果
    @Override
    public User selectBySelective(User user) {
        return userMapper.selectBySelective(user);
    }

    @CacheEvict(value = { "selectUsers", "selectByPrimaryKey", "selectBySelective" }, allEntries = true)
    @Override
    public int updatePassword(User user) {
        user.setHashedPassword(passwordManager.encode(user.getHashedPassword()));
        return userMapper.updatePassword(user);
    }
}