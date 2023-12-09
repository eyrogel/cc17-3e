package com.teamviewer.collabmates;

public class Task {
    private String title;
    private String task;
    private String budget;
    private String deadline;
    private String name;
    private String userId;  // New field for the user ID

    // Required empty constructor for Firebase
    public Task() {
    }

    public Task(String title, String task, String budget, String deadline, String name, String userId) {
        this.title = title;
        this.task = task;
        this.budget = budget;
        this.deadline = deadline;
        this.name = name;
        this.userId = userId;
    }

    // Getters and setters (required for Firebase)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
