package com.hand.activiti.gateway;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class DelegateExpression2 implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("DelegateExpression2 running");
		execution.setVariable("delegateExpression", "i am DelegateExpression2");
	}

}
