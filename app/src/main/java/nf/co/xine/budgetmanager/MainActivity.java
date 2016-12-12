package nf.co.xine.budgetmanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import layout.AccountsFragment;
import layout.BudgetFragment;
import layout.NewAccountFragment;
import layout.NewTransactionFragment;
import layout.TransactionsFragment;
import nf.co.xine.budgetmanager.dataObjects.Account;
import nf.co.xine.budgetmanager.dataObjects.Transaction;

public class MainActivity extends AppCompatActivity implements AccountsFragment.AccountsFragListener,
        BudgetFragment.BudgetFragListener, NewAccountFragment.OnNewAccountListener, TransactionsFragment.TransactionFragListener, NewTransactionFragment.NewTransactionListener {

    private ArrayList<Account> accountList = new ArrayList<Account>();
    private ArrayList<Transaction> transactionList = new ArrayList<>();
    private ArrayList<String> categoryList = new ArrayList<>();
    private boolean editMode = false;
    private Fragment currentFragment;
    private NewAccountFragment newAccountFragment;
    private NewTransactionFragment newTransactionFragment;
    private int currentAccount;
    private int transactionToEdit = -1;
    private int accountToEdit = -1;//account to edit, -1 if creating new account
    public final static SimpleDateFormat DATE_TIME_FORMAT =
            new SimpleDateFormat("EEE, d MMM yyyy @ HH:mm:ss", Locale.getDefault());
    public final static SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Accounts");
        new GetAccountsFromDb().execute(this);
        //fillRandomly();
        currentFragment = AccountsFragment.newInstance();
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_accounts) {
                    Log.d("accountsList size", String.valueOf(accountList.size()));
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frag_container, currentFragment).commit();
                    Log.d("Frag", "Accounts");
                }
                if (tabId == R.id.tab_budget) {
                    //currentFragment = BudgetFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, currentFragment).commit();
                    Log.d("Frag", "Budget");
                }
                if (tabId == R.id.tab_charts) {

                }
            }
        });
        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        currentFragment = getSupportFragmentManager().findFragmentById(R.id.frag_container);
                        if (currentFragment instanceof AccountsFragment) {
                            getSupportActionBar().setTitle("Accounts");
                            ((AccountsFragment) currentFragment).setAccountsList(accountList);
                            currentAccount = -1;
                        }
                        if (currentFragment instanceof TransactionsFragment)
                            getSupportActionBar().setTitle(accountList.get(currentAccount).getName());
                        if (currentFragment instanceof NewAccountFragment) {
                            if (accountToEdit == -1)
                                getSupportActionBar().setTitle("New account");
                            else
                                getSupportActionBar().setTitle("Edit account");
                        }
                        if (currentFragment instanceof NewTransactionFragment) {
                            if (transactionToEdit == -1)
                                getSupportActionBar().setTitle("New transaction");
                            else
                                getSupportActionBar().setTitle("Edit transaction");
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.new_transaction) {
            newTransactionFragment = NewTransactionFragment.newInstance(-1, currentAccount);
            getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.frag_container, newTransactionFragment).addToBackStack(null).commit();
        }
        if (item.getItemId() == R.id.edit_accounts) {
            editMode = true;
            ((AccountsFragment) currentFragment).toggleEditing(true);
            invalidateOptionsMenu(); //To call onCreateOptionsMenu on AccountsFragment
            Log.d("Menu", "Invalidated");
        }

        if (item.getItemId() == R.id.done_editing) {
            editMode = false;
            new SaveAccountsToDb().execute(this);
            ((AccountsFragment) currentFragment).toggleEditing(false);
            invalidateOptionsMenu();
        }
        if (item.getItemId() == R.id.new_account) {
            newAccountFragment = NewAccountFragment.newInstance(-1);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.frag_container, newAccountFragment).addToBackStack(null).commit();
            Log.d("Frag", "Replaced");
        }
        if (item.getItemId() == R.id.add_account_option) {
            if (accountToEdit != -1) {//if existing account was edited
                Account last = accountList.get(accountToEdit);
                accountList.set(accountToEdit, newAccountFragment.getNewAccount());
                accountList.get(accountToEdit).addToValue(last.getValue());
                accountList.get(accountToEdit).addToValue(last.getStartValue() * -1);
                accountToEdit = -1;
            } else
                accountList.add(0, newAccountFragment.getNewAccount());
            if (currentFragment instanceof AccountsFragment) {
                currentFragment = AccountsFragment.newInstance();
            }
            getSupportFragmentManager().popBackStack();
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        if (item.getItemId() == R.id.add_transaction_option) {
            if (transactionToEdit != -1) {
                double lastValue = transactionList.get(transactionToEdit).getValue();
                transactionList.set(transactionToEdit, newTransactionFragment.getNewTransaction());
                accountList.get(transactionList.get(transactionToEdit).getAccountId()).addToValue(transactionList.get(transactionToEdit).getValue());
                accountList.get(transactionList.get(transactionToEdit).getAccountId()).addToValue(lastValue * -1);
                transactionToEdit = -1;
            } else {
                transactionList.add(0, newTransactionFragment.getNewTransaction());
                accountList.get(transactionList.get(0).getAccountId()).addToValue(transactionList.get(0).getValue());
            }
            new SaveAccountsToDb().execute(this);
            new SaveTransactionsToDb().execute(this);
            if (currentFragment instanceof TransactionsFragment)
                ((TransactionsFragment) currentFragment).dataSetChanged();
            getSupportFragmentManager().popBackStack();
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        if (editMode) {
//            menu.clear();
//            MenuInflater inflater = getMenuInflater();
//            inflater.inflate(R.menu.edit_accounts_menu, menu);
//            Log.d("Menu", "Prepared");
//        }
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void editAccount(int id) {
        accountToEdit = id;
        newAccountFragment = NewAccountFragment.newInstance(id);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.frag_container, newAccountFragment).addToBackStack(null).commit();
    }


    private void fillRandomly() {
        for (int i = 0; i < 7; i++) {
            accountList.add(new Account("Visa Gold " + i, "Credit Card", 7000, "USD"));
        }
    }

    //DataBase work
    private class GetAccountsFromDb extends AsyncTask<Context, Void, Context> {

        @Override
        protected Context doInBackground(Context... params) {
            try {
                accountList = new ArrayList<>();
                DbHelper helper = new DbHelper(params[0]);
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor cursor = db.query("ACCOUNTS", new String[]{"NAME", "TYPE", "VALUE", "CURRENCY"}, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    accountList.add(new Account(cursor.getString(0), cursor.getString(1), Double.parseDouble(cursor.getString(2)), cursor.getString(3)));
                }
                cursor.close();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("Accounts", "retrieved from db");
            return params[0];
        }

        @Override
        protected void onPostExecute(Context context) {
            super.onPostExecute(context);
            if (currentFragment instanceof AccountsFragment) {
                ((AccountsFragment) currentFragment).setAccountsList(accountList);
                Log.d("Accounts", "dataset changed");
            }
            new GetTransactionsFromDb().execute(context);
        }
    }

    private class SaveAccountsToDb extends AsyncTask<Context, Void, Void> {

        @Override
        protected Void doInBackground(Context... params) {
            long startTime = System.currentTimeMillis();
            saveAccountsList(params[0]);

            long endTime = System.currentTimeMillis();

            System.out.println("DB work took " + (endTime - startTime) + " milliseconds");
            return null;
        }
    }

    private void saveAccountsList(Context context) {
        try {
            DbHelper helper = new DbHelper(context);
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("delete from " + "ACCOUNTS");
            for (int i = 0; i < accountList.size(); i++) {
                DbHelper.insertAccount(db, accountList.get(i).getName(), accountList.get(i).getType(), String.valueOf(accountList.get(i).getValue()), accountList.get(i).getCurrency());
            }
            db.close();
            Log.d("Main act", "Successfully inserted!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GetTransactionsFromDb extends AsyncTask<Context, Void, Void> {

        @Override
        protected Void doInBackground(Context... params) {
            try {
                long startTime = System.currentTimeMillis();
                transactionList = new ArrayList<>();
                categoryList = new ArrayList<>();
                DbHelper helper = new DbHelper(params[0]);
                SQLiteDatabase db = helper.getWritableDatabase();
//                DbHelper.insertCategory(db, "Zalupa");
//                DbHelper.insertCategory(db, "Profit");
//                Log.d("Categories", "Inserted");
                Cursor cursor = db.query("TRANSACTIONS", new String[]{"ACCOUNT_ID", "NAME", "CATEGORY_ID", "DATE", "VALUE"}, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    try {
                        transactionList.add(new Transaction(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), DATE_TIME_FORMAT.parse(cursor.getString(3)), Double.parseDouble(cursor.getString(4))));
                    } catch (ParseException ex) {
                        throw new IllegalArgumentException();
                    }
                }
                cursor = db.query("CATEGORIES", new String[]{"NAME"}, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    try {
                        categoryList.add(cursor.getString(0));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                cursor.close();
                db.close();
                long endTime = System.currentTimeMillis();
                System.out.println("Loading transactions and categories took " + (endTime - startTime) + " milliseconds");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class SaveTransactionsToDb extends AsyncTask<Context, Void, Void> {

        @Override
        protected Void doInBackground(Context... params) {
            try {
                DbHelper helper = new DbHelper(params[0]);
                SQLiteDatabase db = helper.getWritableDatabase();
                db.execSQL("delete from " + "TRANSACTIONS");
                for (int i = 0; i < transactionList.size(); i++) {
                    DbHelper.insertTransaction(db, transactionList.get(i).getAccountId(), transactionList.get(i).getCategory(), transactionList.get(i).getName(), DATE_TIME_FORMAT.format(transactionList.get(i).getDate()), String.valueOf(transactionList.get(i).getValue()));
                }
                db.close();
                Log.d("Main act", "Successfully inserted!!!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public ArrayList<String> getCategories() {
        return categoryList;
    }

    @Override
    public ArrayList<Account> getAccounts() {
        return accountList;
    }

    @Override
    public Transaction getTransaction(int position) {
        return transactionList.get(position);
    }

    @Override
    public void removeTransaction(int position) {
        accountList.get(transactionList.get(position).getAccountId()).addToValue(transactionList.get(position).getValue() * -1);
        transactionList.remove(position);
        new SaveTransactionsToDb().execute(this);
        new SaveAccountsToDb().execute(this);
        transactionToEdit = -1;
    }

    @Override
    public boolean isEditMode() {
        return editMode;
    }

    @Override
    public void showAccountTransactions(int position) {
        currentAccount = position;
        TransactionsFragment transactionsFragment = TransactionsFragment.newInstance(position);
        getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.frag_container, transactionsFragment).addToBackStack(null).commit();
    }

    @Override
    public Account getAccount(int i) {
        return accountList.get(i);
    }

    public void removeAccount(int position) {
        //new RemoveFromDb().execute(new RemoveAccountData(accountList.get(position), this, position));
        accountList.remove(position);
        for (Transaction tr :
                transactionList) {
            if (tr.getAccountId() == position) {
                transactionList.remove(tr);
            }
        }
        for (Transaction tr :
                transactionList) {
            if (tr.getAccountId() > position) {
                tr.increaseAccountId(-1);
            }
        }
        new SaveAccountsToDb().execute(this);
        new SaveTransactionsToDb().execute(this);
    }

    private class RemoveFromDb extends AsyncTask<RemoveAccountData, Void, Void> {

        @Override
        protected Void doInBackground(RemoveAccountData... params) {
            DbHelper helper = new DbHelper(params[0].getContext());
            helper.getWritableDatabase().delete("TRANSACTIONS", "ACCOUNT_ID=?", new String[]{String.valueOf(params[0].getPosition())});
            helper.getWritableDatabase().delete("ACCOUNTS", "name=? AND type=?", new String[]{params[0].getAccount().getName(), params[0].getAccount().getType()});
            Log.d("AccountID to remove", String.valueOf(params[0].getPosition()));
            return null;
        }
    }

    private class RemoveAccountData {
        private Account account;
        private Context context;
        private int position;

        RemoveAccountData(Account account, Context context, int position) {
            this.account = account;
            this.context = context;
            this.position = position;
        }

        public Context getContext() {
            return context;
        }

        Account getAccount() {
            return account;
        }

        public int getPosition() {
            return position;
        }
    }

    @Override
    public void editTransaction(int position) {
        transactionToEdit = position;
        newTransactionFragment = NewTransactionFragment.newInstance(position, currentAccount);
        getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.frag_container, newTransactionFragment).addToBackStack(null).commit();
    }

    @Override
    public ArrayList<Transaction> getTransactionsForAcc(int id) {
        ArrayList<Transaction> list = new ArrayList<>();
        for (Transaction tr :
                transactionList) {
            if (tr.getAccountId() == id) list.add(tr);
        }
        return list;
    }
}
