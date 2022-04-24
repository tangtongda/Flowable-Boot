package com.epam.demo.controller;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino_Tang</a>
 * @version 1.0.0-4/7/2022
 */
@RestController
@RequestMapping("/demo")
public class ExpenseController {

  @Autowired private RuntimeService runtimeService;
  @Autowired private TaskService taskService;
  @Autowired private RepositoryService repositoryService;
  @Autowired private ProcessEngine processEngine;

  /**
   * Expense Request
   *
   * @param userId user id
   * @param money expense money
   * @param description description of use
   */
  @GetMapping("/add")
  public String addExpense(
      @RequestParam String userId, @RequestParam Integer money, @RequestParam String description) {
    // 启动流程
    HashMap<String, Object> map = new HashMap<>();
    map.put("taskUser", userId);
    map.put("money", money);
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Expense", map);
    return "：" + processInstance.getId();
  }

  /**
   * Get apply list
   *
   * @param userId user id
   * @return apply list
   */
  @GetMapping("/list")
  public Object list(@RequestParam String userId) {
    List<Task> tasks =
        taskService.createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().desc().list();
    for (Task task : tasks) {
      System.out.println(task.toString());
    }
    return Arrays.toString(tasks.toArray());
  }

  /**
   * Apply the request
   *
   * @param taskId task id
   */
  @GetMapping("/apply")
  public String apply(@RequestParam String taskId) {
    Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
    if (task == null) {
      throw new RuntimeException("Process is not exist.");
    }
    // process approved
    HashMap<String, Object> map = new HashMap<>();
    map.put("outcome", "approved");
    taskService.complete(taskId, map);
    return "processed ok!";
  }

  /**
   * Reject the process
   *
   * @param taskId task id
   * @return result
   */
  @GetMapping("/reject")
  public String reject(String taskId) {
    HashMap<String, Object> map = new HashMap<>();
    map.put("outcome", "rejected");
    taskService.complete(taskId, map);
    return "reject";
  }

  /**
   * Generate Diagram
   *
   * @param processId process id
   */
  @GetMapping("/diagram")
  public void genProcessDiagram(HttpServletResponse httpServletResponse, String processId) {
    ProcessInstance pi =
        runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();

    // Process in progress don't show diagram
    if (pi == null) {
      return;
    }
    Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
    // Query Execution list by instanceId
    String instanceId = task.getProcessInstanceId();
    List<Execution> executions =
        runtimeService.createExecutionQuery().processInstanceId(instanceId).list();

    // 得到正在执行的Activity的Id
    List<String> activityIds = new ArrayList<>();
    List<String> flows = new ArrayList<>();
    for (Execution exe : executions) {
      List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
      activityIds.addAll(ids);
    }

    // Get the diagram
    BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
    ProcessEngineConfiguration engineConfiguration = processEngine.getProcessEngineConfiguration();
    ProcessDiagramGenerator diagramGenerator = engineConfiguration.getProcessDiagramGenerator();

    byte[] buf = new byte[1024];
    int length = 0;
    try (InputStream in =
            diagramGenerator.generateDiagram(
                bpmnModel,
                "png",
                activityIds,
                flows,
                engineConfiguration.getActivityFontName(),
                engineConfiguration.getLabelFontName(),
                engineConfiguration.getAnnotationFontName(),
                engineConfiguration.getClassLoader(),
                1.0,
                true);
        OutputStream out = httpServletResponse.getOutputStream()) {
      while ((length = in.read(buf)) != -1) {
        out.write(buf, 0, length);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
