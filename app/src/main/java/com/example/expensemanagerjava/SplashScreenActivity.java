package com.example.expensemanagerjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.expensemanagerjava.Model.User;
import com.example.expensemanagerjava.Utils.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SplashScreenActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 5000;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        firebaseAuth=FirebaseAuth.getInstance();
        setup();
    }

    private void setup() {
        if (firebaseAuth.getCurrentUser() == null){
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent splashIntent = new Intent(SplashScreenActivity.this, WalkThroughScreenActivity.class);
                    startActivity(splashIntent);
                    finish();
                }
            }, SPLASH_DISPLAY_LENGTH);

        } else {
            fetchData();
        }
    }

    private void fetchData() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Query query=db.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User usermodel= snapshot.getValue(User.class);
                    if(usermodel.getUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        Common.currentUser=usermodel;
                        Intent i=new Intent(getApplicationContext(),AddIncomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(SplashScreenActivity.this, "User Data Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}