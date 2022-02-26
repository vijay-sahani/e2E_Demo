package com.Demoe2e.e2edemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.Demoe2e.e2edemo.databinding.ActivityChatDetailBinding;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding chatDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatDetailBinding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(chatDetailBinding.getRoot());
        getSupportActionBar().hide();

        // back button binding
        chatDetailBinding.backBtn.setOnClickListener(view -> {
            redirect(MainActivity.class);
            finish();
        });

        String userName = getIntent().getStringExtra("username");
        String uid = getIntent().getStringExtra("uid");
        String profilePic = getIntent().getStringExtra("profilePic");

        chatDetailBinding.userName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.img_1).into(chatDetailBinding.profileImage);


    }

    @Override
    public void onBackPressed() {
        redirect(MainActivity.class);
        finish();
    }

    private void redirect(Class<? extends AppCompatActivity> activity) {
        Intent intent = new Intent(ChatDetailActivity.this, activity);
        startActivity(intent);
    }
}