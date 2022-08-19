package com.example.expensemanagerjava;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.expensemanagerjava.Adapters.TransactionAdapter;
import com.example.expensemanagerjava.Model.TransacationModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class TempDashboardActivity extends AppCompatActivity {

    private RecyclerView transactionRecyclerview;
    DatabaseReference databaseReference;
    private ArrayList<TransacationModel> transactionList;
    private TransactionAdapter adapter;
    FloatingActionButton mAddIncomeFab, mAddExpenseFab;
    ExtendedFloatingActionButton mAddFab;
    private TextView addIncomeText,addExpenseText;
    Boolean isAllFabsVisible;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_dashboard);
        setup();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.child("Expense").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    transactionList = transactionList.stream()
//                            .filter(transaction -> transaction.getTransactionType() == true)
//                            .collect(Collectors.toCollection(ArrayList::new));
//                }
                transactionList.removeIf(transaction -> transaction.getTransactionType() == false);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    var transactionmodel = dataSnapshot.getValue(TransacationModel.class);
                    transactionmodel.setTransactionType(false);
                    transactionList.add(transactionmodel);
                }
                transactionList.sort((t1, t2) -> {
                    return t1.getDate().compareTo(t2.getDate());
                });
               adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("Income").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    transactionList = transactionList.stream()
//                            .filter(transaction -> transaction.getTransactionType() == false)
//                            .collect(Collectors.toCollection(ArrayList::new));
//                }
                transactionList.removeIf(transaction -> transaction.getTransactionType() == true);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    var transactionmodel = dataSnapshot.getValue(TransacationModel.class);
                    transactionmodel.setTransactionType(true);
                    transactionList.add(transactionmodel);

                }
                transactionList.sort((t1, t2) -> {
                    return t1.getDate().compareTo(t2.getDate());
                });

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new TransactionAdapter(transactionList);
        transactionRecyclerview.setAdapter(adapter);
        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isAllFabsVisible){
                    mAddIncomeFab.show();
                    mAddExpenseFab.show();
                    addIncomeText.setVisibility(View.VISIBLE);
                    addExpenseText.setVisibility(View.VISIBLE);
                    mAddFab.extend();
                    isAllFabsVisible = true;
                } else {
                    mAddIncomeFab.hide();
                    mAddExpenseFab.hide();
                    addIncomeText.setVisibility(View.GONE);
                    addExpenseText.setVisibility(View.GONE);
                    mAddFab.shrink();
                    isAllFabsVisible = false;
                }
            }
        });
        mAddIncomeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TempDashboardActivity.this, AddIncomeActivity.class));
            }
        });
        mAddExpenseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TempDashboardActivity.this, AddExpenseActivity.class));
            }
        });

    }

    private void setup() {
        transactionList = new ArrayList<>();
        mAddFab = findViewById(R.id.add_fab);
        mAddIncomeFab = findViewById(R.id.add_income_fab);
        mAddExpenseFab = findViewById(R.id.add_expense_fab);
        addIncomeText = findViewById(R.id.add_income_action_text);
        addExpenseText = findViewById(R.id.add_expense_action_text);
        mAddIncomeFab.setVisibility(View.GONE);
        mAddExpenseFab.setVisibility(View.GONE);
        addIncomeText.setVisibility(View.GONE);
        addExpenseText.setVisibility(View.GONE);
        isAllFabsVisible = false;
        transactionRecyclerview = findViewById(R.id.transcation_recyclerview);
    }
}