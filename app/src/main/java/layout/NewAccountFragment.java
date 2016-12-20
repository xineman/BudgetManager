package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import nf.co.xine.budgetmanager.R;
import nf.co.xine.budgetmanager.dataObjects.Account;
import nf.co.xine.budgetmanager.dataObjects.Setting;


public class NewAccountFragment extends Fragment {

    private OnNewAccountListener mListener;
    private EditText name;
    private Spinner type;
    private EditText value;
    private EditText currency;
    private int accountId;
    private ArrayList<String> typeList;

    public NewAccountFragment() {
        // Required empty public constructor
    }


    public static NewAccountFragment newInstance(int accountId) {
        NewAccountFragment fragment = new NewAccountFragment();
        Bundle args = new Bundle();
        args.putInt("accountId", accountId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            accountId = getArguments().getInt("accountId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_account, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        typeList = mListener.getTypeList();
        name = (EditText) getView().findViewById(R.id.new_account_name);
        type = (Spinner) getView().findViewById(R.id.new_account_type);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, typeList);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        type.setAdapter(adapter);
        //type.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, typeList));
        value = (EditText) getView().findViewById(R.id.new_account_value);
        currency = (EditText) getView().findViewById(R.id.new_account_currency);
        if (accountId != -1) {
            name.setText(mListener.getAccount(accountId).getName());
            type.setSelection(mListener.getAccount(accountId).getType());
            value.setText(String.valueOf(mListener.getAccount(accountId).getStartValue()));
            currency.setText(mListener.getAccount(accountId).getCurrency());
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.new_account_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewAccountListener) {
            mListener = (OnNewAccountListener) context;
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

    public Account getNewAccount() {
        try {
            return new Account(name.getText().toString(), type.getSelectedItemPosition(), Double.parseDouble(value.getText().toString()), currency.getText().toString());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnNewAccountListener {
        Account getAccount(int id);

        ArrayList<String> getTypeList();
    }
}
