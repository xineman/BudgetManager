package nf.co.xine.budgetmanager.listeners;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuAdapter;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import layout.CategoriesFragment;
import nf.co.xine.budgetmanager.MainActivity;
import nf.co.xine.budgetmanager.adapters.CategoryAdapter;
import nf.co.xine.budgetmanager.dataObjects.RemoveCategoryData;
import nf.co.xine.budgetmanager.dataObjects.Transaction;

public class RemoveCategoryOnClickListener implements View.OnClickListener {
    private int position;
    private MainActivity context;
    private ArrayList<Transaction> transactions;
    private ArrayList<String> categories;
    private CategoriesFragment categoriesFragment;

    public RemoveCategoryOnClickListener(RemoveCategoryData data) {
        this.position = data.getPosition();
        this.context = (MainActivity) data.getContext();
        this.categories = data.getCategoryList();
        this.transactions = data.getTransactions();
        this.categoriesFragment = data.getCategoriesFragment();
    }

    @Override
    public void onClick(View v) {
        if (position == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("You cannot delete this category!");
            builder.setPositiveButton("OK", null);
            builder.create().show();
        } else {
            Log.d("Clicked", String.valueOf(position));
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Are you sure?");
            builder.setMessage(String.valueOf(context.getTransactionCount(position)) + " transactions in category \"" + categories.get(position) + "\", remove anyway?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (Transaction tr :
                            transactions) {
                        if (tr.getCategory() == position) {
                            tr.setDafaultCategory();
                        }
                    }
                    categories.remove(position);
                    categoriesFragment.invalidate();
                    context.new SaveTransactionsToDb().execute(context);
                    context.new SaveCategoriesToDb().execute(context);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();
        }
    }
}