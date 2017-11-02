package com.hand.annotation;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hand.demo.dto.SysLog;
import com.hand.demo.service.ISysLogService;
import com.hand.util.SpringContextHolder;

/**
 * 日志 切点类
 * 
 * @author Jessey
 */
@Aspect
@Component
public class SysLogAspect {

    @Autowired
    private ISysLogService logService;

    private ObjectMapper objectMapper;

    // 本地异常日志记录对象
    private static final Logger logger = LoggerFactory.getLogger(SysLogAspect.class);

    @Pointcut("@annotation(com.hand.annotation.SysServiceLog)")
    public void serviceAspect() {
    }

    @Pointcut("@annotation(com.hand.annotation.SysControllerLog)")
    public void controllerAspect() {
    }

    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = req.getSession();
        // 读取session中的用户
        // 经过spring-security认证后，security会把一个SecurityContextImpl对象存储到session中，此对象中有当前用户的各种资料
//        SecurityContextImpl securityContextImpl = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
//        System.out.println(securityContextImpl.getAuthentication().getName());
        // 请求的ip
        String ip = req.getRemoteAddr();
        try {
            // ==========控制台输出==========//
            System.out.println("=====新的HTTP请求start=====");
            System.out.println("请求方法：" + joinPoint.getTarget().getClass().getName() + "."
                    + joinPoint.getSignature().getName() + "()");
            System.out.println("方法描述：" + getControllerMethodDescription(joinPoint));
//            System.out.println("请求人：" + securityContextImpl.getAuthentication().getName());
            System.out.println("请求人：");
            System.out.println("请求IP：" + ip);
            // ==========数据库日志==========//
            SysLog log = new SysLog();
            log.setDescription(getControllerMethodDescription(joinPoint));
            log.setMethod(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()");
            log.setType(0);
            log.setRequestIp(ip);
            log.setExceptionCode(null);
            log.setExceptionDetail(null);
            log.setParams(null);
            log.setCreatedBy(-1L);// 系统内建日志
            // 考虑将登录user存放到session
            log.setCreationDate(new Date());
            // 保存数据库
            logService.add(log);
            System.out.println("=====新的HTTP请求  end=====");
        } catch (Exception e) {
            // 记录本地异常日志
            logger.error("==前置通知异常！==");
            logger.error("异常信息:{}", e.getMessage());
        }
    }

    @AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = req.getSession();
        // 读取session中的用户
        // 经过spring-security认证后，security会把一个SecurityContextImpl对象存储到session中，此对象中有当前用户的各种资料
//        SecurityContextImpl securityContextImpl = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
//        System.out.println(securityContextImpl.getAuthentication().getName());
        // 请求的ip
        String ip = req.getRemoteAddr();
        // 获取用户请求方法的参数并序列化为json
        String params = "";
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                try {
                    params += objectMapper.writeValueAsString(joinPoint.getArgs()[i]) + ";";
                } catch (JsonProcessingException ex) {
                    ex.printStackTrace();
                }
            }
        }
        try {
            /* ========控制台输出========= */
            System.out.println("=====异常通知开始=====");
            System.out.println("异常代码:" + e.getClass().getName());
            System.out.println("异常信息:" + e.getMessage());
            System.out.println("异常方法:"
                    + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            System.out.println("方法描述:" + getServiceMethodDescription(joinPoint));
//            System.out.println("请求人:" + securityContextImpl.getAuthentication().getName());
            System.out.println("请求人:");
            System.out.println("请求IP:" + ip);
            System.out.println("请求参数:" + params);
            /* ==========数据库日志========= */
            SysLog log = new SysLog();
            log.setDescription(getServiceMethodDescription(joinPoint));
            log.setExceptionCode(e.getClass().getName());
            log.setType(1);
            log.setExceptionDetail(e.getMessage());
            log.setMethod(
                    (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            log.setParams(params);
            log.setCreatedBy(-1L);// 系统内建日志
            log.setCreationDate(new Date());
            log.setRequestIp(ip);
            // 保存数据库
            logService.add(log);
            System.out.println("=====异常通知结束=====");
        } catch (Exception ex) {
            // 记录本地异常日志
            logger.error("==异常通知异常==");
            logger.error("异常信息:{}", ex.getMessage());
        }
        /* ==========记录本地异常日志========== */
        logger.error("异常方法:{}异常代码:{}异常信息:{}参数:{}",
                joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(),
                e.getMessage(), params);
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     * 
     * @param joinPoint
     *            切点
     * @return 方法描述
     * @throws Exception
     */
    private static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(SysControllerLog.class).description();
                    break;
                }
            }
        }
        return description;
    }

    /**
     * 获取注解中对方法的描述信息 用于service层注解
     * 
     * @param joinPoint
     *            切点
     * @return 方法描述
     * @throws Exception
     */
    private static String getServiceMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(SysControllerLog.class).description();
                    break;
                }
            }
        }
        return description;
    }

}
