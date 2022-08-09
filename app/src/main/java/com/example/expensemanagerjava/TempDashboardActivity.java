package com.example.expensemanagerjava;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;

import com.example.expensemanagerjava.Adapters.TransactionAdapter;
import com.example.expensemanagerjava.Model.TransacationModel;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_dashboard);
        transactionList = new ArrayList<>();
        transactionRecyclerview = findViewById(R.id.transcation_recyclerview);
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
                transactionList.removeIf(transaction -> transaction.getTransactionType() == true);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    var transactionmodel = dataSnapshot.getValue(TransacationModel.class);
                    transactionmodel.setTransactionType(false);
                    transactionList.add(transactionmodel);
                }
               // adapter.notifyDataSetChanged();
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
                transactionList.removeIf(transaction -> transaction.getTransactionType() == false);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    var transactionmodel = dataSnapshot.getValue(TransacationModel.class);
                    transactionmodel.setTransactionType(true);
                    transactionList.add(transactionmodel);

                }
               // adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new TransactionAdapter(transactionList);
        transactionRecyclerview.setAdapter(adapter);

    }
}