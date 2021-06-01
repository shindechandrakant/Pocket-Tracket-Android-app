package com.csDev.pockettracker.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csDev.pockettracker.DataCommunicator;
import com.csDev.pockettracker.MainActivity;
import com.csDev.pockettracker.R;
import com.csDev.pockettracker.adapters.FixExpenseAdapter;
import com.csDev.pockettracker.classes.FixExpense;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class FixedFragment extends Fragment
{
    RecyclerView recyclerView;
    private FixExpenseAdapter adapter;
    private ArrayList<FixExpense> expenseList;
    TextView newUser;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fixed_fragment,container,false);
        recyclerView = view.findViewById(R.id.fix_expense_recycler_view);
        FloatingActionButton addNewItem = view.findViewById(R.id.add_fix_income_fab);
        CoordinatorLayout layout = view.findViewById(R.id.co_layout);
        newUser = view.findViewById(R.id.newUser1);
        expenseList = new ArrayList<>();
        expenseList = ((MainActivity) Objects.requireNonNull(getActivity())).getFixTransaction();

        newUser.setVisibility(expenseList.isEmpty() ? View.VISIBLE : View.GONE);
        adapter = new FixExpenseAdapter(getContext(),expenseList, layout);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewExpense();
            }
        });
        return view;
    }

    private void addNewExpense()
    {
        final Button selectDate,addNewItem;
        final EditText title,amount;
        final DatePickerDialog[] datePickerDialog = new DatePickerDialog[1];
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.setContentView(R.layout.fix_expense_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
        final String[] selectedDate = new String[1];
        selectDate = dialog.findViewById(R.id.fx_select_date);
        addNewItem = dialog.findViewById(R.id.fx_add_item);
        title = dialog.findViewById(R.id.fx_tv_title);
        amount = dialog.findViewById(R.id.fx_tv_amount);

        addNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemTitle = title.getText().toString().trim();
                String strAmount = amount.getText().toString().trim();

                if(!itemTitle.isEmpty() && !strAmount.isEmpty()) {
                    float itemAmount = Float.parseFloat(strAmount);
                    FixExpense item = new FixExpense(itemTitle, itemAmount, false, selectedDate[0], "--");

                    DataCommunicator communicator = (DataCommunicator) getActivity();

                    assert communicator != null;
                    communicator.writeExpenseDataBase(item);

                    newUser.setVisibility(expenseList.isEmpty() ? View.VISIBLE : View.GONE);
                    adapter.notifyItemInserted(expenseList.size());
                    adapter.notifyItemRangeChanged(0, expenseList.size());
                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(getContext(),"Fields should not empty",Toast.LENGTH_LONG).show();
                }
            }
        });
        selectDate.setOnClickListener(new View.OnClickListener() {
            final Calendar calendar = Calendar.getInstance();
            int date1 = calendar.get(Calendar.DATE);
            int month1 = calendar.get(Calendar.MONTH);
            int year1 = calendar.get(Calendar.YEAR);
            @Override
            public void onClick(View v)
            {
                datePickerDialog[0] = new DatePickerDialog(Objects.requireNonNull(getContext()),R.style.DialogTheme ,new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                    {
                        month1 = month + 1;
                        date1 = dayOfMonth;
                        selectedDate[0] = dayOfMonth + " / " + month1;
                        selectDate.setText(selectedDate[0]);
                    }
                },year1,month1,date1);
                datePickerDialog[0].show();
            }
        });
    }
}