package com.app.taskmanager.service;

import com.app.taskmanager.exception.ResourceNotFoundException;
import com.app.taskmanager.model.dtos.TaskDto;
import com.app.taskmanager.model.entity.Task;
import com.app.taskmanager.model.enums.TaskStatus;
import com.app.taskmanager.repository.TaskRepository;
import liquibase.util.ObjectUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ModelMapper modelMapper;

    public TaskDto createTask(TaskDto taskDto){
        Task task = modelMapper.map(taskDto, Task.class);
        task.setStatus(TaskStatus.TO_DO);
        Task savedTask = taskRepository.save(task);
        return modelMapper.map(savedTask, TaskDto.class);
    }

    public TaskDto getTaskDetails(Long id) {
        Task taskInfo = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found : " + id));
        return modelMapper.map(taskInfo, TaskDto.class);
    }

    public TaskDto updateTask(TaskDto taskDto, Long id) {
        Task currentTask = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("could not update. Task not found" + id));
        currentTask.setTitle(taskDto.getTitle());
        currentTask.setDescription(taskDto.getDescription());
        currentTask.setStatus(taskDto.getStatus());
        currentTask.setDueDate(taskDto.getDueDate());
        taskRepository.save(currentTask);
        return modelMapper.map(currentTask, TaskDto.class);
    }

    public void deleteTask(Long id) {
        if(!taskRepository.existsById(id))
            throw new ResourceNotFoundException("Task with id " + id + " not found");
        taskRepository.deleteById(id);
    }

    public Page<TaskDto> getAllTasks(String status, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("dueDate").ascending());
        Page<Task> taskPageResult;
        if(!ObjectUtils.isEmpty(status)){
            TaskStatus taskStatus;
            try{
                taskStatus = TaskStatus.valueOf(status.replace(" ", "_").toUpperCase());
            }
            catch (IllegalArgumentException ex){
                throw new IllegalArgumentException("Invalid task status value : " + status);
            }
            taskPageResult = taskRepository.findAllByStatus(taskStatus,pageable);
        }
        else{
            taskPageResult = taskRepository.findAll(pageable);
        }
        List<TaskDto> taskDtoList = maptoDtoList(taskPageResult.getContent());
        return new PageImpl<>(taskDtoList, pageable, taskPageResult.getTotalElements());
    }

    public List<TaskDto> maptoDtoList(List<Task> tasks){
        return tasks.stream().map(task -> modelMapper.map(task, TaskDto.class)).toList();
    }
}
