package com.hand.activiti.gateway;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class DelegateExpression1 implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("DelegateExpression1 running");
		execution.setVariable("delegateExpression", "i am DelegateExpression1");
		System.out.println("before sleep");
		Thread.sleep(10000);//说明是异步的
		System.out.println("after sleep");
	}

}
