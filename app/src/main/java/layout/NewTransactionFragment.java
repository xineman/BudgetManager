package layout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import nf.co.xine.budgetmanager.MainActivity;
import nf.co.xine.budgetmanager.R;
import nf.co.xine.budgetmanager.dataObjects.Account;
import nf.co.xine.budgetmanager.dataObjects.Transaction;

public class NewTransactionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TRANS_ID = "transactionId";
    private static final String ARG_ACC_ID = "currentAcc";

    // TODO: Rename and change types of parameters
    private int transactionId;
    private int currentAccount;
    private TextView name;
    private TextView value;
    private RadioGroup typeRadio;
    private TextView date;
    private int type = -1;
    private Spinner categoriesSpinner;
    private Spinner accountsSpinner;

    private Calendar myCalendar = Calendar.getInstance();
    private NewTransactionListener mListener;

    public NewTransactionFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static NewTransactionFragment newInstance(int transactionId, int currentAccount) {
        NewTransactionFragment fragment = new NewTransactionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TRANS_ID, transactionId);
        args.putInt(ARG_ACC_ID, currentAccount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            transactionId = getArguments().getInt(ARG_TRANS_ID);
            currentAccount = getArguments().getInt(ARG_ACC_ID);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.new_transaction_menu, menu);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        name = (TextView) getView().findViewById(R.id.new_transaction_name);
        value = (TextView) getView().findViewById(R.id.new_transaction_value);
        categoriesSpinner = (Spinner) getView().findViewById(R.id.categories_spinner);
        accountsSpinner = (Spinner) getView().findViewById(R.id.accounts_spinner);
        date = (TextView) getView().findViewById(R.id.transaction_date);
        typeRadio = (RadioGroup) getView().findViewById(R.id.radio_type);
        categoriesSpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mListener.getCategories()));
        accountsSpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mListener.getAccounts()));
        accountsSpinner.setSelection(currentAccount);
        if (transactionId != -1) {
            name.setText(mListener.getTransaction(transactionId).getName());
            value.setText(String.valueOf(Math.abs(mListener.getTransaction(transactionId).getValue())));
            if (mListener.getTransaction(transactionId).getValue() > 0)
                typeRadio.check(R.id.radio_income);
            categoriesSpinner.setSelection(mListener.getTransaction(transactionId).getCategory());
            myCalendar.setTime(mListener.getTransaction(transactionId).getDate());
            LinearLayout main = (LinearLayout) getView().findViewById(R.id.new_transaction_main);
            Button myButton = new Button(getActivity());
            myButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Are you sure?");
                    builder.setMessage("Remove \"" + mListener.getTransaction(transactionId).getName() + "\"?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().getSupportFragmentManager().popBackStack();
                            mListener.removeTransaction(transactionId);
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
            myButton.setText(getResources().getString(R.string.remove));
            main.addView(myButton);
        }
        date.setText(MainActivity.DATE_FORMAT.format(myCalendar.getTime()));
        final DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                date.setText(MainActivity.DATE_FORMAT.format(myCalendar.getTime()));
            }

        };
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        typeRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_expense: {
                        type = -1;
                        Log.d("Type", String.valueOf(type));
                        break;
                    }
                    case R.id.radio_income: {
                        type = 1;
                        Log.d("Type", String.valueOf(type));
                        break;
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_transaction, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NewTransactionListener) {
            mListener = (NewTransactionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public Transaction getNewTransaction() {
        return new Transaction(accountsSpinner.getSelectedItemPosition(), name.getText().toString(), categoriesSpinner.getSelectedItemPosition(), myCalendar.getTime(), Double.parseDouble(value.getText().toString()) * type);
    }

    public interface NewTransactionListener {
        ArrayList<String> getCategories();

        ArrayList<Account> getAccounts();

        Transaction getTransaction(int position);

        void removeTransaction(int position);
    }
}
