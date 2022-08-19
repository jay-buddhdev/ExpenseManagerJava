package com.example.expensemanagerjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensemanagerjava.Model.User;
import com.example.expensemanagerjava.Utils.Common;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText email,password;
    private Button loginBtn;
    private ImageView backBtn;
    private static final int RC_SIGN_IN = 234;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton google_sign;
    private ProgressDialog progressDialog;
    private TextView forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setup();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Please Wait Login in Process");
                progressDialog.show();
                validate();
            }
        });
        google_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait Login in Process");
                progressDialog.show();
                sign_in_google();
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),ForgetPasswordActivity.class);
                startActivity(i);
                finish();
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
        if (TextUtils.isEmpty(email.getText().toString())) {
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
        } if (TextUtils.isEmpty(password.getText().toString())){
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
        } else {
            Authentication();
        }
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
                                Toast.makeText(getApplicationContext(),"User Not Found Please Signup",Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(getApplicationContext(),SignupActivity.class);
                                progressDialog.dismiss();
                                startActivity(i);
                                finish();
                            } else {
                                getDataFromDatabase();
                        }
                        }
                    }
                });
    }
    private void Authentication() {
        mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            getDataFromDatabase();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this,"Email or Password is Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getDataFromDatabase() {
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
                        Intent i=new Intent(getApplicationContext(),DashboardActivity.class);
                        progressDialog.dismiss();
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "User Data Not Found", Toast.LENGTH_SHORT).show();
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
        email = findViewById(R.id.login_txtview_email);
        password = findViewById(R.id.login_txtview_password);
        loginBtn = findViewById(R.id.login_btn);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        google_sign = findViewById(R.id.login_btn_google);
        backBtn = findViewById(R.id.login_img_view_back_arrow);
        progressDialog = new ProgressDialog(this);
        forgetPassword = findViewById(R.id.login_forget_password);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

}