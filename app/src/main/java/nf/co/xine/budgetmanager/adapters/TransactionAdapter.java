package nf.co.xine.budgetmanager.adapters;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nf.co.xine.budgetmanager.MainActivity;
import nf.co.xine.budgetmanager.R;
import nf.co.xine.budgetmanager.dataObjects.Transaction;

public class TransactionAdapter extends ArrayAdapter {
    public TransactionAdapter(Context context, ArrayList<String> categories, String currency) {
        super(context, 0);
        this.context = context;
        this.categories = categories;
        this.currency = " " + currency.toUpperCase();
    }

    private Context context;
    private ArrayList<String> categories;
    private String currency;

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.transactions_row, parent, false);
        }
        Transaction transaction = (Transaction) getItem(position);
        TextView name = (TextView) convertView.findViewById(R.id.transaction_name);
        TextView category = (TextView) convertView.findViewById(R.id.transaction_category);
        TextView date = (TextView) convertView.findViewById(R.id.transaction_date);
        TextView value = (TextView) convertView.findViewById(R.id.transaction_value);

        String dateStr = MainActivity.DATE_TIME_FORMAT.format(transaction.getDate());
        name.setText(transaction.getName());
        category.setText(categories.get(transaction.getCategory()));
        date.setText(dateStr);
        if (transaction.getValue() > 0) {
            value.setText("+" + String.valueOf(transaction.getValue()) + currency);
            value.setTextColor(Color.GREEN);
        } else {
            value.setText(String.valueOf(transaction.getValue()) + currency);
            value.setTextColor(Color.RED);
        }
        return convertView;
    }
}
