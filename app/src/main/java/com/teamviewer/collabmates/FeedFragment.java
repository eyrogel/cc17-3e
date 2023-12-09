package com.teamviewer.collabmates;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private RecyclerView postsRecyclerView;
    private RecyclerView feedRecyclerView;
    private TasksAdapter tasksAdapter;
    private MyPostsAdapter myPostsAdapter;
    private FirebaseFirestore db;

    private View postsLayout;
    private View feedLayout;

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

        // Initialize layouts
        postsLayout = view.findViewById(R.id.postsLayout);
        feedLayout = view.findViewById(R.id.feedLayout);

        // Set click listeners for the buttons
        postsButton.setOnClickListener(v -> {
            postsButton.setBackgroundResource(R.drawable.rectangle_pressed);
            tasksButton.setBackgroundResource(R.drawable.rectangle_normal);

            // Show postsLayout and hide feedLayout
            postsLayout.setVisibility(View.VISIBLE);
            feedLayout.setVisibility(View.GONE);

            // Fetch tasks created by the current user from Firestore for "My Posts"
            fetchUserTasksFromFirestore();
        });

        tasksButton.setOnClickListener(v -> {
            // Ensure that db is not null before performing the click
            if (db != null) {
                tasksButton.setBackgroundResource(R.drawable.rectangle_pressed);
                postsButton.setBackgroundResource(R.drawable.rectangle_normal);

                // Show feedLayout and hide postsLayout
                feedLayout.setVisibility(View.VISIBLE);
                postsLayout.setVisibility(View.GONE);

                // Fetch tasks created by other users from Firestore for "Offered Tasks"
                fetchOtherUsersTasksFromFirestore();
            } else {
                Log.e("FeedFragment", "FirebaseFirestore is null");
            }
        });

        // Set the background resource to rectangle_pressed for "Offered Tasks" button
        tasksButton.setBackgroundResource(R.drawable.rectangle_pressed);

        // Initialize shared ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Initialize RecyclerViews
        postsRecyclerView = view.findViewById(R.id.postsRecyclerView);
        feedRecyclerView = view.findViewById(R.id.feedRecyclerView);

        // Initialize the TasksAdapter for Offered Tasks
        tasksAdapter = new TasksAdapter(requireContext(), new ArrayList<>(), sharedViewModel);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        feedRecyclerView.setAdapter(tasksAdapter);

        // Initialize the MyPostsAdapter for My Posts
        myPostsAdapter = new MyPostsAdapter(new ArrayList<>());
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        postsRecyclerView.setAdapter(myPostsAdapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Fetch tasks created by other users from Firestore by default
        fetchOtherUsersTasksFromFirestore();

        return view;
    }

    private void fetchUserTasksFromFirestore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null || db == null) {
            return;
        }
        String userId = currentUser.getUid();
        Log.d("FeedFragment", "fetchUserTasksFromFirestore: User ID - " + userId);

        db.collection("tasks")
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Task> tasks = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Task currentTask = document.toObject(Task.class);
                            tasks.add(currentTask);
                        }
                        Collections.reverse(tasks);
                        myPostsAdapter.setTasks(tasks);

                        Log.d("FeedFragment", "fetchUserTasksFromFirestore: Number of tasks retrieved - " + tasks.size());
                    } else {
                        Log.w("FeedFragment", "Error getting documents.", task.getException());
                    }
                });
    }

    private void fetchOtherUsersTasksFromFirestore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            return;
        }
        String userId = currentUser.getUid();
        Log.d("FeedFragment", "fetchOtherUsersTasksFromFirestore: User ID - " + userId);

        db.collection("tasks")
                .whereNotEqualTo("userId", userId)  // Exclude tasks created by the current user
                .orderBy("userId")  // Match the inequality filter property (userId)
                .orderBy("deadline", Query.Direction.ASCENDING) // Order by a different field, e.g., deadline
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Task> tasks = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Task currentTask = document.toObject(Task.class);
                            tasks.add(currentTask);
                        }
                        Collections.reverse(tasks);
                        tasksAdapter.setTaskList(tasks);

                        Log.d("FeedFragment", "fetchOtherUsersTasksFromFirestore: Number of tasks retrieved - " + tasks.size());
                    } else {
                        Log.w("FeedFragment", "Error getting documents.", task.getException());
                    }
                });
    }
}
