package com.munscore;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DayTwoScore extends AppCompatActivity{

    ArrayList<TextView> textViews = new ArrayList<>();
    ArrayList<EditText> editTexts = new ArrayList<>();
    DBHelper mydb;
    android.support.v7.app.ActionBar ab;
    View parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_two_score);
        parent = findViewById(android.R.id.content);
        mydb = new DBHelper(DayTwoScore.this);
        ab = getSupportActionBar();
        Intent i = getIntent();
        String name = i.getStringExtra("name");
        ab.setTitle(name + ": Day 2");
        addTextBoxes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_addtext).setVisible(false);
        menu.findItem(R.id.action_deltext).setVisible(false);
        menu.findItem(R.id.action_home).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_create).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_create:
                String [] c = getEditText();
        }
        return super.onOptionsItemSelected(item);
    }

    void addTextBoxes(){
        LinearLayout score_lay = (LinearLayout) findViewById(R.id.score_lay2);
        String [] col_name = mydb.getJudgeCol();
        for (int i=3; i<col_name.length; i++) {
            TextView tv = new TextView(this);
            EditText ed = new EditText(this);
            textViews.add(tv);
            editTexts.add(ed);
            tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setText(col_name[i]);
            tv.setTextColor(getResources().getColor(android.R.color.black));
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setTextSize(18.0f);
            score_lay.addView(tv);
            ed.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ed.setHint(R.string.setscore);
            ed.setTextColor(getResources().getColor(android.R.color.black));
            ed.setGravity(Gravity.CENTER_HORIZONTAL);
            ed.setTextSize(18.0f);
            score_lay.addView(ed);
        }
    }

    public String[] getEditText(){
        List<String> c = new ArrayList<>();
        for(int k = 0; k < editTexts.size(); k++){
            String criteria = editTexts.get(k).getText().toString();
            c.add(criteria);
        }
        String [] cr = new String[c.size()];
        for(int i = 0; i<c.size(); i++){
            cr[i] = c.get(i);
        }
        for (String aCr : cr) {
            if (aCr.isEmpty()) {
                Snackbar.make(parent, "Please fill all fields", Snackbar.LENGTH_LONG).show();
                return null;
            }
        }
        return cr;
    }

}
