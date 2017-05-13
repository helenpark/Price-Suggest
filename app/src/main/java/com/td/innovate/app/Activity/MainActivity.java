package com.td.innovate.app.Activity;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.td.innovate.app.Model.Profile;
import com.td.innovate.app.Model.Transaction;
import com.td.innovate.app.R;
import com.td.innovate.app.Service.AIService;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private SharedPreferences prefs;
    private static final String ACTION_AI = "com.td.innovate.app.Service.action.AI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        prefs = this.getSharedPreferences("com.td.innovate.app", Context.MODE_PRIVATE);

        if (!prefs.getBoolean("defaultDataSet", false)) {
            loadDefaultData();
        }

        startActionAI(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void barcodeCapture(View view) {
        Intent intent = new Intent(this, BarcodeCaptureActivity.class);
        startActivity(intent);
    }

    public void imageCapture(View view) {
        Intent intent = new Intent(this, ImageCaptureActivity.class);
        startActivity(intent);
    }

    public void getRecentItems(View view) {
        Intent intent = new Intent(this, RecentItemsActivity.class);
        startActivity(intent);
    }

    private void loadDefaultData() {
        try {
            InputStream file = context.getResources().getAssets().open("customerRecord.json");
            byte[] data = new byte[file.available()];
            file.read(data);
            file.close();
            String fileContentsString = new String(data);
            try {
                JSONObject fileContents = new JSONObject(fileContentsString);
                System.out.println("Successfully got JSON contents");

                Profile profile = new Profile(context);
                profile.setName(fileContents.getString("name"));
                profile.setAccountBalance(fileContents.getDouble("accountBalance"));
                profile.setCreditBalance(fileContents.getDouble("creditBalance"));
                profile.setCreditLimit(fileContents.getDouble("creditLimit"));

                JSONArray jsonTransactions = fileContents.getJSONArray("transactions");
                ArrayList<Transaction> transactions = new ArrayList<>();

                for (int i = 0; i < jsonTransactions.length(); i++) {
                    JSONObject jsonTransaction = jsonTransactions.getJSONObject(i);
                    String transactionName = jsonTransaction.getString("name");
                    String transactionDate = jsonTransaction.getString("date");
                    Double transactionCost = jsonTransaction.getDouble("cost");
                    transactions.add(new Transaction(transactionName, transactionDate, transactionCost));
                }

                profile.setTransactions(transactions);

                prefs.edit().putBoolean("defaultDataSet", true).apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts this service to perform action AI with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionAI(Context context) {
        Intent intent = new Intent(context, AIService.class);
        intent.setAction(ACTION_AI);
        context.startService(intent);
    }
}
