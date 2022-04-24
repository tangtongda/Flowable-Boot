package com.epam.demo.config;

import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino_Tang</a>
 * @version 1.0.0-4/7/2022
 */
@Configuration
public class FlowableConfig
    implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {

  @Override
  public void configure(SpringProcessEngineConfiguration engineConfiguration) {
    // set the font
    engineConfiguration.setActivityFontName("Arial");
    engineConfiguration.setLabelFontName("Monospaced");
    engineConfiguration.setAnnotationFontName("Roboto");
  }
}
