/*
 * #{copyright}#
 */
package com.hand.demo.constant;

/**
 * 常量.
 * 
 * @author guanghui.liu
 */
public class UserConstants {

    /**
     * 专用KEY标记存放在redis模板的描述 - 用户管理 - 删除用户.
     */
    public static final String REDIS_DEC_KEY_USER_DELETE = "demo.user.delete.key";
    
    /**
     * 专用KEY标记过期时间-60分钟.
     */
    public static final int KEY_EXPIRE_SIXTY_MIN = 60 * 60;
}
