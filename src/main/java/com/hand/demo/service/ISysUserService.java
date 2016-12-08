package com.hand.demo.service;

import java.util.List;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;

import com.hand.demo.dto.User;

public interface ISysUserService {

//	@Secured不支持EL表达式
//	@PreAuthorize支持EL表达式，方法执行前校验
//	@PostAuthorize支持EL表达式，方法执行后校验-we are making sure that a logged-in user can only get it’s own User type object
	
    /**
     * 创建和更新user
     */
	@Secured({ "USER", "ADMIN", "SALES", "MANAGEMENT" })
    User saveUser(User user);

    /**
     * 删除user
     */
	@PreAuthorize("hasRole('ADMIN') AND hasRole('MANAGEMENT')")
    int deleteUser(Long userId);
    
    /**
     * 批量删除user
     */
	@Secured({ "USER", "ADMIN", "SALES", "MANAGEMENT" })
//	@PostAuthorize ("returnObject.type == authentication.name")
    int batchDeleteUser(List<User> users);

    /**
     * 查询所有user
     */
	@Secured({ "USER", "ADMIN", "SALES", "MANAGEMENT" })
    List<User> selectUsers();

    /**
     * 根据id查询user
     */
	@Secured({ "USER", "ADMIN", "SALES", "MANAGEMENT" })
    User selectByPrimaryKey(Long userId);
    
    /**
     * 根据id查询user
     */
	@Secured({ "USER", "ADMIN", "SALES", "MANAGEMENT" })
    User selectBySelective(User user);
    
    /**
     * 修改密码
     */
	@Secured({ "USER", "ADMIN", "SALES", "MANAGEMENT" })
    int updatePassword(User user);

}