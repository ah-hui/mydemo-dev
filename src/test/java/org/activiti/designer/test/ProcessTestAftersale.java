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
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestAftersale {

	private String filename = "E:\\LearnWorkspace\\mydemo-dev\\src\\main\\resources\\bpmn20\\after_sale.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("after_sale.bpmn20.xml", new FileInputStream(filename))
				.deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		TaskService taskService = activitiRule.getTaskService();
		IdentityService identityService = activitiRule.getIdentityService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		identityService.setAuthenticatedUserId("lgh");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("after_sale", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());
		String procInstanceId = processInstance.getId();
		String procDefinitionId = processInstance.getProcessDefinitionId();

		// Test获得的结论：
		// 1.不管在complete时传不传vars,老的vars都将在下个任务继续生效,也就是说vars是不断累加到全局的
		// 2.每次taskQuery到的都是当前的任务节点,因为可能有并行任务,所以是个list

		// 第1个任务-派工
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstanceId).list();
		for (Task task : tasks) {
			System.out.println("Task: -name=" + task.getName());
			System.out.println("      -assignee=" + task.getAssignee());
			variableMap.put("engineer", "xiaoliu");
			taskService.complete(task.getId(), variableMap);
		}

		// 第2个任务-确认接单(预约时间)
		tasks = taskService.createTaskQuery().processInstanceId(procInstanceId).list();
		for (Task task : tasks) {
			System.out.println("Task: -name=" + task.getName());
			System.out.println("      -assignee=" + task.getAssignee());
			// variableMap.put("engineer", "xiaoliu1");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("flag", "1");// 1下个任务0返回到“派工任务”
			taskService.complete(task.getId(), map);
		}

		// 第3个任务-现场签到
		tasks = taskService.createTaskQuery().processInstanceId(procInstanceId).list();
		for (Task task : tasks) {
			System.out.println("Task: -name=" + task.getName());
			System.out.println("      -assignee=" + task.getAssignee());
			Map<String, Object> map = new HashMap<String, Object>();
			taskService.complete(task.getId(), map);
		}

		// 第4个任务-维修完成(结果上传)
		// tasks =
		// taskService.createTaskQuery().processInstanceId(procInstanceId).list();
		// for (Task task : tasks) {
		// System.out.println("Task: -name=" + task.getName());
		// System.out.println(" -assignee=" + task.getAssignee());
		// Map<String, Object> map = new HashMap<String, Object>();
		// taskService.complete(task.getId(), map);
		// }

		// 回退至派工
		tasks = taskService.createTaskQuery().processInstanceId(procInstanceId).list();
		for (Task task : tasks) {
			TaskEntity curTask = (TaskEntity) taskService.createTaskQuery().taskId(task.getId()).singleResult();
			// 取得流程定义
			ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
					.getDeployedProcessDefinition(curTask.getProcessDefinitionId());

			ActivityImpl targetActivityImpl = ((ProcessDefinitionImpl) processDefinition).findActivity("usertask2");// endevent1-usertask2

			CommandExecutor commandExecutor = ((RuntimeServiceImpl) runtimeService).getCommandExecutor();
			Map<String, Object> map = new HashMap<String, Object>();
			commandExecutor.execute(new JumpTaskCmd(curTask, targetActivityImpl, map));

		}

		tasks = taskService.createTaskQuery().processInstanceId(procInstanceId).list();
		for (Task task : tasks) {
			System.out.println("Task: -name=" + task.getName());
			System.out.println("      -assignee=" + task.getAssignee());
			Map<String, Object> map = new HashMap<String, Object>();
			taskService.complete(task.getId(), map);
		}

	}
}