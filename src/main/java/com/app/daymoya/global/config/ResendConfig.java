package com.app.daymoya.global.config;

import com.app.daymoya.global.properties.ResendProperties;
import com.resend.Resend;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResendConfig {

  @Bean
  public Resend resend(ResendProperties resendProperties){
    return new Resend(resendProperties.getApiKey());
  }

}
