package com.hand.activiti.gateway;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class ServiceTask1 implements JavaDelegate{

	//因为实现JavaDelegate,该类在spring的编制之外,如果要调用其他的service,必须要用如下方式注入
	
//	protected IMsgSaveService msgSaveService;
//
//	public SmsServiceTask() {
//		ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
//		msgSaveService = (IMsgSaveService) applicationContext.getBean(IMsgSaveService.class);
//	}
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("ServiceTask1 running");
		execution.setVariable("task", "i am task1");
	}

}
