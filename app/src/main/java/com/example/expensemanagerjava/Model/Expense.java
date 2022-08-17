package com.example.expensemanagerjava.Model;

public class Expense {
    private String name,category,amount,date,imageUrl;


    public Expense(String name, String category, String amount, String date, String imageUrl) {
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    public Expense() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
