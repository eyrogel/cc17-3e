package com.teamviewer.collabmates;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeedFragment extends Fragment {

    private Button postsButton;
    private Button tasksButton;
    private SharedViewModel sharedViewModel;

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private FirebaseFirestore db;

    public FeedFragment() {
        // Required empty public constructor
    }

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        // Initialize buttons
        postsButton = view.findViewById(R.id.postsButton);
        tasksButton = view.findViewById(R.id.tasksButton);

        // Set click listeners for the buttons
        postsButton.setOnClickListener(v -> {
            postsButton.setBackgroundResource(R.drawable.rectangle_pressed);
            tasksButton.setBackgroundResource(R.drawable.rectangle_normal);
        });

        tasksButton.setOnClickListener(v -> {
            tasksButton.setBackgroundResource(R.drawable.rectangle_pressed);
            postsButton.setBackgroundResource(R.drawable.rectangle_normal);
        });

        // Simulate a click on the "Offered Task" button to set it as default
        postsButton.performClick();

        // Initialize shared ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize an empty task list for now
        List<Task> tasksList = new ArrayList<>();
        taskAdapter = new TaskAdapter(tasksList);
        recyclerView.setAdapter(taskAdapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Fetch all tasks from Firestore
        fetchAllTasksFromFirestore();

        return view;
    }

    private void fetchAllTasksFromFirestore() {
        // Assuming you have a "tasks" collection in Firestore
        db.collection("tasks")
                .orderBy("timestamp", Query.Direction.ASCENDING) // Assuming you have a timestamp field in your tasks
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Task> tasks = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Convert Firestore document to Task object
                            Task currentTask = document.toObject(Task.class);
                            tasks.add(currentTask);
                        }
                        // Reverse the order of tasks to display the latest at the top
                        Collections.reverse(tasks);
                        // Update the adapter with the retrieved tasks
                        taskAdapter.setTasks(tasks);
                    } else {
                        // Handle errors
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }


}
