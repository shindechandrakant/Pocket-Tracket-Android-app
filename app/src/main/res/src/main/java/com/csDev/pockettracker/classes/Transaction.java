package com.csDev.pockettracker.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Transaction implements Parcelable {

    private int ID;
    private String REASON;
    private String PAYMENT_METHOD;
    private String DATE;
    private double AMOUNT;
    private int status;
    private int monthName;

    public Transaction() { }


    public Transaction( String REASON, String PAYMENT_METHOD, String DATE, double AMOUNT, int status, int monthName) {
        this.REASON = REASON;
        this.PAYMENT_METHOD = PAYMENT_METHOD;
        this.DATE = DATE;
        this.AMOUNT = AMOUNT;
        this.status = status;
        this.monthName = monthName;
    }


    protected Transaction(Parcel in, int monthName) {
        ID = in.readInt();
        REASON = in.readString();
        PAYMENT_METHOD = in.readString();
        DATE = in.readString();
        AMOUNT = in.readDouble();
        status = in.readInt();
        this.monthName = monthName;
    }

    protected Transaction(Parcel in) {
        ID = in.readInt();
        REASON = in.readString();
        PAYMENT_METHOD = in.readString();
        DATE = in.readString();
        AMOUNT = in.readDouble();
        status = in.readInt();
        monthName = in.readInt();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };


    public int getMonthName() {
        return monthName;
    }

    public void setMonthName(int monthName) {
        this.monthName = monthName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getREASON() {
        return REASON;
    }

    public void setREASON(String REASON) {
        this.REASON = REASON;
    }

    public String getPAYMENT_METHOD() {
        return PAYMENT_METHOD;
    }

    public void setPAYMENT_METHOD(String PAYMENT_METHOD) {
        this.PAYMENT_METHOD = PAYMENT_METHOD;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public double getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(double AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int TYPE) {
        this.status = TYPE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(ID);
        parcel.writeString(REASON);
        parcel.writeString(PAYMENT_METHOD);
        parcel.writeString(DATE);
        parcel.writeDouble(AMOUNT);
        parcel.writeInt(status);
        parcel.writeInt(monthName);
    }
}
