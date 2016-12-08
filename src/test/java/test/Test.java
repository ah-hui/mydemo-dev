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
import org.activiti.engine.task.Task;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.hand.demo.dto.User;
import com.hand.demo.service.ISysUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(locations = { "classpath:spring/test-applicationContext.xml" })
public class Test {

	@Autowired
	private ISysUserService userService;

	@org.junit.Test
	public void test() {
		System.out.println("1111111111");
	}

//	@org.junit.Test
//	@WithMockUser(username = "lgh", authorities = { "ADMIN", "USER" })
//	public void testGetUser() {
//		List<User> list = userService.selectUsers();
//		for (User user : list) {
//			System.out.println(user.getLoginName());
//		}
//	}

	@org.junit.Test
	public void monthtest() {
		
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

		RuntimeService runtimeService = processEngine.getRuntimeService();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		TaskService taskService = processEngine.getTaskService();
		ManagementService managementService = processEngine.getManagementService();
		IdentityService identityService = processEngine.getIdentityService();
		HistoryService historyService = processEngine.getHistoryService();
		FormService formService = processEngine.getFormService();
		
		// 部署流程定义
		repositoryService.createDeployment().addClasspathResource("bpmn20/first.bpmn20.xml").deploy();
		// 启动流程实例
		String procId = runtimeService.startProcessInstanceByKey("financialReport").getId();
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

}
