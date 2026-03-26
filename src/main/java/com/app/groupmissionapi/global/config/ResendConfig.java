package com.app.groupmissionapi.global.config;

import com.app.groupmissionapi.global.properties.ResendProperties;
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
