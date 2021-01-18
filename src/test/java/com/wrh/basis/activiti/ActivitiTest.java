package com.wrh.basis.activiti;


import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * Activiti测试
 * @author Simple
 * @date 2021/1/18 9:32
 * @change: 2021/1/18 9:32 by wangruoheng@bonc.com.cn for init
 */
@SpringBootTest
public class ActivitiTest{

    private static Logger log = LogManager.getLogger(ActivitiTest.class);

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;


    /**
     * 部署流程
     */
    @Test
    public void Test1() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/firstProcess.bpmn") // 添加bpmn资源
                .addClasspathResource("processes/firstProcess.png")  // 添加png资源
                .name("请假申请流程")
                .disableSchemaValidation()
                .deploy();
        // 1、输出部署信息
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
    }

    /**
     * 启动流程
     */
    @Test
    public void Test2() {
        // ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess_1");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById("myProcess_1:1:7504");
        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
        System.out.println("当前活动Id：" + processInstance.getActivityId());
    }

    /**
     * 查询当前个人待执行的任务
     */
    @Test
    public void Test3() {
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("myProcess_1") //流程Key
                .taskAssignee("boss")//只查询该任务负责人的任务
                .list();

        for (Task task : taskList) {
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    /**
     * 完成任务
     */
    @Test
    public void Test4() {
        Task task1 = taskService.createTaskQuery().processInstanceId("10001").singleResult();
        // 提交任务
        log.info("流程实例id：{}",task1.getProcessInstanceId());
        log.info("任务id：{}",task1.getId());
        log.info("任务负责人：{}",task1.getAssignee());
        log.info("任务名称：{}",task1.getName());
        // taskService.complete(task1.getId());
        // List<Task> simple = taskService.createTaskQuery().taskAssignee("boss").list();
        // for (Task task : simple) {
        //     System.out.println("流程实例id：" + task.getProcessInstanceId());
        //     System.out.println("任务id：" + task.getId());
        //     System.out.println("任务负责人：" + task.getAssignee());
        //     System.out.println("任务名称：" + task.getName());
        // }
    }

    /**
     * 查询所有流程定义
     */
    @Test
    public void Test5() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition processDefinition : list) {
            System.out.println("流程定义Id :" + processDefinition.getId());
            System.out.println("流程定义 name="+processDefinition.getName());
            System.out.println("流程定义 key="+processDefinition.getKey());
            System.out.println("流程定义 Version="+processDefinition.getVersion());
            System.out.println("流程部署ID ="+processDefinition.getDeploymentId());
        }
    }

    /**
     * 查询所有流程实例
     */
    @Test
    public void Test6() {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().list();
        for (HistoricProcessInstance historicProcessInstance : list) {
            log.info("流程实例Id：{}",historicProcessInstance.getId());
            log.info("流程实例的流程定义名称：{}",historicProcessInstance.getProcessDefinitionName());
            log.info("流程实例开始时间：{}",historicProcessInstance.getStartTime());
            log.info("流程实例结束时间：{}",historicProcessInstance.getEndTime());
        }
    }

}
