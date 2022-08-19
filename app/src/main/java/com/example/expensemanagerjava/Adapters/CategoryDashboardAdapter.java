package com.example.expensemanagerjava.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanagerjava.Model.CategoryItems;
import com.example.expensemanagerjava.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryDashboardAdapter extends RecyclerView.Adapter<CategoryDashboardAdapter.categoryDashboardViewHolder>{
    private List<CategoryItems> CategoryItems;

    public CategoryDashboardAdapter(List<com.example.expensemanagerjava.Model.CategoryItems> categoryItems) {
        CategoryItems = categoryItems;
    }
    @NonNull
    @Override
    public categoryDashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryDashboardAdapter.categoryDashboardViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull categoryDashboardViewHolder holder, int position) {
        holder.setCategoryData(CategoryItems.get(position));
    }

    @Override
    public int getItemCount() {
        return CategoryItems.size();
    }

    public class categoryDashboardViewHolder extends RecyclerView.ViewHolder {
        CircleImageView categoryImage;
        TextView categoryName;
        public categoryDashboardViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.imageview_category);
            categoryName = itemView.findViewById(R.id.textview_category);
        }

        public void setCategoryData(CategoryItems categoryItems) {
            categoryName.setText(categoryItems.getCategoryName());
            if(categoryItems.getCategoryImageUrl().isEmpty()){
                categoryImage.setImageResource(R.drawable.ic_baseline_category_24);
            }else{
                Picasso.get().load(categoryItems.getCategoryImageUrl()).into(categoryImage);
            }
        }
    }
}
