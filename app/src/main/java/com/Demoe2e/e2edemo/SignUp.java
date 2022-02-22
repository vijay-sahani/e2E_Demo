package com.Demoe2e.e2edemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.Demoe2e.e2edemo.Models.Users;
import com.Demoe2e.e2edemo.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUp extends AppCompatActivity {
    ActivitySignUpBinding signUpBinding;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(signUpBinding.getRoot());
        // Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        Objects.requireNonNull(getSupportActionBar()).hide(); // Hiding the action bar
        // Progress dialog
        progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("Creating your e2E account.");

        signUpBinding.signUpBtn.setOnClickListener(view -> {
            String email = signUpBinding.email.getText().toString();
            String password = signUpBinding.password.getText().toString();
            if (!email.isEmpty() && !password.isEmpty()) {
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Users user = new Users(getUserName(email), email, password);
                        String userId = task.getResult().getUser().getUid();
                        database.getReference().child("Users").child(userId).setValue(user);
                        Toast.makeText(SignUp.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                        redirect(LogInActivity.class);
                    } else {
                        Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(SignUp.this, "Enter credentials", Toast.LENGTH_SHORT).show();
            }

        });
        signUpBinding.loginLabel.setOnClickListener(view -> {
            redirect(LogInActivity.class);
        });
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser !=null){
            redirect(MainActivity.class);
        }
    }


    private String getUserName(String email) {
        return email.substring(0, email.indexOf('@'));
    }
    private void redirect(Class< ? extends AppCompatActivity> activity ) {
        Intent intent = new Intent(SignUp.this, activity);
        startActivity(intent);
    }
}
