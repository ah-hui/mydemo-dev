package org.activiti.designer.test;

import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestPgwProcess {

	private String filename = "E:\\LearnWorkspace\\mydemo-dev\\src\\main\\resources\\bpmn20\\parallelGateWay.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("pgwProcess.bpmn20.xml", new FileInputStream(filename))
				.deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		TaskService taskService = activitiRule.getTaskService();
		IdentityService identityService = activitiRule.getIdentityService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("pgwProcess", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

		String procInstanceId = processInstance.getId();
		String procDefinitionId = processInstance.getProcessDefinitionId();

		// 任务-故意不完成全部任务
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstanceId).list();
		System.out.println("1st group:");
		for (Task task : tasks) {
			System.out.println("Task: -name=" + task.getName());
			taskService.complete(task.getId(), variableMap);
			break;//不管第二个任务
		}
		
		// 任务-故意不完成全部任务-查看任务当前节点-并完成当前任务（并行的其中一条已完成，单另一条还剩一个任务）
		tasks = taskService.createTaskQuery().processInstanceId(procInstanceId).list();
		System.out.println("1st group:");
		for (Task task : tasks) {
			System.out.println("Task: -name=" + task.getName());
			taskService.complete(task.getId(), variableMap);
		}
		
		// 还剩下一个任务此时
		tasks = taskService.createTaskQuery().processInstanceId(procInstanceId).list();
		System.out.println("1st group:");
		for (Task task : tasks) {
			System.out.println("Task: -name=" + task.getName());
			//taskService.complete(task.getId(), variableMap);
		}
		
		/*
		// 第1组任务
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstanceId).list();
		System.out.println("1st group:");
		for (Task task : tasks) {
			System.out.println("Task: -name=" + task.getName());
			taskService.complete(task.getId(), variableMap);
		}

		// 第2组任务
		tasks = taskService.createTaskQuery().processInstanceId(procInstanceId).list();
		System.out.println("2nd group:");
		for (Task task : tasks) {
			System.out.println("Task: -name=" + task.getName());
			taskService.complete(task.getId(), variableMap);
		}

		// 第3组任务
		tasks = taskService.createTaskQuery().processInstanceId(procInstanceId).list();
		System.out.println("3rd group:");
		for (Task task : tasks) {
			System.out.println("Task: -name=" + task.getName());
			taskService.complete(task.getId(), variableMap);
		}
		*/
	}
}