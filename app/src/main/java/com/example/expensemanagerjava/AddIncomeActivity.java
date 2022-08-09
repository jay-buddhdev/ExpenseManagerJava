package com.example.expensemanagerjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.expensemanagerjava.Model.Expense;
import com.example.expensemanagerjava.Model.Income;
import com.example.expensemanagerjava.Utils.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

public class AddIncomeActivity extends AppCompatActivity {

    private EditText name,amount;
    private DatePicker incomeDate;
    private Button submitBtn;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);
        setup();
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDatabase();
            }
        });
    }


    private void saveToDatabase() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd/hh-MM-SS");
        String formattedDate = formatter.format(Common.getDateFromDatePicker(incomeDate));
        var income = new Income(name.getText().toString(),"",amount.getText().toString(),formattedDate);
        mDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Income").push().setValue(income)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        long currentUserBalance = Common.currentUser.getBalance();
                        long newBalance = currentUserBalance + Long.valueOf(amount.getText().toString());
                        Common.currentUser.setBalance(newBalance);
                        mDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue(newBalance);
                        Toast.makeText(AddIncomeActivity.this, "Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddIncomeActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setup() {
        name = findViewById(R.id.income_name);
        amount = findViewById(R.id.income_amount);
        incomeDate = findViewById(R.id.income_date);
        submitBtn = findViewById(R.id.income_btn);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


}