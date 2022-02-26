package com.Demoe2e.e2edemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.Demoe2e.e2edemo.Models.Users;
import com.Demoe2e.e2edemo.databinding.ActivitySignUpBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUp extends AppCompatActivity {
    ActivitySignUpBinding signUpBinding;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    GoogleSignInClient mGoogleSignInClient;

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
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("238585194263-pf71mtc5a95vdmhm34osh9sbs3cqpeve.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signUpBinding.googleLoginBtn.setOnClickListener(view -> {
            progressDialog.show();
            signIn();
        });
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
                        user.setUserId(userId);
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
        if (currentUser != null) {
            redirect(MainActivity.class);
        }
    }

    private static final int RC_SIGN_IN = 48;

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private String getUserName(String email) {
        return email.substring(0, email.indexOf('@'));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Users users = new Users();
                        users.setUserId(user.getUid());
                        users.setEmail(user.getEmail());
                        users.setUserName(getUserName(user.getEmail()));
                        users.setProfilePic(Objects.requireNonNull(user.getPhotoUrl()).toString());
                        database.getReference().child("Users").child(user.getUid()).setValue(users);
                        redirect(MainActivity.class);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(SignUp.this, "Signing fail", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void redirect(Class<? extends AppCompatActivity> activity) {
        Intent intent = new Intent(SignUp.this, activity);
        startActivity(intent);
        finish();
    }
}
