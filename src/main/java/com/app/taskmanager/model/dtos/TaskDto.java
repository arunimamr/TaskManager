package com.app.taskmanager.model.dtos;

import com.app.taskmanager.model.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaskDto {
    private Long id;

    @NotEmpty(message = "Title is required")
    private String title;

    private String description;

    private TaskStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Due date must be provided")
    @FutureOrPresent(message = "Due date must be Present date or Future Date")
    private LocalDate dueDate;

}