package com.teamviewer.collabmates;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateFragment extends Fragment {

    private FirebaseFirestore db;

    public CreateFragment() {
        // Required empty public constructor
    }

    public static CreateFragment newInstance() {
        return new CreateFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create, container, false);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Find the "Post Task" button
        Button postTaskButton = rootView.findViewById(R.id.buttonPostTask);

        // Set an OnClickListener for the "Post Task" button
        postTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call a method to save data to Firestore
                saveTaskToFirestore();
            }
        });

        // Find the EditText with the ID editTextCalendar
        EditText calendarEditText = rootView.findViewById(R.id.editTextCalendar);

        // Set an OnClickListener for the EditText
        calendarEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        return rootView;
    }

    public void showDatePickerDialog(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        EditText calendarEditText = requireView().findViewById(R.id.editTextCalendar);
                        calendarEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void saveTaskToFirestore() {
        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Check if the user is authenticated
        if (currentUser == null) {
            // Handle the case where the user is not authenticated
            return;
        }

        // Retrieve user's name from Realtime Database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        String userId = currentUser.getUid();

        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // User data found, retrieve the name
                    String userName = snapshot.child("name").getValue(String.class);

                    // Get the values from the EditText fields
                    EditText titleEditText = requireView().findViewById(R.id.editTextTitle);
                    EditText taskEditText = requireView().findViewById(R.id.editTextTask);
                    EditText budgetEditText = requireView().findViewById(R.id.editTextBudget);
                    EditText calendarEditText = requireView().findViewById(R.id.editTextCalendar);

                    String title = titleEditText.getText().toString();
                    String task = taskEditText.getText().toString();
                    String budget = budgetEditText.getText().toString();
                    String deadline = calendarEditText.getText().toString();

                    // Check for empty fields
                    if (title.isEmpty() || task.isEmpty() || budget.isEmpty() || deadline.isEmpty()) {
                        // Show a message or handle the case where fields are empty
                        return;
                    }

                    // Create a Map to represent the data
                    Map<String, Object> taskData = new HashMap<>();
                    taskData.put("title", title);
                    taskData.put("task", task);
                    taskData.put("budget", budget);
                    taskData.put("deadline", deadline);
                    taskData.put("name", userName);  // Use the retrieved name
                    taskData.put("timestamp", System.currentTimeMillis());  // Add timestamp

                    // Add the data to Firestore
                    db.collection("tasks")
                            .add(taskData)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    // Data added successfully
                                    // Clear input fields
                                    clearInputFields();

                                    // After saving, navigate to FeedFragment
                                    navigateToFeedFragment(title, task, budget, deadline);

                                    // Switch to the "Feed" fragment in MainActivity
                                    switchToFeedFragment();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure
                                    // You might want to log the exception or display an error message
                                }
                            });
                } else {
                    // User data not found in Realtime Database
                    // Handle the case accordingly
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if the data retrieval is canceled
            }
        });
    }

    private void navigateToFeedFragment(String title, String task, String budget, String deadline) {
        // Create an instance of FeedFragment
        FeedFragment feedFragment = FeedFragment.newInstance();

        // Pass data to FeedFragment
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("task", task);
        bundle.putString("budget", budget);
        bundle.putString("deadline", deadline);
        feedFragment.setArguments(bundle);

        // Get the FragmentManager
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // Begin the transaction
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace the contents of the container with the new fragment
        transaction.replace(R.id.frameLayout1, feedFragment);  // Replace with the correct ID of the container

        // Add the transaction to the back stack
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private void clearInputFields() {
        EditText titleEditText = requireView().findViewById(R.id.editTextTitle);
        EditText taskEditText = requireView().findViewById(R.id.editTextTask);
        EditText budgetEditText = requireView().findViewById(R.id.editTextBudget);
        EditText calendarEditText = requireView().findViewById(R.id.editTextCalendar);

        titleEditText.setText("");
        taskEditText.setText("");
        budgetEditText.setText("");
        calendarEditText.setText("");
    }

    private void switchToFeedFragment() {
        // Reference to the "Feed" menu item in BottomNavigationView
        getActivity().findViewById(R.id.menu_feed).performClick();
    }
}
