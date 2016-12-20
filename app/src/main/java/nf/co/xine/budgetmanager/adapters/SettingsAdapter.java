package nf.co.xine.budgetmanager.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import nf.co.xine.budgetmanager.R;
import nf.co.xine.budgetmanager.dataObjects.Setting;

public class SettingsAdapter extends ArrayAdapter<Setting> {
    public SettingsAdapter(Context context) {
        super(context, 0);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.settings_row, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.setting_name);
        TextView value = (TextView) convertView.findViewById(R.id.setting_value);
        name.setText(getItem(position).getName());
        value.setText(getItem(position).getValue());
        return convertView;
    }
}
