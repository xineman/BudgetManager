package layout;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import nf.co.xine.budgetmanager.R;
import nf.co.xine.budgetmanager.dataObjects.Budget;

public class NewBudgetSetupFragment extends Fragment {


    private OnNewBudgetListener mListener;
    private ArrayList<String> categories;
    private Spinner categoriesSpinner;
    private Spinner periodSpinner;
    private TextView value;
    private int budgetToEdit = -1;
    private Budget editedBudget;

    public NewBudgetSetupFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewBudgetSetupFragment newInstance() {
        NewBudgetSetupFragment fragment = new NewBudgetSetupFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.new_budget_menu, menu);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        categories = mListener.getCategories();
        categoriesSpinner = (Spinner) getView().findViewById(R.id.new_budget_category);
        periodSpinner = (Spinner) getView().findViewById(R.id.new_budget_period);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, categories);
        ArrayAdapter<CharSequence> periodAdapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.period_array, R.layout.spinner_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(adapter);
        periodSpinner.setAdapter(periodAdapter);
        value = (TextView) getView().findViewById(R.id.new_budget_value);
        if (budgetToEdit != -1) {
            editedBudget = mListener.getBudgetList().get(budgetToEdit);
            categoriesSpinner.setSelection(editedBudget.getCategoryId());
            periodSpinner.setSelection(editedBudget.getPeriod());
            value.setText(String.valueOf(editedBudget.getValue()));
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
                    builder.setMessage("Remove this setup?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().getSupportFragmentManager().popBackStack();
                            mListener.removeBudget(budgetToEdit);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_budget_setup, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewBudgetListener) {
            mListener = (OnNewBudgetListener) context;
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

    public Budget getNewBudget() {
        Budget budget;
        try {
            budget = new Budget(categoriesSpinner.getSelectedItemPosition(), Double.parseDouble(value.getText().toString()), periodSpinner.getSelectedItemPosition());
        } catch (Exception ex) {
            return null;
        }
        return budget;
    }

    public void setBudgetToEdit(int budgetToEdit) {
        this.budgetToEdit = budgetToEdit;
    }

    public interface OnNewBudgetListener {
        ArrayList<String> getCategories();

        void removeBudget(int position);

        ArrayList<Budget> getBudgetList();
    }
}
