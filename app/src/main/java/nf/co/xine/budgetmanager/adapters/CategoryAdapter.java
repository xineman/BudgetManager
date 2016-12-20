package nf.co.xine.budgetmanager.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import layout.CategoriesFragment;
import nf.co.xine.budgetmanager.MainActivity;
import nf.co.xine.budgetmanager.R;
import nf.co.xine.budgetmanager.dataObjects.RemoveCategoryData;
import nf.co.xine.budgetmanager.listeners.RemoveCategoryOnClickListener;

public class CategoryAdapter extends ArrayAdapter<String> {
    public CategoryAdapter(Context context, CategoriesFragment categoriesFragment) {
        super(context, 0);
        this.context = (MainActivity) context;
        this.categoriesFragment = categoriesFragment;
    }

    private MainActivity context;
    private CategoriesFragment categoriesFragment;

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_row, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.category_name);
        Button removeBtn = (Button) convertView.findViewById(R.id.remove_category_btn);
        removeBtn.setTag(position);
        Button editBtn = (Button) convertView.findViewById(R.id.edit_category_btn);
        editBtn.setTag(position);
        Log.d("Tag", "Set to " + position);
        removeBtn.setOnClickListener(new RemoveCategoryOnClickListener(new RemoveCategoryData(position, context, context.getCategories(), context.getTransactionList(), categoriesFragment)));
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) context).editCategory(position);
            }
        });
        // Populate the data into the template view using the data object
        name.setText(getItem(position));
        // Return the completed view to render on screen
        return convertView;
    }


}
