package com.hand.activiti.gateway;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class ServiceTask2 implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("ServiceTask2 running");
		String var = (String) execution.getVariable("name");
		
		System.out.println("var="+var);
		execution.setVariable("task", "i am task2");
	}

}
