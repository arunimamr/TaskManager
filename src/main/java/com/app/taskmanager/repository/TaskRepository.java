package com.app.taskmanager.repository;

import com.app.taskmanager.model.entity.Task;
import com.app.taskmanager.model.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findAllByStatus(TaskStatus taskStatus, Pageable pageable);
}
