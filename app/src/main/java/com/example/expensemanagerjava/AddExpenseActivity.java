package com.example.expensemanagerjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.expensemanagerjava.Model.Expense;
import com.example.expensemanagerjava.Utils.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText name,amount,expenseDate;
    private ImageView backarrow;
    // private DatePicker incomeDate;
    private MaterialButton addExpenseBtn,submitBtn;
    private DatabaseReference mDatabase;
    private Spinner categorySpinner;
    List<String> category;
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
                if(TextUtils.isEmpty(name.getText().toString())){
                    Toast.makeText(AddExpenseActivity.this, "Name is Empty", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(amount.getText().toString())){
                    Toast.makeText(AddExpenseActivity.this, "Amount is Empty", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(expenseDate.getText().toString())){
                    Toast.makeText(AddExpenseActivity.this, "Date is Empty", Toast.LENGTH_SHORT).show();
                }else {
                    if (downloadUrl == null) {
                        downloadUrl = Uri.parse("");
                    }
                    saveToDatabase(downloadUrl.toString());
                }

            }
        });
        addExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
                submitBtn.setEnabled(false);
            }
        });
        expenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        AddExpenseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                expenseDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();

            }
        });
        loadSpinner();
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddExpenseActivity.this, TempDashboardActivity.class));
                finish();
            }
        });
    }

    private void loadSpinner() {
        mDatabase.child("Users").child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot areaSnapshot: snapshot.getChildren()) {
                    String categoryName = areaSnapshot.child("categoryName").getValue(String.class);
                    category.add(categoryName);
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(AddExpenseActivity.this, android.R.layout.simple_spinner_item, category);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploadImage();

            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
    private void uploadImage() {
        if (filePath != null) {
            StorageReference ref
                    = storageReference
                    .child(
                            "images/expense_proof"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {

//                                    Toast.makeText(AddIncomeActivity.this,
//                                                    "Image Uploaded!!",
//                                                    Toast.LENGTH_SHORT)
//                                            .show();
                                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!urlTask.isSuccessful());
                                    downloadUrl = urlTask.getResult();
                                    submitBtn.setEnabled(true);
                                    addExpenseBtn.setText("Expense Proof Added");
                                    addExpenseBtn.setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_done_24));
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            submitBtn.setEnabled(true);
                            Toast
                                    .makeText(AddExpenseActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        }
    }
    private void saveToDatabase(String imageUrl) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+05:30"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd/HH-mm-ss");
        //formatter.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
        String[] ymd = expenseDate.getText().toString().split("-");
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(ymd[0]));
        cal.set(Calendar.MONTH, Integer.parseInt(ymd[1])-1);
        cal.set(Calendar.YEAR, Integer.parseInt(ymd[2]));
        Log.d("Date",cal.getTime().toString());
        String formattedDate = formatter.format(cal.getTime());
        // String formattedDate = formatter.format(date);
        Expense expense = new Expense(name.getText().toString(),categorySpinner.getSelectedItem().toString(),amount.getText().toString(),formattedDate,imageUrl);
        mDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Expense").push().setValue(expense)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        long currentUserBalance = Common.currentUser.getBalance();
                        long currentExpense = Common.currentUser.getTotalExpense();
                        long newExpense = currentExpense + Long.valueOf(amount.getText().toString());
                        long newBalance = currentUserBalance - Long.valueOf(amount.getText().toString());
                        Common.currentUser.setTotalExpense(newExpense);
                        Common.currentUser.setBalance(newBalance);
                        Toast.makeText(AddExpenseActivity.this, Common.currentUser.getTotalExpense().toString(), Toast.LENGTH_SHORT).show();
                        mDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("TotalExpense").setValue(newExpense);
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
        categorySpinner = findViewById(R.id.add_expense_spinner_category);
        category =  new ArrayList<String>();
    }
}