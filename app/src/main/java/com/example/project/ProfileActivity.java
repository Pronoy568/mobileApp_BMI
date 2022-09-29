package com.example.project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    private ActionBar actionBar;

    private FirebaseAuth firebaseAuth;

    EditText heights,weights;
    Button btn;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        heights = findViewById(R.id.height);
        weights = findViewById(R.id.weight);
        btn = findViewById(R.id.btn);
        result = findViewById(R.id.result);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Double weight = new Double(weights.getText().toString());
                    Double height = new Double(heights.getText().toString());
                    Double updateHeight = height*0.3048;
                    Double lasestHeight = updateHeight*updateHeight;
                    Double bmi = weight/lasestHeight;

                    String finalResult = String.format("%.2f", bmi);

                    result.setText(new Double(finalResult).toString());
                }
                catch (Exception c){
                    Toast.makeText(getApplicationContext(), "Error 404", Toast.LENGTH_SHORT).show();
                }
            }
        });

        actionBar = getSupportActionBar();
        actionBar.setTitle("Login");

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                checkUser();
            }
        });
    }

    private void checkUser() {

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
             startActivity(new Intent(this, LoginActivity.class));
             finish();
        }else{
            String email = firebaseUser.getEmail();
            binding.emailTv.setText(email);
        }
    }
}