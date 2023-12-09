package com.teamviewer.collabmates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyPostsViewHolder> {

    private Context context;
    private List<Task> taskList;
    private SharedViewModel sharedViewModel;

    public TasksAdapter(Context context, List<Task> taskList, SharedViewModel sharedViewModel) {
        this.context = context;
        this.taskList = taskList;
        this.sharedViewModel = sharedViewModel;
    }

    @NonNull
    @Override
    public MyPostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.common_feed_item, parent, false);
        return new MyPostsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostsViewHolder holder, int position) {
        Task task = taskList.get(position);

        // Bind data to views in the ViewHolder
        holder.textViewName.setText(task.getName());
        holder.textViewTask.setText(task.getTask());
        holder.textViewBudget.setText("Php " + task.getBudget());
        holder.textViewDeadline.setText("Deadline: " + task.getDeadline());

        // Handle item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the selected task in the shared ViewModel
                sharedViewModel.selectTask(task);
            }
        });

        // Handle button click (if needed)
        holder.buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle apply button click
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public class MyPostsViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewTask;
        TextView textViewBudget;
        TextView textViewDeadline;
        Button buttonApply;

        public MyPostsViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views in the ViewHolder
            textViewName = itemView.findViewById(R.id.nameView);
            textViewTask = itemView.findViewById(R.id.taskView);
            textViewBudget = itemView.findViewById(R.id.budgetView);
            textViewDeadline = itemView.findViewById(R.id.deadlineView);
            buttonApply = itemView.findViewById(R.id.buttonApply);
        }
    }
}
