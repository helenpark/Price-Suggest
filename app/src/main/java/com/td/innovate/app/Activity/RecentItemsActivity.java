package com.td.innovate.app.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.td.innovate.app.Model.Profile;
import com.td.innovate.app.Model.Result;
import com.td.innovate.app.R;

import java.util.ArrayList;

public class RecentItemsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_items);

        Profile profile = new Profile(this);
        ArrayList<Result> pastResults = profile.getPastResults();
        ArrayList<String> pastResultsNames = new ArrayList<>();
        for (int i = 0; i < pastResults.size(); i++) {
            Result currentResult = pastResults.get(i);
            pastResultsNames.add(currentResult.getName());
        }

        ListView recentItemsListview = (ListView) findViewById(R.id.recentItemsListview);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, pastResultsNames);
        recentItemsListview.setAdapter(adapter);
    }
}
