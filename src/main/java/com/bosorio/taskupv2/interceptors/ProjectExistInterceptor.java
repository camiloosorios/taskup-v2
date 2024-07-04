package com.bosorio.taskupv2.interceptors;

import com.bosorio.taskupv2.DTOs.ProjectDTO;
import com.bosorio.taskupv2.Exceptions.NotFoundException;
import com.bosorio.taskupv2.services.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProjectExistInterceptor implements HandlerInterceptor {

    private final ProjectService projectService;

    @Autowired
    public ProjectExistInterceptor(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String projectId = request.getRequestURI().split("/")[3];
            ProjectDTO projectDTO = projectService.getProjectById(Long.parseLong(projectId));
            request.setAttribute("projectDTO", projectDTO);

            return true;
        } catch (Exception e) {
            response.setContentType("application/json");
            Map<String, String> error = new HashMap<>();
            if (e instanceof NotFoundException) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                error.put("error", e.getMessage());
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                error.put("error", "Invalid Action");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String errorMessage = objectMapper.writeValueAsString(error);
            PrintWriter writer = response.getWriter();
            writer.write(errorMessage);
            writer.flush();

            return false;
        }
    }
}
