package com.app.taskmanager.controller;

import com.app.taskmanager.model.dtos.TaskDto;
import com.app.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/save")
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody TaskDto taskDto){
        TaskDto createdTask = taskService.createTask(taskDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskDetails(@PathVariable Long id){
        TaskDto taskInfo = taskService.getTaskDetails(id);
        return ResponseEntity.status(HttpStatus.OK).body(taskInfo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTaskDetails( @Valid @RequestBody TaskDto taskDto, @PathVariable Long id){
        TaskDto updatedTask = taskService.updateTask(taskDto, id);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/all")
    public ResponseEntity<Page<TaskDto>> getAllTasks(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "0") int pageNum
    ){
        return ResponseEntity.ok(taskService.getAllTasks(status, pageNum, pageSize));
    }

}
