package com.csDev.pockettracker.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.csDev.pockettracker.databases.ContrastClass.GRAPHICAL;

public class GraphicalDataBase extends SQLiteOpenHelper
{
    private static final String NAME = "Graphical.db";
    private static final int VERSION = 1;
    private static final String SCHEMA = " CREATE TABLE "
                + GRAPHICAL.TABLE_NAME + " ( "
                + GRAPHICAL.ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + GRAPHICAL.MONTH_NAME + " INTEGER ,"
                + GRAPHICAL.TOTAL_TRANSACTION + " INTEGER ,"
                + GRAPHICAL.SAVING + " DOUBLE, "
                + GRAPHICAL.EXPENSE + " DOUBLE "
                + " );";

    public GraphicalDataBase(@Nullable Context context) {
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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GRAPHICAL.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
