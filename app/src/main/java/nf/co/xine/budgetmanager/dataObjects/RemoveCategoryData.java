package nf.co.xine.budgetmanager.dataObjects;

import android.content.Context;

import java.util.ArrayList;

import layout.CategoriesFragment;

public class RemoveCategoryData {

    private int position;
    private Context context;
    private ArrayList<String> categoryList;
    private ArrayList<Transaction> transactions;
    private CategoriesFragment categoriesFragment;

    public RemoveCategoryData(int position, Context context, ArrayList<String> categoryList, ArrayList<Transaction> transactions, CategoriesFragment categoriesFragment) {
        this.position = position;
        this.context = context;
        this.categoryList = categoryList;
        this.transactions = transactions;
        this.categoriesFragment = categoriesFragment;
    }

    public ArrayList<String> getCategoryList() {
        return categoryList;
    }

    public Context getContext() {
        return context;
    }

    public int getPosition() {
        return position;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public CategoriesFragment getCategoriesFragment() {
        return categoriesFragment;
    }
}
