package com.example.expensemanagerjava.Model;

public class CategoryItems {
    private String categoryImageUrl,categoryName;

    public CategoryItems() {
    }

    public CategoryItems(String categoryImageUrl, String categoryName) {
        this.categoryImageUrl = categoryImageUrl;
        this.categoryName = categoryName;
    }

    public String getCategoryImageUrl() {
        return categoryImageUrl;
    }

    public void setCategoryImageUrl(String categoryImageUrl) {
        this.categoryImageUrl = categoryImageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
