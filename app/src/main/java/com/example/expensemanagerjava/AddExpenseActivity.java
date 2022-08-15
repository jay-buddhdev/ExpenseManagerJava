package com.example.expensemanagerjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.expensemanagerjava.Model.Expense;
import com.example.expensemanagerjava.Utils.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText name,amount,expenseDate;
    private ImageView backarrow;
    // private DatePicker incomeDate;
    private MaterialButton addExpenseBtn,submitBtn;
    private DatabaseReference mDatabase;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri filePath;
    Date date;
    Uri downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        setup();
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDatabase();
            }
        });
    }

    private void saveToDatabase() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd/HH-mm-ss");
        String formattedDate = formatter.format(Common.getDateFromDatePicker(expenseDate));
        Expense expense = new Expense(name.getText().toString(),"",amount.getText().toString(),formattedDate);
        mDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Expense").push().setValue(expense)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        long currentUserBalance = Common.currentUser.getBalance();
                        long newBalance = currentUserBalance - Long.valueOf(amount.getText().toString());
                        Common.currentUser.setBalance(newBalance);
                        mDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue(newBalance);
                        Toast.makeText(AddExpenseActivity.this, "Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddExpenseActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setup() {
        name = findViewById(R.id.add_expense_txtview_name);
        amount = findViewById(R.id.add_expense_txtview_amount);
        expenseDate = findViewById(R.id.expense_date_editext);
        submitBtn = findViewById(R.id.expense_btn);
        addExpenseBtn = findViewById(R.id.add_expense_invoice);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        date = new Date();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        backarrow = findViewById(R.id.add_expense_back_arrow);
    }
}