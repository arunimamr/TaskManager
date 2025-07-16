package com.app.taskmanager.service;

import com.app.taskmanager.exception.ResourceNotFoundException;
import com.app.taskmanager.model.dtos.TaskDto;
import com.app.taskmanager.model.entity.Task;
import com.app.taskmanager.model.enums.TaskStatus;
import com.app.taskmanager.repository.TaskRepository;
import com.app.taskmanager.utils.TestTaskUtil;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Spy
    private ModelMapper modelMapper;

    @Setter
    private Task task;

    @Setter
    private TaskDto taskDto;

    @BeforeEach
    void setup(){
        task = TestTaskUtil.getTask();
        taskDto = TestTaskUtil.getTaskDto();
    }

    @Test
    void testCreateTaskSuccess(){
        when(modelMapper.map(taskDto,Task.class)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);

        TaskDto response = taskService.createTask(taskDto);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1001L);

        verify(taskRepository).save(task);
    }

    @Test
    void testGetTaskByIdSuccess(){
        when(taskRepository.findById(any())).thenReturn(Optional.ofNullable(task));
        when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);

        TaskDto response = taskService.getTaskDetails(1001L);
        assertThat(response.getId()).isEqualTo(1001L);

        verify(taskRepository).findById(1001L);
    }

    @Test
    void testGetTaskByIdNotFound(){
        when(taskRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                taskService.getTaskDetails(1002L));
    }

    @Test
    void testUpdateTask(){
        LocalDate taskDate = LocalDate.now().plusDays(1);
        TaskDto updatedTaskDto = new TaskDto(1001L, "New Junit update",
                "New updates for Junit task", TaskStatus.IN_PROGRESS, taskDate);

        Task updatedTask = new Task(1001L, "New Junit update",
                "New updates for Junit task", TaskStatus.IN_PROGRESS, taskDate);

        when(taskRepository.findById(1001L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any())).thenReturn(updatedTask);
        when(modelMapper.map(updatedTask, TaskDto.class)).thenReturn(updatedTaskDto);

        TaskDto response = taskService.updateTask(updatedTaskDto, 1001L);
        assertThat(response.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void testDeleteTask(){
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);
        taskService.deleteTask(1L);
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void testDeleteTaskNotFound(){
        when(taskRepository.existsById(any())).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, ()-> taskService.deleteTask(1L));
    }

    @Test
    void testGetAllTasksWithPaginationAndFilter(){
        List<Task> taskList = List.of(task);
        Page<Task> taskPage = new PageImpl<>(taskList);

        List<TaskDto> taskDtoList = taskService.maptoDtoList(taskList);
        when(taskRepository.findAllByStatus(eq(TaskStatus.TO_DO), any(Pageable.class))).thenReturn(taskPage);

        Page<TaskDto> response = taskService.getAllTasks(String.valueOf(TaskStatus.TO_DO), 0,5);
        assertThat(taskDtoList).hasSize(1);
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().getFirst().getStatus()).isEqualTo(TaskStatus.TO_DO);
    }

    @Test
    void testGetAllTasksWhenStatusFilterNotProvided(){
        List<Task> taskList = List.of(task);
        Page<Task> taskPage = new PageImpl<>(taskList);

        List<TaskDto> taskDtoList = taskService.maptoDtoList(taskList);
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(taskPage);

        Page<TaskDto> response = taskService.getAllTasks(String.valueOf(""), 0,5);
        assertThat(taskDtoList).hasSize(1);
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().getFirst().getStatus()).isEqualTo(TaskStatus.TO_DO);
    }

}
