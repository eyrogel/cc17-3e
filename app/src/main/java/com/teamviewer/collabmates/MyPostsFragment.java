package com.teamviewer.collabmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class MyPostsFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyPostsAdapter myPostsAdapter;
    private List<Task> taskList; // Assuming Task is your data model
    private FirebaseFirestore db;
    private CollectionReference tasksCollection;
    private SharedViewModel sharedViewModel;  // Add this line

    public MyPostsFragment() {
        // Required empty public constructor
    }

    public static MyPostsFragment newInstance() {
        return new MyPostsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_posts, container, false);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        tasksCollection = db.collection("tasks"); // Replace with your collection name

        // Initialize RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        taskList = new ArrayList<>();
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);  // Add this line
        myPostsAdapter = new MyPostsAdapter(requireContext(), taskList, sharedViewModel);  // Modify this line
        recyclerView.setAdapter(myPostsAdapter);

        // Load data from Firestore
        loadDataFromFirestore();

        return rootView;
    }

    // ... (existing code)

    private void loadDataFromFirestore() {
        tasksCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                taskList.clear(); // Clear existing data
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Convert Firestore document to your Task model
                    Task taskModel = document.toObject(Task.class);
                    taskList.add(taskModel);
                }
                myPostsAdapter.notifyDataSetChanged();
            } else {
                // Handle failure
            }
        });
    }
}
