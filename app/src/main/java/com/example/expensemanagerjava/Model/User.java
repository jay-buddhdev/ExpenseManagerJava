package com.example.expensemanagerjava.Model;

public class User {
    private String name, email, UID;
    private Long balance;
    private Long TotalExpense,TotalIncome;

    public User() {

    }

    public User(String name, String email, String UID, Long balance, Long totalExpense, Long totalIncome) {
        this.name = name;
        this.email = email;
        this.UID = UID;
        this.balance = balance;
        TotalExpense = totalExpense;
        TotalIncome = totalIncome;
    }

    public Long getTotalExpense() {
        return TotalExpense;
    }

    public void setTotalExpense(Long totalExpense) {
        TotalExpense = totalExpense;
    }

    public Long getTotalIncome() {
        return TotalIncome;
    }

    public void setTotalIncome(Long totalIncome) {
        TotalIncome = totalIncome;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
