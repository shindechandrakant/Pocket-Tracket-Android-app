package com.csdev.propockettracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csdev.propockettracker.R;
import com.csdev.propockettracker.adapters.PieChartAdapter;
import com.csdev.propockettracker.classes.MonthlySummery;
import com.csdev.propockettracker.databases.ContrastClass;
import com.csdev.propockettracker.databases.GraphicalDataBase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class SummeryFragment extends Fragment
{
    ArrayList<MonthlySummery> monthlySummeryList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.summery_fragment,container,false);
        monthlySummeryList = new ArrayList<>();

        readMonthlyDataBase();
        RecyclerView pieRecyclerView = view.findViewById(R.id.pie_recycler_view);
        PieChartAdapter pieChartAdapter = new PieChartAdapter(getContext(), monthlySummeryList);
        pieRecyclerView.setAdapter(pieChartAdapter);
        pieRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    private void readMonthlyDataBase()
    {
        readCurrentMonthSummery();
        GraphicalDataBase graphicalDataBase = new GraphicalDataBase(getContext());
        SQLiteDatabase database = graphicalDataBase.getReadableDatabase();
        String[] columns = {
                ContrastClass.GRAPHICAL.ID,
                ContrastClass.GRAPHICAL.MONTH_NAME,
                ContrastClass.GRAPHICAL.TOTAL_TRANSACTION,
                ContrastClass.GRAPHICAL.SAVING,
                ContrastClass.GRAPHICAL.EXPENSE
        };

        Cursor cursor = database.query(ContrastClass.GRAPHICAL.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null);

        if(cursor != null)
        {
            while (cursor.moveToNext())
            {
                monthlySummeryList.add(new MonthlySummery(
                        cursor.getInt(cursor.getColumnIndex(ContrastClass.GRAPHICAL.TOTAL_TRANSACTION)),
                        cursor.getDouble(cursor.getColumnIndex(ContrastClass.GRAPHICAL.SAVING)),
                        cursor.getDouble(cursor.getColumnIndex(ContrastClass.GRAPHICAL.EXPENSE)),
                        cursor.getInt(cursor.getColumnIndex(ContrastClass.GRAPHICAL.MONTH_NAME))
                ));
            }
            cursor.close();
        }
        database.close();
    }

    private void readCurrentMonthSummery()
    {
        SharedPreferences preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("DASHBOARD", Context.MODE_PRIVATE);

        MonthlySummery summery = new MonthlySummery(preferences.getInt("TOTAL",0),
                preferences.getFloat("LEFT",0),
                preferences.getFloat("SPEND",0),
                Calendar.getInstance().get(Calendar.MONTH) );

        monthlySummeryList.add(summery);
    }

}