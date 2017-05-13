package com.td.innovate.app.Adapter;

/**
 * Created by joshuahill on 2015-11-10.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.td.innovate.app.R;

import java.util.ArrayList;

/**
 * Created by joshuahill on 2015-11-10.
 */
public class ReviewsAdapter extends ArrayAdapter<String> {

    public ReviewsAdapter(Context context, ArrayList<String> AL) {
        super(context, 0, AL);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String string = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reviews_list_view, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.reviewsTV);
        // Populate the data into the template view using the data object
        tvName.setText(string);
        // Return the completed view to render on screen
        return convertView;
    }
}
