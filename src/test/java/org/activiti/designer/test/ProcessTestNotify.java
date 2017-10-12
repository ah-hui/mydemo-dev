package org.activiti.designer.test;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(locations = { "classpath:spring/test-applicationContext.xml" })
public class ProcessTestNotify {

	/**
	 * 定时任务+自动执行 <serviceTask>元素，可以实现自动活动（多种使用方式-注意userTask不支持以下属性）：
	 **/
	
	/**
	 * 1.activiti:class属性为该结点对应的处理类，该类要求实现org.activiti.engine.delegate.
	 * JavaDelegate接口（在parallelGateWay中使用，其他的均在本例中讲述）
	 * 例如：<serviceTask id="usertask1" activiti:class="com.hand.activiti.gateway.DelegateExpression1"></serviceTask>
	 **/
	
	/**
	 * 2.<activiti:taskListener>元素的event属性，它一共包含三种事件："create"、"assignment"、
	 * "complete"，分别表示结点执行处理逻辑的时机为：在处理类实例化时、在结点处理逻辑被指派时、在结点处理逻辑执行完成时，
	 * 可以根据自己的需要进行指定 
	 * 例如：
	 * <userTask id="usertask1" name="任务等待" activiti:candidateUsers="lgh">
		   <extensionElements>
			   <activiti:taskListener event="complete" class="com.hand.activiti.gateway.Task1Listener" />
		   </extensionElements>
	   </userTask>
	 **/
	
	/**
	 * 3.activiti:expression 
	 * 例如：<serviceTask id="usertask1" name="test" activiti:expression="#{activitiExpression1.handle()}"></serviceTask>
	 * <serviceTask id="usertask1" name="test" activiti:expression="#{activitiExpression1.name}"></serviceTask>
	 **/
	
	/**
	 * 4.activiti:delegateExpression
	 * 例如：<serviceTask id="usertask1" activiti:delegateExpression="${delegateExpression1}"></serviceTask>
	 **/
	
	/**
	 * 关于<timeDuration>PT25S</timeDuration>
	 * 经过测试-JobExecutor每5秒检查一次act_run_job表，所以表现为：
	 * 设置5-9秒的任务，将在第10秒执行；分钟也同样，设置2min的任务，实际在125秒执行
	 */

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	RuntimeService runtimeService = processEngine.getRuntimeService();
	RepositoryService repositoryService = processEngine.getRepositoryService();
	TaskService taskService = processEngine.getTaskService();

	@Test
	public void startProcess() throws Exception {
		// 部署流程定义
		repositoryService.createDeployment().addClasspathResource("bpmn20/timer.bpmn").deploy();

		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		// ===============弃用-设置定时的开始和结束日期==================
		// String pattern = "yyyy-MM-dd'T'HH:mm:ss:SSSZZ";
		String pattern = "yyyy-MM-dd'T'HH:mm:ss";// ISO8601日期格式
		Date now = new Date();
		String nowStr = DateFormatUtils.format(now, pattern);
		Date after10Sec = new Date(now.getTime() + 10 * 1000);// 10秒后
		String after10SecStr = DateFormatUtils.format(after10Sec, pattern);
		System.out.println(nowStr);
		System.out.println(after10SecStr);
		// ===============
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("notify", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " " + processInstance.getProcessDefinitionId());

		String procInstanceId = processInstance.getId();
		// String procDefinitionId = processInstance.getProcessDefinitionId();

		// 第1个任务-派工-Task1Listener自动执行
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstanceId).list();
		for (Task task : tasks) {
			System.out.println("Task: -name=" + task.getName());
			System.out.println("      -assignee=" + task.getAssignee());
			taskService.complete(task.getId(), variableMap);
		}
		
		// 第2个任务-接单预约-不完成该任务
		tasks = taskService.createTaskQuery().processInstanceId(procInstanceId).list();
		for (Task task : tasks) {
			System.out.println("Task: -name=" + task.getName());
			System.out.println("      -assignee=" + task.getAssignee());
			//taskService.complete(task.getId(), variableMap);
		}

		System.out.println("before sleep...");
		System.out.println(Thread.currentThread().getId());
		System.out.println(Thread.currentThread().getThreadGroup().getName());
		for(int i=1; i<130; i++){
			Thread.sleep(1000);//等待超时
			System.out.println("sleeping "+i+"s");
		}
		System.out.println("after sleep...");

		// 第二个任务超时-自动拒单&timer终止流程-无任何任务
		tasks = taskService.createTaskQuery().processInstanceId(procInstanceId).list();
		for (Task task : tasks) {
			System.out.println("Task: -name=" + task.getName());
			System.out.println(" -assignee=" + task.getAssignee());
			// taskService.complete(task.getId(), variableMap);
		}
	}
}