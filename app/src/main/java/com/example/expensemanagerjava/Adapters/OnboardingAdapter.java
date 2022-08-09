package com.example.expensemanagerjava.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanagerjava.Model.OnBoardingItems;
import com.example.expensemanagerjava.R;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {
    private List<OnBoardingItems> onBoardingItems;
    public OnboardingAdapter(List<OnBoardingItems> onBoardingItems) {
        this.onBoardingItems = onBoardingItems;
    }
    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_onboarding, parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.setOnBoardingData(onBoardingItems.get(position));

    }

    @Override
    public int getItemCount() {
        return onBoardingItems.size();

    }

    public class OnboardingViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;
        private TextView textTitleDescription;
        private ImageView imageOnboarding;


        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle=itemView.findViewById(R.id.onboardingitemTitle);
            textTitleDescription=itemView.findViewById(R.id.onboardingitemTitledes);
            imageOnboarding=itemView.findViewById(R.id.imageOnboarding);

        }

        public void setOnBoardingData(OnBoardingItems onBoardingItem) {
            textTitle.setText(onBoardingItem.getTitle());
            textTitleDescription.setText(onBoardingItem.getTitledescription());
            imageOnboarding.setImageResource(onBoardingItem.getImage());
        }
    }

}
