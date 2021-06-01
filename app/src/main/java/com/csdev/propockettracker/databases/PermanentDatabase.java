package com.csdev.propockettracker.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.csdev.propockettracker.databases.ContrastClass.PERMANENT_TRANSACTION;

import androidx.annotation.Nullable;

public class PermanentDatabase extends SQLiteOpenHelper
{
    private static final String NAME = "Permanent.db";
    private static final int VERSION = 1;
    private static final String SCHEMA = " CREATE TABLE "
                + PERMANENT_TRANSACTION.TABLE_NAME
                + "( "
                + PERMANENT_TRANSACTION.ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + PERMANENT_TRANSACTION.MONTH_NAME + " INTEGER ,"
                + PERMANENT_TRANSACTION.DATE + " INTEGER , "
                + PERMANENT_TRANSACTION.FOR_WHAT + " TEXT , "
                + PERMANENT_TRANSACTION.PAYMENT_METHOD + " TEXT ,"
                + PERMANENT_TRANSACTION.AMOUNT + " DOUBLE NOT NULL, "
                + PERMANENT_TRANSACTION.TYPE + " INTEGER "
                + " );";

    public PermanentDatabase(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(SCHEMA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PERMANENT_TRANSACTION.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}