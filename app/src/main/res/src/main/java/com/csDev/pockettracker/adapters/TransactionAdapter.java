package com.csDev.pockettracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.csDev.pockettracker.R;
import com.csDev.pockettracker.classes.Transaction;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.viewHolder> {

    public Context context;
    LayoutInflater layoutInflater;
    ArrayList<Transaction> transactionList;
   public TransactionAdapter(Context context, ArrayList<Transaction> transactionList)
   {
       this.context = context;
       this.transactionList = transactionList;
       layoutInflater = LayoutInflater.from(context);
   }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.transaction_recycler_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.setData(transactionList.get(position),position);
    }

    @Override
    public int getItemCount() {
        return transactionList == null ? 0 : transactionList.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder
    {
        TextView reason,paymentOption,date,amount;
        View view;
        int position;
        Transaction currentTransaction;
        public viewHolder(@NonNull View itemView)
        {
            super(itemView);
            view = itemView;
            date = itemView.findViewById(R.id.tv_date);
            amount = itemView.findViewById(R.id.tv_amount);
            paymentOption = itemView.findViewById(R.id.tv_payment_method);
            reason = itemView.findViewById(R.id.tv_reason);
        }
        public void setData(Transaction transaction,int position)
        {
            currentTransaction = transaction;
            this.position = position;
            date.setText(transaction.getDATE());
            String money;
            NumberFormat nf = NumberFormat.getCurrencyInstance();
            Locale locale = new Locale("en","IN");
            nf.setCurrency(Currency.getInstance(locale));
            if(transaction.getStatus() == 1)
            {
                amount.setTextColor(ContextCompat.getColor(context, R.color.received));
                money = " + " +  nf.format(transaction.getAMOUNT());
            }
            else {
                amount.setTextColor(ContextCompat.getColor(context,R.color.send));
                money =" - " +  nf.format(transaction.getAMOUNT());
            }
            amount.setText(money);
            paymentOption.setText(transaction.getPAYMENT_METHOD());
            reason.setText(transaction.getREASON());
        }
    }
}
