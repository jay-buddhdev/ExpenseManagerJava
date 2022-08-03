package com.example.expensemanagerjava;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText email;
    private MaterialButton continueBtn;
    private FirebaseAuth mAuth;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        setup();
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void validate() {
        if(TextUtils.isEmpty(email.getText().toString())){
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
        } else {
           checkEmailExist();
        }
    }

    private void checkEmailExist() {
        mAuth.fetchSignInMethodsForEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.getResult().getSignInMethods().size() == 0){
                    Toast.makeText(ForgetPasswordActivity.this, "Please Signup No Email Found", Toast.LENGTH_SHORT).show();
                }else {
                    sendResetEmail();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void sendResetEmail() {
        mAuth.sendPasswordResetEmail(email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent i=new Intent(getApplicationContext(),ForgetPasswordEmailActivity.class);
                            startActivity(i);
                            finish();
                            Log.d("send Password", "Email sent.");
                        }else {
                            Toast.makeText(ForgetPasswordActivity.this, "Email Could Not be Sent", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setup() {
        email = findViewById(R.id.forget_password_editxt_email);
        continueBtn = findViewById(R.id.forget_password_continue_btn);
        mAuth = FirebaseAuth.getInstance();
        backBtn = findViewById(R.id.forget_password_img_view_back_arrow);
    }
}