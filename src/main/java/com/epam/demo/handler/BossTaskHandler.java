package com.epam.demo.handler;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino_Tang</a>
 * @version 1.0.0-4/7/2022
 */
public class BossTaskHandler implements TaskListener {

  @Override
  public void notify(DelegateTask delegateTask) {
    delegateTask.setAssignee("Boss");
  }
}
