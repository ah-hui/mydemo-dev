package com.hand.activiti.gateway;

public class ActivitiExpression1 {

	private String name = "lgh";

	public String getName() {
		System.out.println("ActivitiExpression1.getName() is running.");
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void handle() {
		System.out.println("ActivitiExpression1 is running handle().");
		System.out.println("before sleep");
		System.out.println(Thread.currentThread().getId());
		System.out.println(Thread.currentThread().getThreadGroup().getName());
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("after sleep");
	}

}
