package com.teamviewer.collabmates;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private TextView nameTextView, idNumberTextView;
    private Button signOutButton;
    private Button ratingsButton;
    private Button portfolioButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameTextView = view.findViewById(R.id.nameTextView);
        idNumberTextView = view.findViewById(R.id.idNumberTextView);
        signOutButton = view.findViewById(R.id.signOutButton);
        ratingsButton = view.findViewById(R.id.ratingsButton);
        portfolioButton = view.findViewById(R.id.portfolioButton);

        // Call method to fetch and set user data
        fetchAndSetUserData();
        // Inflate the layout for this fragment

        // Initialize buttons

        // Set click listeners for the buttons
        ratingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingsButton.setBackgroundResource(R.drawable.rectangle_pressed);
                portfolioButton.setBackgroundResource(R.drawable.rectangle_normal);
            }
        });

        portfolioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                portfolioButton.setBackgroundResource(R.drawable.rectangle_pressed);
                ratingsButton.setBackgroundResource(R.drawable.rectangle_normal);
            }
        });

        // Simulate a click on the "Offered Task" button to set it as default
        portfolioButton.performClick();

        // Set click listener for the Sign Out button
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        return view;
    }

    private void fetchAndSetUserData() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            Log.d("ProfileFragment", "User UID: " + firebaseUser.getUid());

            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

            usersRef.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("ProfileFragment", "DataSnapshot: " + dataSnapshot.toString());

                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String idNumber = dataSnapshot.child("idNumber").getValue(String.class);

                        // Log the retrieved data
                        Log.d("ProfileFragment", "Name: " + name + ", ID Number: " + idNumber);

                        // Set the retrieved data in the TextViews
                        if (nameTextView != null && idNumberTextView != null) {
                            nameTextView.setText(name);
                            idNumberTextView.setText("ID Number: " + idNumber);
                        } else {
                            Log.e("ProfileFragment", "TextViews are null");
                        }
                    } else {
                        Log.e("ProfileFragment", "DataSnapshot does not exist");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error
                    Log.e("ProfileFragment", "DatabaseError: " + databaseError.getMessage());
                }
            });
        } else {
            Log.e("ProfileFragment", "FirebaseUser is null");
        }
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        // Navigate to the SignInActivity after signing out
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish(); // Finish the current activity to prevent going back to the profile fragment
    }
}
