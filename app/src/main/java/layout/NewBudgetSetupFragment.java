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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import nf.co.xine.budgetmanager.R;

public class NewBudgetSetupFragment extends Fragment {


    private OnNewBudgetListener mListener;
    private ArrayList<String> categories;
    private Spinner categoriesSpinner;
    private Spinner periodSpinner;

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

    public interface OnNewBudgetListener {
        ArrayList<String> getCategories();
    }
}
