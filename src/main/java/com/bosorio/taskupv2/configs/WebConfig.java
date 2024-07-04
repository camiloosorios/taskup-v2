package com.bosorio.taskupv2.configs;

import com.bosorio.taskupv2.interceptors.ProjectExistInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ProjectExistInterceptor projectExistInterceptor;

    @Autowired
    public WebConfig(ProjectExistInterceptor projectExistInterceptor) {
        this.projectExistInterceptor = projectExistInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(projectExistInterceptor).addPathPatterns("/api/projects/{projectId}/tasks/**");
    }
}
