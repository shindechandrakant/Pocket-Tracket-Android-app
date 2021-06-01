package com.csDev.pockettracker.adapters;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.csDev.pockettracker.R;
import com.csDev.pockettracker.classes.FixExpense;
import com.csDev.pockettracker.databases.ContrastClass;
import com.csDev.pockettracker.databases.FixTransactionDatabase;
import com.google.android.material.snackbar.Snackbar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class FixExpenseAdapter extends RecyclerView.Adapter<FixExpenseAdapter.MyViewHolder> {
    Context context;
    private LayoutInflater inflater;
    ArrayList<FixExpense> expenseList;
    CoordinatorLayout coordinator;
    TextView newUser;

    public FixExpenseAdapter(Context context, ArrayList<FixExpense> expenseList, CoordinatorLayout coordinator) {
        this.context = context;
        this.coordinator = coordinator;
        this.expenseList = expenseList;
        inflater = LayoutInflater.from(context);
        newUser = coordinator.findViewById(R.id.newUser1);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.fixed_expenses_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setData(expenseList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return expenseList == null ? 0 : expenseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView amount, title, status, dueDate, payedDate;
        CheckBox markAsPayed;
        int position;
        CardView cardView;
        FixExpense current;
        ImageView deleteItem;
        NumberFormat numberFormat;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.fx_title);
            amount = itemView.findViewById(R.id.fx_amount);
            status = itemView.findViewById(R.id.fx_status);
            cardView = itemView.findViewById(R.id.fix_card);
            dueDate = itemView.findViewById(R.id.fx_due_amount);
            payedDate = itemView.findViewById(R.id.fx_payed_date);
            markAsPayed = itemView.findViewById(R.id.fx_check_box);
            deleteItem = itemView.findViewById(R.id.fx_delete);
            Locale locale = new Locale("en", "IN");
            numberFormat = NumberFormat.getCurrencyInstance(locale);

            markAsPayed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (compoundButton.isChecked()) {
                        status.setText("Payed");
                        current.setStatus(true);
                        String date = getPayedDate();
                        current.setPayedDate(date);
                        payedDate.setText(date);

            //          Toast.makeText(context,"Marked As Payed for this month",Toast.LENGTH_LONG).show();

                        cardView.setBackgroundColor(Color.parseColor("#B1F694"));

                        // call update and delete func here
                        updateAndDelete(2);
                    }
                }
            });

            deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    expenseList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, expenseList.size());
                    newUser.setVisibility(expenseList.isEmpty() ? View.VISIBLE : View.GONE);
                    final UndoSnackBar undo = new UndoSnackBar(position, current);
                    Snackbar snackbar = Snackbar.make(coordinator, "Item removed", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Undo", undo);
                    snackbar.setActionTextColor(Color.WHITE);
                    snackbar.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!undo.isUndo) {
                                // call Update And Delete func here
                                updateAndDelete(1);
                            }
                        }
                    }, 3000);
                }
            });
        }

        @SuppressLint("ResourceAsColor")
        public void setData(FixExpense expense, int position) {
            this.position = position;
            current = expense;
            title.setText(expense.getTitle());

            amount.setText(numberFormat.format(expense.getAmount()));
            status.setText(expense.getStatus() ? "Payed" : "Pending");
            dueDate.setText(expense.getDate());
            payedDate.setText(expense.getPayedDate());
            cardView.setBackgroundColor(expense.getStatus() ? Color.parseColor("#B1F694") : Color.parseColor("#A5B2F4"));
            markAsPayed.setChecked(expense.getStatus());
        } //setData

        private String getPayedDate() {
            String dateStr;
            Calendar calendar = Calendar.getInstance();
            int date = calendar.get(Calendar.DATE);
            int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            dateStr = date + " / " + month;
            return dateStr;
        }

        private void updateAndDelete(int operationCode) {
            FixTransactionDatabase database = new FixTransactionDatabase(context);
            SQLiteDatabase sql = database.getWritableDatabase();
            String where;
            String[] selection;
            //delete
            if (operationCode == 1) {
                where = ContrastClass.FIX_EXPENSE.TITLE + " = ? ";
                selection = new String[]{current.getTitle()};
                sql.delete(ContrastClass.FIX_EXPENSE.TABLE_NAME, where, selection);

            }//update
            else if (operationCode == 2) {
                where = ContrastClass.FIX_EXPENSE.TITLE + " = ? ";
                selection = new String[]{current.getTitle()};

                ContentValues value = new ContentValues();
                value.put(ContrastClass.FIX_EXPENSE.PAYED_DATE, current.getPayedDate());
                value.put(ContrastClass.FIX_EXPENSE.STATUS, 1);

                sql.update(ContrastClass.FIX_EXPENSE.TABLE_NAME, value, where, selection);
            }
            sql.close();
        }
    }

    class UndoSnackBar implements View.OnClickListener {
        boolean isUndo;
        int position;
        FixExpense expense;

        public UndoSnackBar(int position, FixExpense expense) {
            this.position = position;
            this.expense = expense;
            isUndo = false;
        }

        @Override
        public void onClick(View view) {
            expenseList.add(this.expense);
            notifyItemInserted(this.position);
            notifyItemRangeChanged(this.position, expenseList.size());
            newUser.setVisibility(expenseList.isEmpty() ? View.VISIBLE : View.GONE);
            isUndo = true;
        }
    }//undoSnackBar -> Delete
}

