package com.hand.demo.security;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * @author guanghui.liu
 */
public class PasswordManager {

    private String salt;
    private String defaultPassword = "111111";

    public String getDefaultPassword() {
        return defaultPassword;
    }

    public void setDefaultPassword(String defaultPassword) {
        this.defaultPassword = defaultPassword;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * 加盐MD5处理.
     */
    public String encode(String rawPass) {
        Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
        return passwordEncoder.encodePassword(rawPass, salt);
    }

    /**
     * 调试用main函数-实际产生的对象还是autowire自动产生的.
     * 
     * @param args
     */
    public static void main(String[] args) {
        PasswordManager pm = new PasswordManager();
        pm.setSalt("hoshi");
        System.out.println(pm.encode("111111"));
        
        String s1 = "Programming";
        String s2 = new String("Programming");
        String s3 = "Program";
        String s4 = "ming";
        String s5 = "Program" + "ming";
        String s6 = s3 + s4;
        System.out.println(s1 == s2);
        System.out.println(s1 == s5);
        System.out.println(s1 == s6);
        System.out.println(s1 == s6.intern());
        System.out.println(s2 == s2.intern());
    }
}
