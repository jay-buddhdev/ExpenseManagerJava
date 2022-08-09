package com.example.expensemanagerjava.Model;

public class TransacationModel {

    private String name,category,amount,date;
    private Boolean transactionType; // 1 income and 0 expense
    public TransacationModel() {
    }

    public TransacationModel(String name, String category, String amount, String date, Boolean transactionType) {
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.transactionType = transactionType;
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

    public Boolean getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Boolean transactionType) {
        this.transactionType = transactionType;
    }
}
