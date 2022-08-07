package com.example.expensemanagerjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.simform.custombottomnavigation.SSCustomBottomNavigation;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity {

    private CircleImageView userImage;
    private TextView userName,userBalance,userIncome,userExpense;
    private RecyclerView categoryRecyclerview,spendingRecyclerview;
    private SSCustomBottomNavigation bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setup();
    }

    private void setup() {
        userImage = findViewById(R.id.dashboard_profile_image);
        userName = findViewById(R.id.dashboard_username);
        userBalance = findViewById(R.id.card_balance);
        userIncome = findViewById(R.id.card_textview_income);
        userExpense = findViewById(R.id.card_textview_expense);
        categoryRecyclerview = findViewById(R.id.dashboard_category_recyclerview);
        spendingRecyclerview = findViewById(R.id.dashboard_recent_spending_recyclerview);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }
}