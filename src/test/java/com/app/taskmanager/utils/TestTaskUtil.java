package com.app.taskmanager.utils;

import com.app.taskmanager.model.dtos.TaskDto;
import com.app.taskmanager.model.entity.Task;
import com.app.taskmanager.model.enums.TaskStatus;

import java.time.LocalDate;

public class TestTaskUtil {
    public static TaskDto getTaskDto(){
        TaskDto dto = new TaskDto();
        dto.setId(1001L);
        dto.setTitle("JUnit task");
        dto.setDescription("Implement junit for the task");
        dto.setDueDate(LocalDate.now().plusDays(1));
        dto.setStatus(TaskStatus.TO_DO);
        return dto;
    }

    public static Task getTask(){
        Task task = new Task();
        task.setId(1001L);
        task.setTitle("JUnit task");
        task.setDescription("Implement junit for the task");
        task.setDueDate(LocalDate.now().plusDays(1));
        task.setStatus(TaskStatus.TO_DO);
        return task;
    }
}
