package com.csDev.pockettracker.classes;

public class FixExpense
{
    private String title;
    private double amount;
    private boolean status;
    private String date;
    private String payedDate;

    public FixExpense(String title, double amount, boolean status, String date, String payedDate) {
        this.title = title;
        this.amount = amount;
        this.status = status;
        this.date = date;
        this.payedDate = payedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPayedDate() {
        return payedDate;
    }

    public void setPayedDate(String payedDate) {
        this.payedDate = payedDate;
    }
}
