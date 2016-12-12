package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import nf.co.xine.budgetmanager.R;
import nf.co.xine.budgetmanager.adapters.TransactionAdapter;
import nf.co.xine.budgetmanager.dataObjects.Account;
import nf.co.xine.budgetmanager.dataObjects.Transaction;

public class TransactionsFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ACCOUNT_ID = "account_id";

    private int accountId;
    private TextView accountValue;
    private ListView transactionsList;
    private TransactionAdapter transactionAdapter;
    private ArrayList<Transaction> transactions;

    private TransactionFragListener mListener;

    public TransactionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionsFragment.
     */

    public static TransactionsFragment newInstance(int accountId) {
        TransactionsFragment fragment = new TransactionsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ACCOUNT_ID, accountId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            accountId = getArguments().getInt(ARG_ACCOUNT_ID);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.transactions_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transactions, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        accountValue = (TextView) getView().findViewById(R.id.account_value);
        transactionsList = (ListView) getView().findViewById(R.id.transaction_list);
        transactionAdapter = new TransactionAdapter(getActivity(), mListener.getCategories(), mListener.getAccount(accountId).getCurrency());
        transactions = mListener.getTransactionsForAcc(accountId);
        transactionAdapter.addAll(transactions);
        transactionsList.setAdapter(transactionAdapter);
        transactionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.editTransaction(position);
            }
        });
        accountValue.setText(getResources().getString(R.string.total_value) + " " + String.valueOf(mListener.getAccount(accountId).getValue()) + " " + mListener.getAccount(accountId).getCurrency().toUpperCase());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TransactionFragListener) {
            mListener = (TransactionFragListener) context;
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

    public void dataSetChanged() {
        transactionAdapter.notifyDataSetChanged();
    }

    public interface TransactionFragListener {
        Account getAccount(int id);

        ArrayList<Transaction> getTransactionsForAcc(int id);

        ArrayList<String> getCategories();

        void editTransaction(int position);
    }
}
