package com.app.daymoya.global.config;

import com.app.daymoya.global.properties.FileProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final FileProperties fileProperties;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    Path uploadPath = Paths.get(fileProperties.getUploadPath()).toAbsolutePath().normalize();

    registry.addResourceHandler(fileProperties.getAccessUrl() + "/**")
      .addResourceLocations("file:" + uploadPath + "/");
  }

}
