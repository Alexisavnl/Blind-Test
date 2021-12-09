package com.example.quizmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(currentUser == null){
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                } else{
                    startActivity(new Intent(SplashScreen.this, Searchactivity.class));
                }

            }
        }, 1500);
    }

}