package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nf.co.xine.budgetmanager.R;
import nf.co.xine.budgetmanager.adapters.BudgetAdapter;
import nf.co.xine.budgetmanager.dataObjects.Budget;
import nf.co.xine.budgetmanager.dataObjects.Transaction;
import ua.privatbank.framework.api.Message;
import ua.privatbank.payoneclicklib.Api;
import ua.privatbank.payoneclicklib.Pay;

public class BudgetFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //private TextView mTransaction;
    private ListView budgetList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean budgetEditMode = false;
    private BudgetAdapter budgetAdapter;
    private ArrayList<Transaction> transactions;

    private BudgetFragListener mListener;

    public BudgetFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BudgetFragment newInstance() {
        BudgetFragment fragment = new BudgetFragment();
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
        inflater.inflate(R.menu.budget_menu, menu);
    }

    /*private void privatGet() {
        String uuid = "5ec33f90-a471-11e2-9e96-0800200c9a65";

        Pay mPay = new Pay(getActivity(), new Api.ApiEventListener<ua.privatbank.payoneclicklib.Api>() {
            @Override
            public void onApiStartRequest() {
                //mTransaction.setText("начало");
                // ваш код обработки начала отправки очереди запросов
            }

            @Override
            public void onApiFinishRequest() {
                //mTransaction.setText("конец");
                Log.d("Request", "finished");
                // ваш код обработки завершения отправки очереди запросов
            }

            @Override
            public void onApiError(ua.privatbank.payoneclicklib.Api api, Message.ErrorCode code) {
                //mTransaction.setText(api.getLastServerFailCode() + " " + code);
                // ваш код обработки ошибок которые приходят от сервера (список есть ниже)
                // код ошибки нужно получать этим методом: api.getLastServerFailCode()
                //Log.d("Error", api.getLastServerFailCode());
            }
        }, uuid, "104112");

        mPay.getHistory(new Pay.GetHistoryCallBack() {
            @Override
            public void onGetHistorySuccess(List<ua.privatbank.payoneclicklib.model.Transaction> transactionList) {
                //  код обработки полученного списка транзакций
                mTransaction.setText("получили список" + transactionList.toString());
            }

            @Override
            public void onGetHistoryFailed() {
                // ваш код обработки ошибок при получении списка транзакций
                mTransaction.setText("история транзакций отсутствует");
            }
        });

        mPay.destroy();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_budget, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        transactions = mListener.getTransactionList();
        budgetList = (ListView) getView().findViewById(R.id.budget_list);
        //mTransaction = (TextView) getView().findViewById(R.id.transactions_list);
        budgetAdapter = new BudgetAdapter(getActivity(), mListener.getBudgetList(), mListener.getTransactionList(), mListener.getCategories());
        budgetAdapter.addAll(mListener.getBudgetList());
        budgetList.setAdapter(budgetAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BudgetFragListener) {
            mListener = (BudgetFragListener) context;
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
        budgetEditMode = editMode;
        if (editMode) {
            budgetAdapter = new BudgetAdapter(getActivity(), mListener.getBudgetList(), mListener.getTransactionList(), mListener.getCategories());
            budgetList.setOnItemClickListener(null);
        } else {
            budgetAdapter = new BudgetAdapter(getActivity(), mListener.getBudgetList(), mListener.getTransactionList(), mListener.getCategories());
            budgetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //mListener.showAccountTransactions(position);
                    Log.d("Listener", "position is " + position);
                }
            });
        }
        budgetAdapter.addAll(mListener.getBudgetList());
        budgetList.setAdapter(budgetAdapter);
    }

    public interface BudgetFragListener {
        ArrayList<Transaction> getTransactionList();

        ArrayList<Budget> getBudgetList();

        ArrayList<String> getCategories();
    }
}
