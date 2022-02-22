package com.Demoe2e.e2edemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.Demoe2e.e2edemo.databinding.ActivityLogInBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    ActivityLogInBinding logInBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logInBinding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(logInBinding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        Objects.requireNonNull(getSupportActionBar()).hide(); // Hiding the action bar
        // Progress dialog
        progressDialog = new ProgressDialog(LogInActivity.this);
        progressDialog.setTitle("Logging in");
        progressDialog.setMessage("Validating user...");

        // Login btn listener
        logInBinding.login.setOnClickListener(view -> {
            String email = logInBinding.email.getText().toString();
            String password = logInBinding.password.getText().toString();
            if (!email.isEmpty() && !password.isEmpty()) {
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        redirect(LogInActivity.this,MainActivity.class);
                    } else {
                        Toast.makeText(LogInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(LogInActivity.this, "Enter Credentials", Toast.LENGTH_SHORT).show();
            }
        });
        logInBinding.signupLabel.setOnClickListener(view -> {
            redirect(LogInActivity.this,SignUp.class);
        });
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            redirect(LogInActivity.this,MainActivity.class);
        }
    }

    private void redirect(Context context, Class< ? extends AppCompatActivity> activity ) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }
}