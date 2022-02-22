package com.Demoe2e.e2edemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.Demoe2e.e2edemo.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    FirebaseAuth currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        // Logout user
        currentUser = FirebaseAuth.getInstance();
        activityMainBinding.logoutBtn.setOnClickListener(view -> {
            currentUser.signOut();
            forwardUser(MainActivity.this,LogInActivity.class);
        });
    }

    public void forwardUser(Context context, Class<? extends AppCompatActivity> activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
    }
}