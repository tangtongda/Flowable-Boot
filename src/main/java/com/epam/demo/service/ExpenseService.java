package com.epam.demo.service;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino_Tang</a>
 * @version 1.0.0-4/6/2022
 */
public class ExpenseService {

  @Autowired private RuntimeService runtimeService;

  @Autowired private TaskService taskService;

  @Transactional
  public void startProcess() {
    runtimeService.startProcessInstanceByKey("oneTaskProcess");
  }

  @Transactional
  public List<Task> getTasks(String assignee) {
    return taskService.createTaskQuery().taskAssignee(assignee).list();
  }
}
