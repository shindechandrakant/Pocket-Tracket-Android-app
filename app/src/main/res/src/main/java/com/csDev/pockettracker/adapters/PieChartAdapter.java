package com.csDev.pockettracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.csDev.pockettracker.AllTransactions;
import com.csDev.pockettracker.R;
import com.csDev.pockettracker.classes.MonthlySummery;
import com.csDev.pockettracker.databases.ContrastClass;


import java.util.ArrayList;
import java.util.List;

public class PieChartAdapter extends RecyclerView.Adapter<PieChartAdapter.PieViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<MonthlySummery> summery;
    public PieChartAdapter(Context context, ArrayList<MonthlySummery> summery)
    {
        this.summery = summery;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public PieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PieViewHolder(inflater.inflate(R.layout.summery_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PieViewHolder holder, int position)
    {
        holder.setUpGraph(summery.get(position));
    }

    @Override
    public int getItemCount() {
        return summery == null ? 0 : summery.size();
    }

    class PieViewHolder extends RecyclerView.ViewHolder
    {
        AnyChartView chartView;
        View view;
        TextView totalTransaction;
        Button viewAllTransactions;
        MonthlySummery current;
        public PieViewHolder(@NonNull View itemView)
        {
            super(itemView);
            view = itemView;
            totalTransaction = itemView.findViewById(R.id.total_transactions);
            chartView = itemView.findViewById(R.id.pia_chart);
            viewAllTransactions = itemView.findViewById(R.id.view_all_transactions);
            viewAllTransactions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AllTransactions.class);
                    intent.putExtra("MONTH",current.getMonthName());
                    context.startActivity(intent);
                }
            });
        }

        public void setUpGraph(MonthlySummery summery)
        {
            current = summery;
            Pie pie = AnyChart.pie();
            List<DataEntry> dataEntries = new ArrayList<>();
            dataEntries.add(new ValueDataEntry("Saving",summery.getSaving()));
            dataEntries.add(new ValueDataEntry("Expense",summery.getSpended()));
            totalTransaction.setText(String.valueOf(summery.getTotalTransaction()));
            pie.title(ContrastClass.getMonth(summery.getMonthName()));
            pie.data(dataEntries);
            chartView.setChart(pie);
        }




    }
}
