package com.hand.demo.mapper;

import com.hand.demo.dto.PersistentLogin;

public interface PersistentLoginMapper {
    
    int persist(PersistentLogin record);//insert

    int update(PersistentLogin record);

    int delete(PersistentLogin record);

    PersistentLogin select(PersistentLogin record);
}
