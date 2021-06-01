package com.csDev.pockettracker.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.csDev.pockettracker.databases.ContrastClass.TEMP_TRANS;
import androidx.annotation.Nullable;

public class CurrentTransactionDatabase extends SQLiteOpenHelper
{
    public static final int VERSION = 1;
    public static final String NAME = "POCKET_TRACKER_DB.db";
    private static final String TRANSACTION_DB =
            "CREATE TABLE " + TEMP_TRANS.TABLE_NAME +
                    "( " +  TEMP_TRANS.ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    TEMP_TRANS.DATE + " INTEGER , " +
                    TEMP_TRANS.FOR_WHAT + " TEXT , " +
                    TEMP_TRANS.PAYMENT_METHOD + " TEXT ," +
                    TEMP_TRANS.MONTH + " INTEGER ," +
                    TEMP_TRANS.AMOUNT + " INTEGER NOT NULL, " +
                    TEMP_TRANS.TYPE + " INTEGER )";

    public CurrentTransactionDatabase(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(TRANSACTION_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TEMP_TRANS.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
