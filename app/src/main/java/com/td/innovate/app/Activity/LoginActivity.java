package com.td.innovate.app.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.td.innovate.app.R;

public class LoginActivity extends AppCompatActivity {

    EditText access_ET;
    EditText pass_ET;
    Button login_BTN;
    Button dummyLogin_BTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);

        access_ET = (EditText) findViewById(R.id.access_card_edittext);
        pass_ET = (EditText) findViewById(R.id.password_field_edittext);
        login_BTN = (Button) findViewById(R.id.doneButton);
        dummyLogin_BTN = (Button) findViewById(R.id.dummy_data_button);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dummy_login, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void dummyDataLogin(View view){
        Toast.makeText(this, "DUMMY DATA LOGIN", Toast.LENGTH_SHORT).show();
        access_ET.setText("20533374193");
        pass_ET.setText("password");

        Intent intent = new Intent(this, PaymentsActivity.class);
        startActivity(intent);
    }

    public void doneLogIn(View view){
        Toast.makeText(this, "ACTUAL LOGIN", Toast.LENGTH_SHORT).show();
    }
}
