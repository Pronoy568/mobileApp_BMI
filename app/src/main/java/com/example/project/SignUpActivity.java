package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.project.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    private ActionBar actionBar;

    private String email = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        actionBar = getSupportActionBar();
        actionBar.setTitle("SignUp");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Creating you account...");
        progressDialog.setCanceledOnTouchOutside(false);
        
        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void validateData() {
        email = binding.emailEt.getText().toString().trim();
        password = binding.passwordTilEt.getText().toString().trim();

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailEt.setError("InValid email format");
        }
        else if(TextUtils.isEmpty(password)){
            binding.passwordTilEt.setError("Enter Password");
        }
        else if(password.length()<6){
            binding.emailEt.setError("Password must at least 6 charters long");
        }
        else{
            firebaseSignUp();
        }
    }

    private void firebaseSignUp() {
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog.dismiss();

                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        String email = firebaseUser.getEmail();
                        Toast.makeText(SignUpActivity.this, "Account Created\n"+email, Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(SignUpActivity.this, ProfileActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}