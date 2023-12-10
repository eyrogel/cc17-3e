package com.teamviewer.collabmates;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
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

        // Handle button click
        holder.buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TasksAdapter", "Apply button clicked for task: " + task.getName());

                // Show the Apply dialog
                showApplyDialog(context, task);
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

    public void clearAdapter() {
        taskList.clear();
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

            // Set click listener for the apply button
            buttonApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Directly handle the button click here
                    showApplyDialog(context, taskList.get(getAdapterPosition()));
                }
            });
        }
    }

    // Show the Apply dialog directly within the adapter
    public void showApplyDialog(Context context, Task task) {
        try {
            // Inflate the custom dialog layout
            View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog_apply, null);

            // Create and show the Apply dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(dialogView);

            AlertDialog alertDialog = builder.create();

            // Initialize views in the Apply dialog
            Button okayButton = dialogView.findViewById(R.id.btn_yesimsure);
            Button cancelButton = dialogView.findViewById(R.id.btn_cancel);

            okayButton.setOnClickListener(v -> {
                // Remove the task from the list
                int position = taskList.indexOf(task);
                if (position != -1) {
                    taskList.remove(position);
                    notifyItemRemoved(position);
                }

                alertDialog.dismiss();
            });

            cancelButton.setOnClickListener(v -> {
                alertDialog.dismiss();
            });

            alertDialog.show();
        } catch (Exception e) {
            Log.e("TasksAdapter", "Error showing Apply dialog: " + e.getMessage());
        }
    }
}
