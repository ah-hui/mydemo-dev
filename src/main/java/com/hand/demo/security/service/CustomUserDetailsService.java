package com.hand.demo.security.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.demo.dto.User;
import com.hand.demo.service.ISysUserService;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    @Qualifier("sysUserService")
    private ISysUserService sysUserService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User tmp = new User();
        tmp.setLoginName(username);
        User user = sysUserService.selectBySelective(tmp);
        logger.info("User : {}", user);
        if (user == null) {
            logger.info("User not found");
            throw new UsernameNotFoundException("Username not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getLoginName(), user.getHashedPassword(), user.isEnabled(),
                !user.isAccountExpired(), !user.isPasswordExpired(), !user.isAccountLocked(), getGrantedAuthorities(user));
    }

    private List<GrantedAuthority> getGrantedAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        logger.info("authorities : {}", authorities);
        return authorities;
    }

}
