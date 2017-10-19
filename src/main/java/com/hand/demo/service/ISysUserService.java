package com.hand.demo.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.hand.demo.dto.User;

public interface ISysUserService {

    // @Secured不支持EL表达式
    // @PreAuthorize支持EL表达式，方法执行前校验
    // @PostAuthorize支持EL表达式，方法执行后校验-we are making sure that a logged-in user
    // can only get it’s own User type object
    // 表达式不要用hasRole,用hasAuthority,因为hasRole在ajax时判定错误直接403

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
    // @PostAuthorize ("returnObject.type == authentication.name")
    int batchDeleteUser(List<User> users);

    /**
     * 查询所有user
     */
    // @PreAuthorize("hasAnyAuthority('ADMIN', 'USER', 'SALES', 'MANAGEMENT')")
    List<User> selectUsers();

    /**
     * 根据id查询user
     */
    User selectByPrimaryKey(Long userId);

    /**
     * 根据id查询user
     */
    User selectBySelective(User user);

    /**
     * 修改密码
     */
    int updatePassword(User user);

}