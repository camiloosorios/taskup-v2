package com.bosorio.taskupv2.repositories;

import com.bosorio.taskupv2.entites.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
