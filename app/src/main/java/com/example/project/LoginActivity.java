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

import com.example.project.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private ActionBar actionBar;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private String email = "", password = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        actionBar = getSupportActionBar();
        actionBar.setTitle("Login");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Logging In");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        binding.haveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 validateData();
            }
        });
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        }
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
        else{
            firebaseLogin();
        }
    }

    private void firebaseLogin() {
         progressDialog.show();

         firebaseAuth.signInWithEmailAndPassword(email, password)
                 .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                     @Override
                     public void onSuccess(AuthResult authResult) {
                         FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                         String email = firebaseUser.getEmail();
                         Toast.makeText(LoginActivity.this, "LoggIn\n"+email, Toast.LENGTH_SHORT).show();

                         startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                         finish();
                     }
                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                          progressDialog.dismiss();
                          Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                     }
                 });
    }
}