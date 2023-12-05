package com.teamviewer.collabmates;

public class Task {
    private String title;
    private String task;
    private String budget;
    private String deadline;
    private String name;

    // Required empty constructor for Firebase
    public Task() {
    }

    public Task(String title, String description, String budget, String deadline, String name) {
        this.title = title;
        this.task = task;
        this.budget = budget;
        this.deadline = deadline;
        this.name = name;
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

    public void setDescription(String task) {
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
}
