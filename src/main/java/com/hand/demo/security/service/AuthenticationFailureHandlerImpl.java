package com.hand.demo.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    private final static Logger logger = LoggerFactory.getLogger(AuthenticationFailureHandlerImpl.class);

    private final RequestCache requestCache;

    public AuthenticationFailureHandlerImpl() {
        this.requestCache = new HttpSessionRequestCache();
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authenticationException) throws IOException, ServletException {
        // 登录之前请求的时候，在过滤器链的最后，会发现没有登录，抛出异常，这时候会将当前的request放到HttpSessionRequestCache中
        // 所以,当[1.请求前未登录;2.请求的地址不是登录地址]时,savedRequest不为空
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        logger.debug("saved Request: {}", savedRequest);
        logger.warn("Authn Failure.", authenticationException);

        if (savedRequest != null) {
            logger.debug("Retry original request of {}", savedRequest.getRedirectUrl());
            response.sendRedirect(savedRequest.getRedirectUrl());
        } else {
            logger.debug("Retry login {}", "/demo/login.html");
            response.sendRedirect("/demo/login.html");
        }
    }

}
