package com.example.expensemanagerjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.expensemanagerjava.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText name,email,password;
    private Button signUpBtn;
    private CheckBox termsAndCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setup();
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }

    private void validate() {
        if(TextUtils.isEmpty(name.getText().toString())){
            Toast.makeText(this,"Please Enter Name",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email.getText().toString())) {
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
        } if (TextUtils.isEmpty(password.getText().toString())){
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
        } else if(!termsAndCondition.isChecked()){
            Toast.makeText(this,"Please Check Terms and Condition",Toast.LENGTH_SHORT).show();
        } else {
            Authentication();
        }
    }

    private void Authentication() {
        mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            mDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(
                                    new User(name.getText().toString(),email.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getApplicationContext(),"User Registration Done",Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(),"User Registration Failed",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {

                                Toast.makeText(SignupActivity.this,"Provided Email Is Already Registered!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
    }

    private void setup() {
        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.signup_txtview_name);
        email = findViewById(R.id.signup_txtview_email);
        password = findViewById(R.id.signup_txtview_password);
        signUpBtn = findViewById(R.id.signup_btn);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
}