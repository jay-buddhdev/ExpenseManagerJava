package com.example.expensemanagerjava;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.expensemanagerjava.Adapters.CategoryAdapter;
import com.example.expensemanagerjava.Adapters.TransactionAdapter;
import com.example.expensemanagerjava.Model.CategoryItems;
import com.example.expensemanagerjava.Model.TransacationModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView categoryRecyclerview;
    DatabaseReference databaseReference;
    private ArrayList<CategoryItems> CategoryList;
    private CategoryAdapter adapter;
    private ImageView backarrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setUp();
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CategoryActivity.this, DashboardActivity.class));
                finish();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CategoryActivity.this, AddCategoryActivity.class));
            }
        });
        databaseReference.child("Category").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    var categorymodel = dataSnapshot.getValue(CategoryItems.class);
                    CategoryList.add(categorymodel);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new CategoryAdapter(CategoryList);
        categoryRecyclerview.setAdapter(adapter);
    }

    private void setUp() {
        fab = findViewById(R.id.add_fab);
        categoryRecyclerview = findViewById(R.id.category_recyclerview);
        CategoryList = new ArrayList<>();
        backarrow = findViewById(R.id.category_back_arrow);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
    }
}