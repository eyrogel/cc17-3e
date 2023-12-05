package com.teamviewer.collabmates;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;

public class CreateViewModel extends ViewModel {
    private MutableLiveData<Task> taskLiveData = new MutableLiveData<>();

    public void setTask(Task task) {
        taskLiveData.setValue(task);
    }

    public MutableLiveData<Task> getTaskLiveData() {
        return taskLiveData;
    }
}

