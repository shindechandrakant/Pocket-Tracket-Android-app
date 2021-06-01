package com.csDev.pockettracker.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import com.csDev.pockettracker.databases.ContrastClass.FIX_EXPENSE;


public class FixTransactionDatabase extends SQLiteOpenHelper
{
    private static final String NAME = "FIX_TRANS.db";
    private static final int VERSION = 1;
    private static final String SCHEMA =
                    "CREATE TABLE " + FIX_EXPENSE.TABLE_NAME + "( "
                    + FIX_EXPENSE.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + FIX_EXPENSE.TITLE + " TEXT, "
                    + FIX_EXPENSE.AMOUNT + " DOUBLE , "
                    + FIX_EXPENSE.STATUS + " INTEGER , "
                    + FIX_EXPENSE.DUE_DATE + " TEXT , "
                    + FIX_EXPENSE.PAYED_DATE + " TEXT "
                    + ");";

    public FixTransactionDatabase(@Nullable Context context) {
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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FIX_EXPENSE.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
