package com.munscore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DayOneScore extends AppCompatActivity {
    ArrayList<TextView> textViews = new ArrayList<>();
    ArrayList<EditText> editTexts = new ArrayList<>();
    DBHelper mydb;
    android.support.v7.app.ActionBar ab;
    View parent;
    String [] c, cr;
    String name;
    private static final String JUDGE_TABLE_NAME = "judge";
    private static final String POINT_OF_INFO = "point_of_info";
    private static final String POINT_OF_ORDER = "point_of_order";
    private static final String CHIT = "chit";
    private static final String DR = "draft_reso";
    private static final String DIRECTIVE = "directive";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_one_score);
        parent = findViewById(android.R.id.content);
        mydb = new DBHelper(DayOneScore.this);
        ab = getSupportActionBar();
        Intent i = getIntent();
        String name = i.getStringExtra("name");
        ab.setTitle(name + ": Day 1");
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
                Intent i = getIntent();
                name = i.getStringExtra("name");
                c = getEditText();
                cr = getTextView();
                if(c != null && cr != null){
                    AlertDialog.Builder al = new AlertDialog.Builder(DayOneScore.this);
                    al.setCancelable(true);
                    al.setTitle("Save details");
                    al.setMessage("Are you sure you want to save these changes?");
                    al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = getIntent();
                            String type = i.getStringExtra("type");
                            if(type.equals("poo")){
                                mydb.InsertTabScore(getApplicationContext(), name, POINT_OF_ORDER, 1, Integer.parseInt(c[0]));
                            }
                            if(type.equals("poi")){
                                mydb.InsertTabScore(getApplicationContext(), name, POINT_OF_INFO, 1, Integer.parseInt(c[0]));
                            }
                            if(type.equals("chit")){
                                mydb.InsertTabScore(getApplicationContext(), name, CHIT, 1, Integer.parseInt(c[0]));
                            }
                            if(type.equals("speech")){
                                mydb.insertScore(getApplicationContext(), cr, c, 1, name);
                            }
                            if(type.equals("dr")){
                                mydb.InsertTabScore(getApplicationContext(), name, DR, 1, Integer.parseInt(c[0]));
                            }
                            if(type.equals("dir")){
                                mydb.InsertTabScore(getApplicationContext(), name, DIRECTIVE, 1, Integer.parseInt(c[0]));
                            }
                            Intent in = new Intent(getApplicationContext(), CountryDetails.class);
                            setResult(RESULT_OK, in);
                            finish();
                        }
                    });
                    al.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    al.show();
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    void addTextBoxes(){
        LinearLayout score_lay = (LinearLayout) findViewById(R.id.score_lay);
        Intent in = getIntent();
        String type = in.getStringExtra("type");
        String [] col_name = new String[0];
        if(type.equals("poo")){
            col_name = mydb.getCol(POINT_OF_ORDER);
        }
        if(type.equals("poi")){
            col_name = mydb.getCol(POINT_OF_INFO);
        }
        if(type.equals("chit")){
            col_name = mydb.getCol(CHIT);
        }
        if(type.equals("speech")){
            col_name = mydb.getCol(JUDGE_TABLE_NAME);
        }
        if(type.equals("dr")){
            col_name = mydb.getCol(DR);
        }
        if(type.equals("dir")){
            col_name = mydb.getCol(DIRECTIVE);
        }
        for (int i=3; i<col_name.length; i++) {
            TextView tv = new TextView(DayOneScore.this);
            EditText ed = new EditText(DayOneScore.this);
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

    public String[] getTextView(){
        List<String> c = new ArrayList<>();
        for(int k = 0; k < textViews.size(); k++){
            String criteria = textViews.get(k).getText().toString();
            c.add(criteria);
        }
        String [] cr = new String[c.size()];
        for(int i = 0; i<c.size(); i++){
            cr[i] = c.get(i);
        }
        return cr;
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
