package nf.co.xine.budgetmanager.adapters;


import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import layout.NewAccountFragment;
import nf.co.xine.budgetmanager.DbHelper;
import nf.co.xine.budgetmanager.MainActivity;
import nf.co.xine.budgetmanager.R;
import nf.co.xine.budgetmanager.dataObjects.Account;
import nf.co.xine.budgetmanager.dataObjects.Transaction;

public class AccountAdapter extends ArrayAdapter<Account> {
    public AccountAdapter(Context context, ArrayList<Account> list, int mode) {
        super(context, 0);
        this.mode = mode;
        this.context = context;
        accounts = list;
    }

    private Context context;
    public static final int BROWSE = 0;
    public static final int EDIT = 1;
    public static final int EDITING = 2;
    private int mode;
    private ArrayList<Account> accounts;
    private int position;

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        final Account account = getItem(position);
        this.position = position;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            switch (mode) {
                case BROWSE: {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.accounts_row, parent, false);
                    break;
                }
                case EDIT: {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.edit_accounts_row, parent, false);
                    break;
                }
                case EDITING: {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.edit_accounts_row, parent, false);
                    break;
                }
            }

        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.account_name);
        TextView type = (TextView) convertView.findViewById(R.id.account_type);
        TextView value = (TextView) convertView.findViewById(R.id.account_value);

        if (mode != BROWSE) {
            Button removeBtn = (Button) convertView.findViewById(R.id.remove_account_btn);
            removeBtn.setTag(position);
            Button editBtn = (Button) convertView.findViewById(R.id.edit_account_btn);
            editBtn.setTag(position);
            Log.d("Tag", "Set to " + position);
            if (mode == EDIT) {
                Animation slideFast = AnimationUtils.loadAnimation(context, R.anim.slide_left);
                Animation slideSlow = AnimationUtils.loadAnimation(context, R.anim.slide_slow);
                slideFast.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mode = EDITING;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                removeBtn.startAnimation(slideFast);
                editBtn.startAnimation(slideSlow);
                value.startAnimation(slideFast);
            }
            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Clicked", String.valueOf(position));
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Are you sure?");
                    builder.setMessage("Remove account \"" + account.getName() + "\"?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((MainActivity) context).removeAccount(position);
                            remove(account);
                            notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                }
            });
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).editAccount(position);
                }
            });
        }
        // Populate the data into the template view using the data object
        name.setText(account.getName());
        type.setText(account.getType());
        value.setText(String.valueOf(account.getValue()) + " " + account.getCurrency().toUpperCase());
        // Return the completed view to render on screen
        return convertView;

    }


}
