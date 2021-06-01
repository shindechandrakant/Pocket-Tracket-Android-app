package com.csDev.pockettracker.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csDev.pockettracker.DataCommunicator;
import com.csDev.pockettracker.MainActivity;
import com.csDev.pockettracker.R;
import com.csDev.pockettracker.adapters.TransactionAdapter;
import com.csDev.pockettracker.classes.Transaction;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener
{
    private View view;
    private TextView amountLeft,amountReceived, amountSpend,newUser;
    private Button addIncome,addExpense;
    private RecyclerView transactionRecycler;
    private TransactionAdapter adapter;
    private double totalAmountLeft,totalAmountReceived,totalAmountSpend;
    private ArrayList<Transaction> transactionList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.home_fragment,container,false);
         initializeComponents();
         readDashboard();
         updateDashboard();

         transactionList = ((MainActivity) Objects.requireNonNull(getActivity())).getCurrentTransaction();

         addExpense.setOnClickListener(this);
         addIncome.setOnClickListener(this);


         adapter = new TransactionAdapter(getContext(),transactionList);
         transactionRecycler.setAdapter(adapter);
         transactionRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private void initializeComponents()
    {
        transactionList = new ArrayList<>();
        newUser = view.findViewById(R.id.newUser);
        transactionRecycler = view.findViewById(R.id.transaction_recycler_view);
        amountLeft = view.findViewById(R.id.tv_amount_left);
        amountReceived = view.findViewById(R.id.tv_amount_received);
        amountSpend = view.findViewById(R.id.tv_amount_spent);
        addIncome = view.findViewById(R.id.add_income);
        addExpense = view.findViewById(R.id.add_expense);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.add_income:
                addIncomeMethod();
                break;
            case R.id.add_expense:
                addExpenseMethod();
                break;
        }
    }


    // Add income
    private void addIncomeMethod()
    {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.setContentView(R.layout.add_income);
        final EditText addAmount = dialog.findViewById(R.id.et_add_income);
        final EditText incomeSource = dialog.findViewById(R.id.et_add_income_source);
        final Button addAmountButton = dialog.findViewById(R.id.button_add_income);
        addAmountButton.setEnabled(false);

        Objects.requireNonNull(dialog.getWindow())
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

        addAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                addAmountButton.setEnabled(!addAmount.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
        addAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                final double amount = Double.parseDouble(addAmount.getText().toString().trim());
                String source = incomeSource.getText().toString().trim();

                Date date = Calendar.getInstance().getTime();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MMM");
                Transaction transaction = new Transaction( source,
                        " ", day() +
                        "-" + df.format(date), amount, 1, Calendar.MONTH);

                DataCommunicator communicator = (DataCommunicator) getActivity();
                assert communicator != null;
                communicator.writeTransactionDatabase(transaction);
                adapter.notifyItemInserted(transactionList.size());
                adapter.notifyItemRangeChanged(0, transactionList.size());
                totalAmountReceived += amount;
                totalAmountLeft += amount;

                updateDashboard();
                dialog.dismiss();
            }
        });
    }

    // add Expense Dialog
    private void addExpenseMethod()
    {
        final EditText etAmount,etReason;
        final Spinner paymentOption;
        final Button addExpense;
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.setContentView(R.layout.add_expense);
        etAmount = dialog.findViewById(R.id.et_spended_income);
        etReason = dialog.findViewById(R.id.et_add_reason);
        paymentOption = dialog.findViewById(R.id.payment_method_spinner);
        addExpense = dialog.findViewById(R.id.button_add_expense);
        addExpense.setEnabled(false);
        Objects.requireNonNull(dialog.getWindow())
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                addExpense.setEnabled(!etAmount.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String payment = paymentOption.getSelectedItem().toString();
                double spendAmount = Double.parseDouble(etAmount.getText().toString().trim());
                String reason = etReason.getText().toString().trim();
                String forWhat = !reason.isEmpty() ? reason : "--";
                Date date = Calendar.getInstance().getTime();
                totalAmountSpend += spendAmount;
                totalAmountLeft -= spendAmount;
                if(totalAmountLeft < 0)
                    totalAmountLeft = 0;
                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MMM");
                Transaction transaction = new Transaction(forWhat,payment,  day() + "-" +
                        df.format(date),spendAmount,0, Calendar.MONTH);

                DataCommunicator communicator = (DataCommunicator) getActivity();
                assert communicator != null;
                communicator.writeTransactionDatabase(transaction);
                adapter.notifyItemInserted(transactionList.size());
                adapter.notifyItemRangeChanged(0,transactionList.size());
                updateDashboard();

                dialog.dismiss();
            }
        });
    }

    private void updateDashboard()
    {
        boolean flag = totalAmountReceived == 0 && totalAmountSpend == 0;
        newUser.setVisibility(flag ? View.VISIBLE : View.GONE);
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        Locale locale = new Locale("en","IN");
        nf.setCurrency(Currency.getInstance(locale));
        amountLeft.setText(nf.format(totalAmountLeft));
        amountReceived.setText(nf.format(totalAmountReceived));
        amountSpend.setText(nf.format(totalAmountSpend));
    }

    private void readDashboard()
    {
        SharedPreferences preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("DASHBOARD", Context.MODE_PRIVATE);
        totalAmountLeft = preferences.getFloat("LEFT",0);
        totalAmountReceived = preferences.getFloat("RECEIVED",0);
        totalAmountSpend = preferences.getFloat("SPEND",0);
    }

    @Override
    public void onPause() {
        writeDashboard();
        super.onPause();
    }

    private void writeDashboard()
    {
        SharedPreferences preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("DASHBOARD",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("TOTAL",transactionList.size());
        editor.putFloat("LEFT", (float) totalAmountLeft);
        editor.putFloat("RECEIVED", (float) totalAmountReceived);
        editor.putFloat("SPEND", (float) totalAmountSpend);
        editor.apply();
    }

    // day
    String day()
    {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        switch (day)
        {
            case Calendar.SUNDAY:
                return "Sun";
            case Calendar.MONDAY:
                return "Mon";
            case Calendar.TUESDAY:
                return "Tue";
            case Calendar.WEDNESDAY:
                return "Wed";
            case Calendar.THURSDAY:
                return "Thur";
            case Calendar.FRIDAY:
                return "Fri";
            default:
                return "-";
        }
    }
}
