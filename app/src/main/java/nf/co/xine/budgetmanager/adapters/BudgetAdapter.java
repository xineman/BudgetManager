package nf.co.xine.budgetmanager.adapters;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nf.co.xine.budgetmanager.PercentFormatter;
import nf.co.xine.budgetmanager.R;
import nf.co.xine.budgetmanager.dataObjects.Budget;
import nf.co.xine.budgetmanager.dataObjects.Transaction;

public class BudgetAdapter extends ArrayAdapter<Budget> {
    public BudgetAdapter(Context context, ArrayList<Budget> budgets, ArrayList<Transaction> transactions, ArrayList<String> categories) {
        super(context, 0);
        this.budgets = budgets;
        this.transactions = transactions;
        this.context = context;
        this.categories = categories;
        periods = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.period_array)));
    }

    private ArrayList<Budget> budgets;
    private ArrayList<Transaction> transactions;
    private ArrayList<String> categories;
    private Context context;
    private ArrayList<String> periods;

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.budget_row, parent, false);
        }

        HorizontalBarChart chart = (HorizontalBarChart) convertView.findViewById(R.id.chart);

        TextView category = (TextView) convertView.findViewById(R.id.budget_category);
        TextView period = (TextView) convertView.findViewById(R.id.budget_period);
        TextView value = (TextView) convertView.findViewById(R.id.budget_value);
        TextView currentValue = (TextView) convertView.findViewById(R.id.budget_current_value);
        category.setText(categories.get(getItem(position).getCategoryId()));
        period.setText(periods.get(getItem(position).getPeriod()));
        value.setText(String.valueOf(getItem(position).getValue()));
        double total = 0;
        // get today and clear time of day
        Calendar cal = Calendar.getInstance();
        System.out.println("Start of this week:       " + cal.getTime());
        switch (getItem(position).getPeriod()) {
            case 0: {
                cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
                cal.clear(Calendar.MINUTE);
                cal.clear(Calendar.SECOND);
                cal.clear(Calendar.MILLISECOND);

// get start of this week in milliseconds
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                break;
            }
            case 1: {
                cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
                cal.clear(Calendar.MINUTE);
                cal.clear(Calendar.SECOND);
                cal.clear(Calendar.MILLISECOND);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                break;
            }
            case 2: {
                cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
                cal.clear(Calendar.MINUTE);
                cal.clear(Calendar.SECOND);
                cal.clear(Calendar.MILLISECOND);
                cal.set(Calendar.DAY_OF_YEAR, 1);
                break;
            }
        }
        for (Transaction tr :
                transactions) {
            if (tr.getCategory() == getItem(position).getCategoryId() && tr.getDate().compareTo(cal.getTime()) > 0)
                total += tr.getValue() * -1;
        }
        currentValue.setText(String.valueOf(total));
        BigDecimal number = new BigDecimal(total / getItem(position).getValue()*100);
        float budgetProgress = number.floatValue();
        List<BarEntry> entries = new ArrayList<BarEntry>();
        entries.add(new BarEntry(0f, budgetProgress));
        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        BarData data = new BarData(set);
        data.setBarWidth(10f); // set custom bar width
        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(false);
        YAxis right = chart.getAxisRight();
        right.setEnabled(false);
        YAxis left = chart.getAxisLeft();
        left.setAxisMinimum(0f);
        left.setValueFormatter(new PercentFormatter());
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);
        set.setColor(Color.RED);
        if (budgetProgress<100f) {
            set.setColor(Color.GREEN);
            left.setAxisMaximum(100f);
        }
        chart.setDrawGridBackground(false);
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate();
        return convertView;
    }
}
