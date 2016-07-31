package com.hand.demo.security.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.demo.dto.PersistentLogin;
import com.hand.demo.mapper.PersistentLoginMapper;

@Service
@Transactional
public class PersistentTokenRepositoryImpl implements PersistentTokenRepository {

    static final Logger logger = LoggerFactory.getLogger(PersistentTokenRepositoryImpl.class);

    @Autowired
    private PersistentLoginMapper persistentLoginMapper;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        logger.info("Creating Token for user : {}", token.getUsername());
        PersistentLogin persistentLogin = new PersistentLogin();
        persistentLogin.setUsername(token.getUsername());
        persistentLogin.setSeries(token.getSeries());
        persistentLogin.setToken(token.getTokenValue());
        persistentLogin.setLast_used(token.getDate());
        persistentLoginMapper.persist(persistentLogin);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        logger.info("Updating Token for series : {}", series);
        PersistentLogin persistentLogin = new PersistentLogin();
        persistentLogin.setSeries(series);
        persistentLogin.setToken(tokenValue);
        persistentLogin.setLast_used(lastUsed);
        persistentLoginMapper.update(persistentLogin);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        logger.info("Fetch Token if any for seriesId : {}", seriesId);
        PersistentLogin tmp = new PersistentLogin();
        tmp.setSeries(seriesId);
        PersistentLogin persistentLogin = persistentLoginMapper.select(tmp);
        if (persistentLogin == null) {
            logger.info("Token not found...");
            return null;
        }
        return new PersistentRememberMeToken(persistentLogin.getUsername(), persistentLogin.getSeries(),
                persistentLogin.getToken(), persistentLogin.getLast_used());

    }

    @Override
    public void removeUserTokens(String username) {
        logger.info("Removing Token if any for user : {}", username);
        PersistentLogin tmp = new PersistentLogin();
        tmp.setUsername(username);
        PersistentLogin persistentLogin = persistentLoginMapper.select(tmp);
        if (persistentLogin != null) {
            logger.info("rememberMe was selected");
            persistentLoginMapper.delete(persistentLogin);
        }
    }

}
