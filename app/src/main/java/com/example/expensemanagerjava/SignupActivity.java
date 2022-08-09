package com.example.expensemanagerjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.expensemanagerjava.Model.User;
import com.example.expensemanagerjava.Utils.Common;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText name,email,password;
    private Button signUpBtn;
    private ImageView backBtn;
    private CheckBox termsAndCondition;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 234;
    SignInButton google_sign;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setup();
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Please Wait Sign up in Process");
                progressDialog.show();
                validate();
            }
        });
        google_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait Sign Up in Process");
                progressDialog.show();
                sign_in_google();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),WalkThroughScreenActivity.class);
                startActivity(i);
                finish();
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
            Toast.makeText(this,"Please Check Terms and Conditions",Toast.LENGTH_SHORT).show();
        }else {
            Authentication();
        }
    }

    private void Authentication() {
        mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                          saveToDatabase(0);
                        }
                        else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                progressDialog.dismiss();
                                Toast.makeText(SignupActivity.this,"Provided Email Is Already Registered!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
    }

    private void saveToDatabase(int type){
        FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
        User user;
        if(type == 0) {
            user = new User(name.getText().toString(),email.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getUid(),0l);
        } else {
            user = new User(firebaseuser.getDisplayName().toString(),firebaseuser.getEmail().toString(),FirebaseAuth.getInstance().getCurrentUser().getUid(),0l);
        }
        mDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Common.currentUser = new User(name.getText().toString(),email.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(),0l);
                        Toast.makeText(getApplicationContext(),"User Registration Done",Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getApplicationContext(),DashboardActivity.class);
                        progressDialog.dismiss();
                        startActivity(i);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"User Registration Failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void sign_in_google() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                            if (isNewUser){
                                saveToDatabase(1);
                            }
                        } else {
                            fetchData();
                        }
                    }
                });
    }

    private void fetchData() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Query query=db.child("Users").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot user:snapshot.getChildren()){
                        User usermodel = user.getValue(User.class);
                        if(usermodel.getUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        {
                            Common.currentUser=usermodel;
                            Intent i=new Intent(getApplicationContext(),DashboardActivity.class);
                            progressDialog.dismiss();
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(SignupActivity.this, "User Data Not Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        termsAndCondition = findViewById(R.id.signup_checkbox_terms);
        google_sign = findViewById(R.id.signup_btn_google);
        backBtn = findViewById(R.id.signup_img_view_back_arrow);
        progressDialog = new ProgressDialog(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
}