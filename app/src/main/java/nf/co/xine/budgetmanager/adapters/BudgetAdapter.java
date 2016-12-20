package nf.co.xine.budgetmanager.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

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
                break;
            }
        }
        for (Transaction tr :
                transactions) {
            if (tr.getCategory() == getItem(position).getCategoryId() && tr.getDate().compareTo(cal.getTime()) > 0)
                total += tr.getValue() * -1;
        }
        currentValue.setText(String.valueOf(total));
        return convertView;
    }
}
