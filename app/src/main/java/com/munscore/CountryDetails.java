package com.munscore;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CountryDetails extends AppCompatActivity implements View.OnClickListener {

    android.support.v7.app.ActionBar ab;
    TextView comm_name, no_of_speeches;
    String name, act;
    DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_details);
        mydb = new DBHelper(getApplicationContext());
        Intent i = getIntent();
        act = i.getStringExtra("act");
        name = i.getStringExtra("name");
        setActionBar(act, name);
        comm_name = (TextView) findViewById(R.id.comm_name);
        comm_name.setText(name);
        no_of_speeches = (TextView) findViewById(R.id.no_of_speeches);
        setSpeechNo(act);
        Button b = (Button) findViewById(R.id.new_speech);
        b.setOnClickListener(this);
    }

    public void setSpeechNo(String a){
        switch (a) {
            case "DayOne":
                int val = mydb.getSpeechCount(name, 1);
                no_of_speeches.setText(Integer.toString(val));
                break;
            case "DayTwo":
                int val1 = mydb.getSpeechCount(name, 2);
                no_of_speeches.setText(Integer.toString(val1));
                break;
            case "DayThree":
                int val2 = mydb.getSpeechCount(name, 3);
                no_of_speeches.setText(Integer.toString(val2));
                break;
        }
    }

    public void setActionBar(String a, String b) {
        switch (a) {
            case "DayOne":
                ab = getSupportActionBar();
                ab.setTitle(b + ": Day 1");
                break;
            case "DayTwo":
                ab = getSupportActionBar();
                ab.setTitle(b + ": Day 2");
                break;
            case "DayThree":
                ab = getSupportActionBar();
                ab.setTitle(b + ": Day 2");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        View parentView = findViewById(android.R.id.content);
        setSpeechNo(act);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                Snackbar.make(parentView, "New speech for day 1 added for "+ name, Snackbar.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 2){
            if(resultCode == RESULT_OK){
                Snackbar.make(parentView, "New speech for day 2 added for "+ name, Snackbar.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 3){
            if(resultCode == RESULT_OK){
                Snackbar.make(parentView, "New speech for day 3 added for "+ name, Snackbar.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_speech:
                switch (act) {
                    case "DayOne":
                        Intent in = new Intent(CountryDetails.this, DayOneScore.class);
                        in.putExtra("name", name);
                        startActivityForResult(in, 1);
                        break;

                    case "DayTwo":
                        Intent i = new Intent(CountryDetails.this, DayTwoScore.class);
                        i.putExtra("name", name);
                        startActivityForResult(i, 2);
                        break;

                    case "DayThree":
                        Intent o = new Intent(CountryDetails.this, DayThreeScore.class);
                        o.putExtra("name", name);
                        startActivityForResult(o, 2);
                        break;

                }
        }
    }
}
