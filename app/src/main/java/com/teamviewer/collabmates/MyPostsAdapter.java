package com.teamviewer.collabmates;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyPostsAdapter extends RecyclerView.Adapter<MyPostsAdapter.TaskViewHolder> {

    private List<Task> tasks;

    // Constructor to initialize the tasks list
    public MyPostsAdapter(List<Task> tasksList) {
        this.tasks = tasks;
    }

    // Setter method to update the tasks list
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_posts_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        // Bind data to views in the ViewHolder
        Task task = tasks.get(position);
        holder.nameView.setText(task.getName());
        holder.taskView.setText(task.getTask());
        holder.budgetView.setText("Php " + task.getBudget());
        holder.deadlineView.setText("Deadline: " + task.getDeadline());
    }

    @Override
    public int getItemCount() {
        return tasks != null ? tasks.size() : 0;
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        TextView taskView;
        TextView budgetView;
        TextView deadlineView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.nameView);
            taskView = itemView.findViewById(R.id.taskView);
            budgetView = itemView.findViewById(R.id.budgetView);
            deadlineView = itemView.findViewById(R.id.deadlineView);
        }
    }
}
