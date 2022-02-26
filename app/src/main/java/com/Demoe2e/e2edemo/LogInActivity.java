package com.Demoe2e.e2edemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.Demoe2e.e2edemo.databinding.ActivityLogInBinding;
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

public class LogInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    ActivityLogInBinding logInBinding;
    GoogleSignInClient mGoogleSignInClient;

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
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            redirect(MainActivity.class);
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("238585194263-pf71mtc5a95vdmhm34osh9sbs3cqpeve.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Button listeners
        logInBinding.login.setOnClickListener(view -> {
            String email = logInBinding.email.getText().toString();
            String password = logInBinding.password.getText().toString();
            if (!email.isEmpty() && !password.isEmpty()) {
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        redirect(MainActivity.class);
                    } else {
                        Toast.makeText(LogInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(LogInActivity.this, "Enter Credentials", Toast.LENGTH_SHORT).show();
            }
        });
        logInBinding.googleLoginBtn.setOnClickListener(view -> {
            progressDialog.show();
            signIn();
        });
        logInBinding.signupLabel.setOnClickListener(view -> redirect(SignUp.class));
    }

    private static final int RC_SIGN_IN = 48;

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

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
//                        FirebaseUser user = mAuth.getCurrentUser();
                        redirect(MainActivity.class);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(LogInActivity.this, "Signing fail", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void redirect(Class<? extends AppCompatActivity> activity) {
        Intent intent = new Intent(LogInActivity.this, activity);
        startActivity(intent);
        finish();
    }
}