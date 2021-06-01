package com.csDev.pockettracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;

import com.csDev.pockettracker.databases.ContrastClass.PERMANENT_TRANSACTION;
import com.csDev.pockettracker.adapters.TransactionAdapter;
import com.csDev.pockettracker.classes.Transaction;
import com.csDev.pockettracker.databases.ContrastClass;
import com.csDev.pockettracker.databases.CurrentTransactionDatabase;
import com.csDev.pockettracker.databases.PermanentDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class AllTransactions extends AppCompatActivity {

    private ArrayList<Transaction> transactionList;
    private PermanentDatabase permanentDatabase;
    private int month;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_transactions);
        permanentDatabase = new PermanentDatabase(this);
        Toolbar toolbar = findViewById(R.id.toolbar_all_transaction);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.all_transaction_recycler);
        transactionList = new ArrayList<>();
        Intent intent = getIntent();

        month = intent.getIntExtra("MONTH",-1);
        getSupportActionBar().setTitle(ContrastClass.getMonth(month) + " Transactions");

        if(Calendar.getInstance().get(Calendar.MONTH) == month)
            currentTransaction();
        else
            readDataBase();

        TransactionAdapter adapter = new TransactionAdapter(this, transactionList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void currentTransaction()
    {

        CurrentTransactionDatabase transactionDatabase = new CurrentTransactionDatabase(this);
        SQLiteDatabase database = transactionDatabase.getReadableDatabase();

        String[] columns = {
                ContrastClass.TEMP_TRANS.ID,
                ContrastClass.TEMP_TRANS.DATE,
                ContrastClass.TEMP_TRANS.FOR_WHAT,
                ContrastClass.TEMP_TRANS.MONTH,
                ContrastClass.TEMP_TRANS.PAYMENT_METHOD,
                ContrastClass.TEMP_TRANS.AMOUNT,
                ContrastClass.TEMP_TRANS.TYPE
        };
        Cursor cursor = database.query(ContrastClass.TEMP_TRANS.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Transaction transaction = new Transaction();
                transaction.setDATE(cursor.getString(cursor.getColumnIndex(ContrastClass.TEMP_TRANS.DATE)));
                transaction.setAMOUNT(cursor.getDouble(cursor.getColumnIndex(ContrastClass.TEMP_TRANS.AMOUNT)));
                transaction.setPAYMENT_METHOD(cursor.getString(cursor.getColumnIndex(ContrastClass.TEMP_TRANS.PAYMENT_METHOD)));
                transaction.setREASON(cursor.getString(cursor.getColumnIndex(ContrastClass.TEMP_TRANS.FOR_WHAT)));
                transaction.setStatus(cursor.getInt(cursor.getColumnIndex(ContrastClass.TEMP_TRANS.TYPE)));
                transaction.setMonthName(cursor.getInt(cursor.getColumnIndex(ContrastClass.TEMP_TRANS.MONTH)));
                transactionList.add(transaction);
            }
            cursor.close();
        }
        database.close();

    }


    private void readDataBase()
    {
        SQLiteDatabase database = permanentDatabase.getReadableDatabase();

        String[] columns = {
                PERMANENT_TRANSACTION.DATE,
                PERMANENT_TRANSACTION.FOR_WHAT,
                PERMANENT_TRANSACTION.PAYMENT_METHOD,
                PERMANENT_TRANSACTION.AMOUNT,
                PERMANENT_TRANSACTION.TYPE,
                PERMANENT_TRANSACTION.MONTH_NAME
        };

        String selection = PERMANENT_TRANSACTION.MONTH_NAME + " = ? ";
        String[] selectionArgs = { String.valueOf(month) };

        Cursor cursor = database.query(PERMANENT_TRANSACTION.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null,
                null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                transactionList.add( new Transaction(
                        cursor.getString(cursor.getColumnIndex(PERMANENT_TRANSACTION.FOR_WHAT)),
                        cursor.getString(cursor.getColumnIndex(PERMANENT_TRANSACTION.PAYMENT_METHOD)),
                        cursor.getString(cursor.getColumnIndex(PERMANENT_TRANSACTION.DATE)),
                        cursor.getDouble(cursor.getColumnIndex(PERMANENT_TRANSACTION.AMOUNT)),
                        cursor.getInt(cursor.getColumnIndex(PERMANENT_TRANSACTION.TYPE)),
                        cursor.getInt(cursor.getColumnIndex(PERMANENT_TRANSACTION.MONTH_NAME))));
            }
            cursor.close();
        }
        database.close();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}