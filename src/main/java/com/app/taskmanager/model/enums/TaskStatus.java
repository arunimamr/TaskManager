package com.app.taskmanager.model.enums;


import lombok.Getter;

@Getter
public enum TaskStatus {
    TO_DO("TODO"),
    IN_PROGRESS("IN PROGRESS"),
    DONE("DONE");

    private final String display;

    TaskStatus(String display){
        this.display = display;
    }

}