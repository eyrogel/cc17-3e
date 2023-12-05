package com.teamviewer.collabmates;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextName, editTextIdNumber;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        editTextEmail = findViewById(R.id.email_signup);
        editTextPassword = findViewById(R.id.password_signup);
        editTextName = findViewById(R.id.name);
        editTextIdNumber = findViewById(R.id.idNumber);

        Button haveAccountButton = findViewById(R.id.signin_button);

        haveAccountButton.setOnClickListener(view -> finish());

        Button signUp = findViewById(R.id.signup_button);

        signUp.setOnClickListener(view -> {
            String getName = editTextName.getText().toString().trim();
            String getEmail = editTextEmail.getText().toString().trim();
            String getPassword = editTextPassword.getText().toString().trim();
            String getIdNumber = editTextIdNumber.getText().toString().trim();

            if (TextUtils.isEmpty(getName) || TextUtils.isEmpty(getEmail) || TextUtils.isEmpty(getPassword) || TextUtils.isEmpty(getIdNumber)) {
                Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the email is already registered
            checkEmailExists(getEmail, getName, getIdNumber);
        });
    }

    private void checkEmailExists(String email, String name, String idNumber) {
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // If the email is not registered, create a new user
                        if (task.getResult().getSignInMethods().isEmpty()) {
                            firebaseAuth.createUserWithEmailAndPassword(email, editTextPassword.getText().toString().trim())
                                    .addOnCompleteListener(this, signUpTask -> {
                                        if (signUpTask.isSuccessful()) {
                                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                            if (firebaseUser != null) {
                                                saveUserDataToFirebase(firebaseUser.getUid(), name, idNumber, email);
                                            }
                                            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(SignUpActivity.this, "Failed: " + signUpTask.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Email is already registered
                            Toast.makeText(SignUpActivity.this, "Email is already registered", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle the exception if the email check fails
                        Toast.makeText(SignUpActivity.this, "Failed to check email: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserDataToFirebase(String userId, String name, String idNumber, String email) {
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("idNumber", idNumber);
        userData.put("email", email);

        usersRef.child(userId).setValue(userData);
        Log.d("SignUpActivity", "User data saved to Firebase: " + userData.toString());
    }
}
