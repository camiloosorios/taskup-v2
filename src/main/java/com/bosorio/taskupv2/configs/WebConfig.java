package com.bosorio.taskupv2.configs;

import com.bosorio.taskupv2.interceptors.ProjectExistInterceptor;
import com.bosorio.taskupv2.interceptors.TaskExistInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.origin}")
    private String corsOrigin;

    private final ProjectExistInterceptor projectExistInterceptor;

    private final TaskExistInterceptor taskExistInterceptor;

    @Autowired
    public WebConfig(ProjectExistInterceptor projectExistInterceptor, TaskExistInterceptor taskExistInterceptor) {
        this.projectExistInterceptor = projectExistInterceptor;
        this.taskExistInterceptor = taskExistInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(projectExistInterceptor).addPathPatterns("/api/projects/{projectId}/tasks/**");
        registry.addInterceptor(taskExistInterceptor).addPathPatterns("/api/projects/{projectId}/tasks/{taskId}/notes/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(corsOrigin)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);
    }
}
