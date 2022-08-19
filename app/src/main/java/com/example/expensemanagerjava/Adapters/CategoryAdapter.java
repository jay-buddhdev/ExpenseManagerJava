package com.example.expensemanagerjava.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanagerjava.Model.CategoryItems;
import com.example.expensemanagerjava.Model.TransacationModel;
import com.example.expensemanagerjava.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.categoryViewHolder> {

    private List<CategoryItems> CategoryItems;

    public CategoryAdapter(List<com.example.expensemanagerjava.Model.CategoryItems> categoryItems) {
        CategoryItems = categoryItems;
    }

    @NonNull
    @Override
    public categoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryAdapter.categoryViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.categoryitems_layout, parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull categoryViewHolder holder, int position) {
        holder.setCategoryData(CategoryItems.get(position));
    }

    @Override
    public int getItemCount() {
        return CategoryItems.size();
    }

    public class categoryViewHolder extends RecyclerView.ViewHolder {
        CircleImageView categoryImage;
        TextView categoryName;
        public categoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.imageview_category);
            categoryName = itemView.findViewById(R.id.textview_category);
        }
        public void setCategoryData(CategoryItems categoryItem){
            categoryName.setText(categoryItem.getCategoryName());
            Picasso.get().load(categoryItem.getCategoryImageUrl()).into(categoryImage);
        }
    }
}
