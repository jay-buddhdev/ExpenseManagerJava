package com.example.expensemanagerjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.expensemanagerjava.Adapters.OnboardingAdapter;
import com.example.expensemanagerjava.Model.OnBoardingItems;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class WalkThroughScreenActivity extends AppCompatActivity {

    private OnboardingAdapter onboardingAdapter;
    private LinearLayout layoutOnboardingIndicator;
    private MaterialButton loginBtn,signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_through_screen);
        layoutOnboardingIndicator = findViewById(R.id.layoutOnboardingIndicators);
        setOnboardingItem();
        ViewPager2 onboardingViewPager = findViewById(R.id.onBoardingViewpager);
        onboardingViewPager.setAdapter(onboardingAdapter);
        setOnboadingIndicator();
        setCurrentOnboardingIndicators(0);
        loginBtn = findViewById(R.id.signin_btn_walkthrough);
        signupBtn = findViewById(R.id.signup_btn_walkthrough);
        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicators(position);
            }
        });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(WalkThroughScreenActivity.this, SignupActivity.class);
                startActivity(signupIntent);
                finish();
            }
        });
    }

    private void setCurrentOnboardingIndicators(int index) {
        int childCount = layoutOnboardingIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutOnboardingIndicator.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive));
            }
        }
    }
    private void setOnboadingIndicator() {
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(), R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicator.addView(indicators[i]);
        }

    }

    private void setOnboardingItem() {
        List<OnBoardingItems> onBoardingItems = new ArrayList<>();
        OnBoardingItems itemlist=new OnBoardingItems();
        itemlist.setTitle("Gain total control \n" +
                "of your money");
        itemlist.setTitledescription("Become your own money manager and make every cent count");
        itemlist.setImage(R.drawable.onboarding_item_1);
        OnBoardingItems itemlist2=new OnBoardingItems();
        itemlist2.setTitle("Know where your \n" +
                "money goes");
        itemlist2.setTitledescription("Track your transaction easily,\n" +
                "with categories and financial report ");
        itemlist2.setImage(R.drawable.onboarding_item_2);

        OnBoardingItems itemlist3=new OnBoardingItems();
        itemlist3.setTitle("Planning ahead");
        itemlist3.setTitledescription("Setup your budget for each category\n" +
                "so you in control");
        itemlist3.setImage(R.drawable.onboarding_item_3);
        onBoardingItems.add(itemlist);
        onBoardingItems.add(itemlist2);
        onBoardingItems.add(itemlist3);
        onboardingAdapter=new OnboardingAdapter(onBoardingItems);
    }
}