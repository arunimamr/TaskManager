package com.app.taskmanager.controller;

import com.app.taskmanager.model.dtos.TaskDto;
import com.app.taskmanager.service.TaskService;
import com.app.taskmanager.utils.TestTaskUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;


    @Test
    void testCreateTask() throws Exception {
        TaskDto taskDto = TestTaskUtil.getTaskDto();
        MvcResult result;

        when(taskService.createTask(any())).thenReturn(taskDto);
        result = mockMvc.perform(post("/tasks/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                        .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("JUnit task"))
                .andReturn();
    }

    @Test
    void testGetTaskById() throws Exception {
        TaskDto taskDto = TestTaskUtil.getTaskDto();
        Long taskId = taskDto.getId();

        when(taskService.getTaskDetails(taskId)).thenReturn(taskDto);
        mockMvc.perform(get("/tasks/" + taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId));
    }

    @Test
    void testUpdateTask() throws Exception {
        TaskDto updatedDto = TestTaskUtil.getTaskDto();
        Long taskId = updatedDto.getId();
        updatedDto.setDescription("Update JUnit tests");

        when(taskService.updateTask(any(), any())).thenReturn(updatedDto);
        mockMvc.perform(put("/tasks/"+ taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Update JUnit tests"));
    }

    @Test
    void testDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(1L);
        mockMvc.perform(delete("/tasks/" + 1)).andExpect(status().isNoContent());
    }

    @Test
    void testGetAllTasksWithPaginationAndFilter() throws Exception {
        TaskDto taskDto = TestTaskUtil.getTaskDto();
        Page<TaskDto> mockPage = new PageImpl<>(List.of(taskDto));
        when(taskService.getAllTasks(any(), anyInt(),anyInt())).thenReturn(mockPage);
        mockMvc.perform(get("/tasks/all")
                .param("status","TODO")
                .param("pageNum", "0")
                .param("pageSize", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(taskDto.getId()));
    }

}
