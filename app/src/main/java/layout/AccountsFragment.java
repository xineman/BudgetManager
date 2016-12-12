package layout;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import nf.co.xine.budgetmanager.R;
import nf.co.xine.budgetmanager.adapters.AccountAdapter;
import nf.co.xine.budgetmanager.dataObjects.Account;

public class AccountsFragment extends Fragment {

    private ArrayList<Account> accountsList;
    private AccountsFragListener mListener;
    private AccountAdapter accountAdapter;
    private ListView accountsView;

    public AccountsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AccountsFragment newInstance() {
        AccountsFragment fragment = new AccountsFragment();
        Bundle args = new Bundle();
        //args.putParcelableArrayList("accountList", accountList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            //accountsList = getArguments().getParcelableArrayList("accountList");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getView() != null) {
            accountsView = (ListView) getView().findViewById(R.id.accounts_list);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accounts, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mListener.isEditMode())
            inflater.inflate(R.menu.edit_accounts_menu, menu);
        else
            inflater.inflate(R.menu.main_menu, menu);
        Log.d("Menu", "inflated");
        super.onCreateOptionsMenu(menu, inflater);
    }

 /*   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit_accounts) {

        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AccountsFragListener) {
            mListener = (AccountsFragListener) context;
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


    public void toggleEditing(boolean editMode) {
        if (editMode) {
            accountAdapter = new AccountAdapter(getActivity(), accountsList, AccountAdapter.EDIT);
            accountsView.setOnItemClickListener(null);
        } else {
            accountAdapter = new AccountAdapter(getActivity(), accountsList, AccountAdapter.BROWSE);
            accountsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mListener.showAccountTransactions(position);
                    Log.d("Listener", "position is " + position);
                }
            });
        }
        accountAdapter.addAll(accountsList);
        accountsView.setAdapter(accountAdapter);
    }

    public void setAccountsList(ArrayList<Account> accounts) {
        accountsList = accounts;
        toggleEditing(mListener.isEditMode());
    }

    public interface AccountsFragListener {
        boolean isEditMode();

        ArrayList<Account> getAccounts();

        void showAccountTransactions(int position);
    }
}
