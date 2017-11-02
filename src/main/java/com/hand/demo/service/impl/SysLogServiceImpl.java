package com.hand.demo.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hand.demo.dto.SysLog;
import com.hand.demo.service.ISysLogService;

@Service("sysLogService")
@Transactional
public class SysLogServiceImpl implements ISysLogService {

    @Override
    public int add(SysLog log) {
        System.out.println("=======================666=====================");
        return 1;
    }

}
