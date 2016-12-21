package layout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import nf.co.xine.budgetmanager.R;
import nf.co.xine.budgetmanager.adapters.SettingsAdapter;
import nf.co.xine.budgetmanager.dataObjects.Account;
import nf.co.xine.budgetmanager.dataObjects.Setting;


public class SettingsFragment extends Fragment {
    private String[] settings;
    private ListView settingsList;
    private OnSettingsListener mListener;
    private ArrayList<Setting> settingArrayList;
    private SharedPreferences sharedPref;
    private SettingsAdapter settingsAdapter;

    public SettingsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        settingsList = (ListView) getView().findViewById(R.id.settings_list);
        settings = getResources().getStringArray(R.array.settings_array);
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        settingArrayList = new ArrayList<>();
        String defaultAccountName;
        if (mListener.getAccounts().isEmpty()) {
            defaultAccountName = "No accounts";
        } else {
            defaultAccountName = mListener.getAccounts().get(sharedPref.getInt(getString(R.string.default_account), 0)).getName();
        }
        settingArrayList.add(new Setting(settings[0], ""));
        settingArrayList.add(new Setting(settings[1], defaultAccountName));
        settingArrayList.add(new Setting(settings[2], sharedPref.getString(getString(R.string.privat_account), "Login")));
        settingsAdapter = new SettingsAdapter(getActivity());
        settingsAdapter.addAll(settingArrayList);
        settingsList.setAdapter(settingsAdapter);
        settingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        mListener.editCategories();
                        break;
                    }
                    case 1: {
                        if (mListener.getAccounts().isEmpty()) {
                            AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                            b.setMessage("No accounts!");
                            b.create().show();
                        } else {
                            AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                            b.setTitle("Select default account");
                            b.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, mListener.getAccounts()), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putInt(getString(R.string.default_account), which);
                                    editor.apply();
                                    settingArrayList.set(1, new Setting(settings[1], mListener.getAccounts().get(which).getName()));
                                    settingsAdapter.clear();
                                    settingsAdapter.addAll(settingArrayList);
                                }
                            });
                            b.create().show();
                        }
                        break;
                    }
                    case 2: {
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSettingsListener) {
            mListener = (OnSettingsListener) context;
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
    public interface OnSettingsListener {
        void editCategories();

        ArrayList<Account> getAccounts();
    }
}
