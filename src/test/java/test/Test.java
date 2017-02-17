package test;

import java.util.List;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.javax.el.ExpressionFactory;
import org.activiti.engine.impl.javax.el.ValueExpression;
import org.activiti.engine.impl.juel.ExpressionFactoryImpl;
import org.activiti.engine.impl.juel.SimpleContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.hand.demo.service.ISysUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(locations = { "classpath:spring/test-applicationContext.xml" })
public class Test {

	@Autowired
	private ISysUserService userService;

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	RuntimeService runtimeService = processEngine.getRuntimeService();
	RepositoryService repositoryService = processEngine.getRepositoryService();
	TaskService taskService = processEngine.getTaskService();
	ManagementService managementService = processEngine.getManagementService();
	IdentityService identityService = processEngine.getIdentityService();
	HistoryService historyService = processEngine.getHistoryService();
	FormService formService = processEngine.getFormService();

	@org.junit.Test
	public void test() {
		System.out.println("1111111111");
	}

	// @org.junit.Test
	// @WithMockUser(username = "lgh", authorities = { "ADMIN", "USER" })
	// public void testGetUser() {
	// List<User> list = userService.selectUsers();
	// for (User user : list) {
	// System.out.println(user.getLoginName());
	// }
	// }

	@org.junit.Test
	public void monthtest() {

		// ProcessEngine processEngine =
		// ProcessEngines.getDefaultProcessEngine();
		//
		// RuntimeService runtimeService = processEngine.getRuntimeService();
		// RepositoryService repositoryService =
		// processEngine.getRepositoryService();
		// TaskService taskService = processEngine.getTaskService();
		// ManagementService managementService =
		// processEngine.getManagementService();
		// IdentityService identityService = processEngine.getIdentityService();
		// HistoryService historyService = processEngine.getHistoryService();
		// FormService formService = processEngine.getFormService();

		// 部署流程定义
		repositoryService.createDeployment().addClasspathResource("bpmn20/first.bpmn20.xml").deploy();
		// 启动流程实例
		String procId = runtimeService.startProcessInstanceByKey("financialReport").getId();
		
		TaskDefinition curTaskDef1 = findNextTaskByProcInstanceId(procId);
		
		// 获得第一个任务
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("sales").list();
		for (Task task : tasks) {
			System.out.println("Following task is available for sales group: " + task.getName());
			// 认领任务这里由foozie认领，因为fozzie是sales组的成员
			taskService.claim(task.getId(), "fozzie");
		}
		// 查看fozzie现在是否能够获取到该任务
		tasks = taskService.createTaskQuery().taskAssignee("fozzie").list();
		for (Task task : tasks) {
			System.out.println("Task for fozzie: " + task.getName());
			// 执行(完成)任务
			taskService.complete(task.getId());
		}
		// 现在fozzie的可执行任务数就为0了
		System.out
				.println("Number of tasks for fozzie: " + taskService.createTaskQuery().taskAssignee("fozzie").count());
		// 获得第二个任务
		TaskDefinition curTaskDef = findNextTaskByProcInstanceId(procId);
		
		tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
		for (Task task : tasks) {
			System.out.println("Following task is available for accountancy group:" + task.getName());
			// 认领任务这里由kermit认领，因为kermit是management组的成员
			taskService.claim(task.getId(), "kermit");
		}
		// 完成第二个任务结束流程
		for (Task task : tasks) {
			taskService.complete(task.getId());
		}
		// 核实流程是否结束,输出流程结束时间
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(procId).singleResult();
		System.out.println("Process instance end time: " + historicProcessInstance.getEndTime());
	}

	// 根据流程ID获取当前任务
	private TaskDefinition findNextTaskByProcInstanceId(String procId) {
		// 1.根据流程ID获取所有任务
		List<Task> allTask = taskService.createTaskQuery().processInstanceId(procId).list();
		// 当前流程节点Id信息
		ExecutionEntity execution = (ExecutionEntity) runtimeService.createProcessInstanceQuery()
				.processInstanceId(procId).singleResult();
		String activitiId = execution.getActivityId();
		// 获取流程所有节点信息
		String definitionId = runtimeService.createProcessInstanceQuery().processInstanceId(procId).singleResult()
				.getProcessDefinitionId();
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(definitionId);
		List<ActivityImpl> activitiList = processDefinitionEntity.getActivities();
		// 遍历所有节点信息
		for (ActivityImpl activityImpl : activitiList) {
			String id = activityImpl.getId();
			// 找到当前节点信息
			if (activitiId.equals(id)) {
				// 获取下一个节点信息
				TaskDefinition task = nextTaskDefinition(activityImpl, activityImpl.getId(), null, procId);
				return task;
			}
		}
		return null;
	}

	/**
	 * 下一个任务节点信息,
	 * 
	 * 如果下一个节点为用户任务则直接返回,
	 * 
	 * 如果下一个节点为排他网关, 获取排他网关Id信息, 根据排他网关Id信息和execution获取流程实例排他网关Id为key的变量值,
	 * 根据变量值分别执行排他网关后线路中的el表达式, 并找到el表达式通过的线路后的用户任务信息
	 * 
	 * @param ActivityImpl
	 *            activityImpl 流程节点信息
	 * @param String
	 *            activityId 当前流程节点Id信息
	 * @param String
	 *            elString 排他网关顺序流线段判断条件, 例如排他网关顺序留线段判断条件为${money>1000},
	 *            若满足流程启动时设置variables中的money>1000, 则流程流向该顺序流信息
	 * @param String
	 *            processInstanceId 流程实例Id信息
	 * @return
	 */
	private TaskDefinition nextTaskDefinition(ActivityImpl activityImpl, String activityId, String elString,
			String processInstanceId) {
		PvmActivity ac = null;
		Object s = null;
		// 如果遍历节点为用户任务并且节点不是当前节点信息
		if ("userTask".equals(activityImpl.getProperty("type")) && !activityId.equals(activityImpl.getId())) {
			// 获取该节点下一个节点信息
			TaskDefinition taskDefinition = ((UserTaskActivityBehavior) activityImpl.getActivityBehavior())
					.getTaskDefinition();
			return taskDefinition;
		} else {
			// 获取节点所有流向线路信息
			List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
			List<PvmTransition> outTransitionsTemp = null;
			for (PvmTransition tr : outTransitions) {
				ac = tr.getDestination(); // 获取线路的终点节点
				// 如果流向线路为排他网关
				if ("exclusiveGateway".equals(ac.getProperty("type"))) {
					outTransitionsTemp = ac.getOutgoingTransitions();
					// 如果网关路线判断条件为空信息
//					if (StrUtils.isEmpty(elString)) {
					if ("".equals(elString)) {
						// 获取流程启动时设置的网关判断条件信息
						elString = getGatewayCondition(ac.getId(), processInstanceId);
					}
					// 如果排他网关只有一条线路信息
					if (outTransitionsTemp.size() == 1) {
						return nextTaskDefinition((ActivityImpl) outTransitionsTemp.get(0).getDestination(), activityId,
								elString, processInstanceId);
					} else if (outTransitionsTemp.size() > 1) { // 如果排他网关有多条线路信息
						for (PvmTransition tr1 : outTransitionsTemp) {
							s = tr1.getProperty("conditionText"); // 获取排他网关线路判断条件信息
							// 判断el表达式是否成立
//							if (isCondition(ac.getId(), StrUtils.trim(s.toString()), elString)) {
							if (isCondition(ac.getId(), s.toString(), elString)) {
								return nextTaskDefinition((ActivityImpl) tr1.getDestination(), activityId, elString,
										processInstanceId);
							}
						}
					}
				} else if ("userTask".equals(ac.getProperty("type"))) {
					return ((UserTaskActivityBehavior) ((ActivityImpl) ac).getActivityBehavior()).getTaskDefinition();
				} else {
				}
			}
			return null;
		}
	}

	/**
	 * 查询流程启动时设置排他网关判断条件信息
	 * 
	 * @param String
	 *            gatewayId 排他网关Id信息, 流程启动时设置网关路线判断条件key为网关Id信息
	 * @param String
	 *            processInstanceId 流程实例Id信息
	 * @return
	 */
	public String getGatewayCondition(String gatewayId, String processInstanceId) {
		Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();
		return runtimeService.getVariable(execution.getId(), gatewayId).toString();
	}

	/**
	 * 根据key和value判断el表达式是否通过信息
	 * 
	 * @param String
	 *            key el表达式key信息
	 * @param String
	 *            el el表达式信息
	 * @param String
	 *            value el表达式传入值信息
	 * @return
	 */
	public boolean isCondition(String key, String el, String value) {
		ExpressionFactory factory = new ExpressionFactoryImpl();
		SimpleContext context = new SimpleContext();
		context.setVariable(key, factory.createValueExpression(value, String.class));
		ValueExpression e = factory.createValueExpression(context, el, boolean.class);
		return (Boolean) e.getValue(context);
	}

}
