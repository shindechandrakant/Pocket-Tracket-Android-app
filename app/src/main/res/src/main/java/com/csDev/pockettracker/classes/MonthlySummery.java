package com.csDev.pockettracker.classes;

public class MonthlySummery
{
    private int totalTransaction;
    private double saving;
    private double spended;
    private int monthName;

    public int getMonthName() {
        return monthName;
    }

    public void setMonthName(int monthName) {
        this.monthName = monthName;
    }

    public MonthlySummery(int totalTransaction, double saving, double spended, int monthName) {
        this.totalTransaction = totalTransaction;
        this.saving = saving;
        this.spended = spended;
       // this.totalAmount = totalAmount;
        this.monthName = monthName;
    }

    public int getTotalTransaction() {
        return totalTransaction;
    }

    public void setTotalTransaction(int totalTransaction) {
        this.totalTransaction = totalTransaction;
    }

    public double getSaving() {
        return saving;
    }

    public void setSaving(float saving) {
        this.saving = saving;
    }

    public double getSpended() {
        return spended;
    }

    public void setSpended(float spended) {
        this.spended = spended;
    }

}
