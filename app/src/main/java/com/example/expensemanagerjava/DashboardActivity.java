package com.example.expensemanagerjava;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.expensemanagerjava.Adapters.TransactionAdapter;
import com.example.expensemanagerjava.Model.TransacationModel;
import com.example.expensemanagerjava.Utils.Common;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity {

    private CircleImageView userImage;
    private TextView userName,userBalance,userIncome,userExpense;
    private RecyclerView categoryRecyclerview;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView transactionRecyclerview;
    DatabaseReference databaseReference;
    private ArrayList<TransacationModel> transactionList;
    private TransactionAdapter adapter;
    private NavigationView navview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        setup();
        navview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.income_expense){
                    startActivity(new Intent(DashboardActivity.this, TempDashboardActivity.class)); //Go back to home page
                }
                if(id==R.id.logout){
                    FirebaseAuth.getInstance().signOut();
                    Common.currentUser = null;
                    startActivity(new Intent(DashboardActivity.this, LoginActivity.class)); //Go back to home page
                    finish();

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void setup() {
//        userImage = findViewById(R.id.dashboard_profile_image);
//        userName = findViewById(R.id.dashboard_username);
        userBalance = findViewById(R.id.card_balance);
        userIncome = findViewById(R.id.card_textview_income);
        userExpense = findViewById(R.id.card_textview_expense);
        categoryRecyclerview = findViewById(R.id.dashboard_category_recyclerview);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navview= findViewById(R.id.dashboard_navigationview);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        transactionList = new ArrayList<>();
        transactionRecyclerview = findViewById(R.id.dashboard_recent_spending_recyclerview);
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

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}