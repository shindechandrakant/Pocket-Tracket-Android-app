package com.csdev.propockettracker;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.csdev.propockettracker.classes.FixExpense;
import com.csdev.propockettracker.classes.Transaction;
import com.csdev.propockettracker.databases.*;
import com.csdev.propockettracker.databases.ContrastClass.*;
import com.csdev.propockettracker.fragments.FixedFragment;
import com.csdev.propockettracker.fragments.HomeFragment;
import com.csdev.propockettracker.fragments.SummeryFragment;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements DataCommunicator, RewardedVideoAdListener {

    private SQLiteDatabase database;
    ArrayList<Fragment> fragmentList;
    BottomNavigationView bottomNavigationView;
    ArrayList<Transaction> currentTransactionList;
    ArrayList<FixExpense> fixedExpenseList;
    int previousMonth;
    CurrentTransactionDatabase transactionDatabase;
    private double totalAmountLeft,totalAmountReceived,totalAmountSpend;
    Fragment selectedFragment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        initialization();
        readCurrentDatabase();
        readFixedDataBase();

        newUser();

        if(Calendar.getInstance().get(Calendar.MONTH) > previousMonth)
            upDateAppDataBase();
        else  if (previousMonth == Calendar.DECEMBER && Calendar.getInstance().get(Calendar.MONTH) == Calendar.JANUARY)
            upDateAppDataBase();


        selectedFragment = new HomeFragment();
        bottomNavigationView.setOnNavigationItemSelectedListener(listener);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();

    }


    private void upDateAppDataBase() {
        PermanentDatabase permanentDatabase = new PermanentDatabase(this);
        database = permanentDatabase.getWritableDatabase();
        int x = Calendar.getInstance().get(Calendar.MONTH) - 1;
        if (x == -1)
            x = 11;

        for (Transaction transaction : currentTransactionList) {
            ContentValues value = new ContentValues();
            value.put(PERMANENT_TRANSACTION.AMOUNT, transaction.getAMOUNT());
            value.put(PERMANENT_TRANSACTION.DATE, transaction.getDATE());
            value.put(PERMANENT_TRANSACTION.MONTH_NAME, x);
            value.put(PERMANENT_TRANSACTION.FOR_WHAT, transaction.getREASON());
            value.put(PERMANENT_TRANSACTION.TYPE, transaction.getStatus());
            value.put(PERMANENT_TRANSACTION.PAYMENT_METHOD, transaction.getPAYMENT_METHOD());
            database.insert(PERMANENT_TRANSACTION.TABLE_NAME, null, value);
        }
        database.close();

        readDashboard();

        GraphicalDataBase graphicalDataBase = new GraphicalDataBase(this);
        database = graphicalDataBase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GRAPHICAL.TOTAL_TRANSACTION, currentTransactionList.size());
        values.put(GRAPHICAL.SAVING, totalAmountLeft);
        values.put(GRAPHICAL.MONTH_NAME, x);
        values.put(GRAPHICAL.EXPENSE, totalAmountSpend);
        database.insert(GRAPHICAL.TABLE_NAME, null, values);
        database.close();

        totalAmountLeft = totalAmountReceived = totalAmountSpend = 0;
        writeDashboard();

        database = transactionDatabase.getWritableDatabase();
        database.delete(TEMP_TRANS.TABLE_NAME,null,null);
        database.close();
        currentTransactionList.clear();
    }

    private void newUser()
    {
        SharedPreferences preferences = getSharedPreferences("DASHBOARD",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        previousMonth = preferences.getInt("COUNT",-1);
        if (previousMonth == -1) {
            previousMonth = Calendar.getInstance().get(Calendar.MONTH);
            editor.putInt("COUNT", previousMonth);
        }
        editor.apply();
    }

    private void writeDashboard()
    {
        SharedPreferences preferences = getSharedPreferences("DASHBOARD",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("TOTAL",currentTransactionList.size());
        editor.putFloat("LEFT", (float) totalAmountLeft);
        editor.putFloat("RECEIVED", (float) totalAmountReceived);
        editor.putInt("COUNT", Calendar.getInstance().get(Calendar.MONTH));
        editor.putFloat("SPEND", (float) totalAmountSpend);
        editor.apply();
    }

    private void readDashboard()
    {
        SharedPreferences preferences = getSharedPreferences("DASHBOARD", Context.MODE_PRIVATE);
        totalAmountLeft = preferences.getFloat("LEFT",0);
        previousMonth = preferences.getInt("COUNT",-1);
        totalAmountReceived = preferences.getFloat("RECEIVED",0);
        totalAmountSpend = preferences.getFloat("SPEND",0);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId())
                    {
                        case R.id.home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.fixed:
                            selectedFragment = new FixedFragment();
                            break;
                        case R.id.summery:
                            selectedFragment = new SummeryFragment();
                    }
                    assert selectedFragment != null;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, selectedFragment).commit();
                    return true;
                }

            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.rateUs:
                try
                {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + getPackageName())));
                }
                catch (ActivityNotFoundException e)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/" +
                                    "store/apps/details?id=" + getPackageName())));
                }
                break;
            case R.id.share:
                Intent intentSend = new Intent();
                intentSend.setAction(Intent.ACTION_SEND);
                intentSend.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/" +
                        "store/apps/details?id=" + getPackageName());
                intentSend.setType("text/plane");
                startActivity(Intent.createChooser(intentSend,"Share"));
                break;
            case R.id.exit:
                    finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialization()
    {
        transactionDatabase = new CurrentTransactionDatabase(this);
        fixedExpenseList = new ArrayList<>();
        currentTransactionList = new ArrayList<>();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new FixedFragment());
        fragmentList.add(new SummeryFragment());
    }

    public ArrayList<FixExpense> getFixTransaction() {
        return fixedExpenseList;
    }

    public ArrayList<Transaction> getCurrentTransaction() {
        return currentTransactionList;
    }

    //  read database
    private void readCurrentDatabase()
    {
        //  transactionList.clear();
        database = transactionDatabase.getReadableDatabase();
        String[] columns = {
                TEMP_TRANS.ID,
                TEMP_TRANS.DATE,
                TEMP_TRANS.FOR_WHAT,
                TEMP_TRANS.MONTH,
                TEMP_TRANS.PAYMENT_METHOD,
                TEMP_TRANS.AMOUNT,
                TEMP_TRANS.TYPE
        };
        Cursor cursor = database.query(TEMP_TRANS.TABLE_NAME,
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
                transaction.setDATE(cursor.getString(cursor.getColumnIndex(TEMP_TRANS.DATE)));
                transaction.setAMOUNT(cursor.getDouble(cursor.getColumnIndex(TEMP_TRANS.AMOUNT)));
                transaction.setPAYMENT_METHOD(cursor.getString(cursor.getColumnIndex(TEMP_TRANS.PAYMENT_METHOD)));
                transaction.setREASON(cursor.getString(cursor.getColumnIndex(TEMP_TRANS.FOR_WHAT)));
                transaction.setStatus(cursor.getInt(cursor.getColumnIndex(TEMP_TRANS.TYPE)));
                transaction.setMonthName(cursor.getInt(cursor.getColumnIndex(TEMP_TRANS.MONTH)));
                currentTransactionList.add(transaction);
            }
            cursor.close();
        }
        database.close();
    }

    private void readFixedDataBase()
    {
        FixTransactionDatabase fixTransactionDatabase = new FixTransactionDatabase(this);
        database = fixTransactionDatabase.getReadableDatabase();
        String [] columns = {
                FIX_EXPENSE.TITLE,
                FIX_EXPENSE.AMOUNT,
                FIX_EXPENSE.DUE_DATE,
                FIX_EXPENSE.STATUS,
                FIX_EXPENSE.PAYED_DATE
        };

        Cursor cursor = database.query(FIX_EXPENSE.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        if(cursor != null)
        {
            while (cursor.moveToNext())
            {
                fixedExpenseList.add(new FixExpense(
                        cursor.getString(cursor.getColumnIndex(FIX_EXPENSE.TITLE)),
                        cursor.getDouble(cursor.getColumnIndex(FIX_EXPENSE.AMOUNT)),
                        cursor.getInt(cursor.getColumnIndex(FIX_EXPENSE.STATUS)) == 1,
                        cursor.getString(cursor.getColumnIndex(FIX_EXPENSE.DUE_DATE)),
                        cursor.getString(cursor.getColumnIndex(FIX_EXPENSE.PAYED_DATE))
                ));
            }
            cursor.close();
        }
        database.close();
    }

    //write transaction in data base
    @Override
    public void writeTransactionDatabase(Transaction transaction)
    {
        currentTransactionList.add(transaction);
        CurrentTransactionDatabase transactionDatabase = new CurrentTransactionDatabase(this);
        database = transactionDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TEMP_TRANS.DATE,transaction.getDATE());
        values.put(TEMP_TRANS.AMOUNT,transaction.getAMOUNT());
        values.put(TEMP_TRANS.PAYMENT_METHOD,transaction.getPAYMENT_METHOD());
        values.put(TEMP_TRANS.FOR_WHAT,transaction.getREASON());
        values.put(TEMP_TRANS.TYPE,transaction.getStatus());
        values.put(TEMP_TRANS.MONTH,transaction.getMonthName());
        database.insert(TEMP_TRANS.TABLE_NAME,null,values);
        database.close();
    }

    //write Expense Database
    @Override
    public void writeExpenseDataBase(FixExpense expense)
    {
        fixedExpenseList.add(expense);
        FixTransactionDatabase fixTransactionDatabase = new FixTransactionDatabase(this);
        database = fixTransactionDatabase.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(FIX_EXPENSE.TITLE,expense.getTitle());
        value.put(FIX_EXPENSE.AMOUNT,expense.getAmount());
        value.put(FIX_EXPENSE.STATUS,expense.getStatus() ? 1 : 0);
        value.put(FIX_EXPENSE.DUE_DATE,expense.getDate());
        value.put(FIX_EXPENSE.PAYED_DATE,expense.getPayedDate());
        database.insert(FIX_EXPENSE.TABLE_NAME,null,value);
        database.close();
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new SummeryFragment()).commit();

    }

    @Override
    public void onRewarded(com.google.android.gms.ads.reward.RewardItem rewardItem) {

        getSupportFragmentManager().beginTransaction().replace(R.id.container,new SummeryFragment()).commit();

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new SummeryFragment()).commit();

    }
}