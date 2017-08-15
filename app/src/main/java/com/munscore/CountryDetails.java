package com.munscore;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CountryDetails extends AppCompatActivity {

    android.support.v7.app.ActionBar ab;
    TextView comm_name, no_of_speeches, no_of_poo, no_of_poi, no_of_chits, no_of_dr, no_of_dir;
    String name, act;
    DBHelper mydb;
    private static final String POINT_OF_INFO = "point_of_info";
    private static final String POINT_OF_ORDER = "point_of_order";
    private static final String CHIT = "chit";
    private static final String DR = "draft_reso";
    private static final String DIRECTIVE = "directive";

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
        no_of_poo = (TextView) findViewById(R.id.no_of_poo);
        no_of_poi = (TextView) findViewById(R.id.no_of_poi);
        no_of_dir = (TextView) findViewById(R.id.no_of_dir);
        no_of_dr = (TextView) findViewById(R.id.no_of_dr);
        no_of_chits = (TextView) findViewById(R.id.no_of_chits);
        setSpeechNo(act);
        linearLayoutVis();
        this.invalidateOptionsMenu();
        /*Button b = (Button) findViewById(R.id.new_speech);
        b.setOnClickListener(this);*/
    }

    void linearLayoutVis(){
        LinearLayout lr_poo, lr_poi, lr_chits, lr_dr, lr_dir;
        lr_poo = (LinearLayout) findViewById(R.id.lr_poo);
        lr_poi = (LinearLayout) findViewById(R.id.lr_poi);
        lr_chits = (LinearLayout) findViewById(R.id.lr_chits);
        lr_dir = (LinearLayout) findViewById(R.id.lr_dir);
        lr_dr = (LinearLayout) findViewById(R.id.lr_dr);
        if(mydb.isTableExist(POINT_OF_INFO)){
            lr_poi.setVisibility(View.VISIBLE);
        }
        if(mydb.isTableExist(POINT_OF_ORDER)){
            lr_poo.setVisibility(View.VISIBLE);
        }
        if(mydb.isTableExist(CHIT)){
            lr_chits.setVisibility(View.VISIBLE);
        }
        if (mydb.isTableExist(DIRECTIVE)) {

            lr_dir.setVisibility(View.VISIBLE);
        }
        if(mydb.isTableExist(DR)){
            lr_dr.setVisibility(View.VISIBLE);
        }
    }

    public void setSpeechNo(String a){
        switch (a) {
            case "DayOne":
                int val = mydb.getSpeechCount(name, 1);
                no_of_speeches.setText(Integer.toString(val));
                no_of_speeches.setText(Integer.toString(val));
                if(mydb.isTableExist(POINT_OF_ORDER)){
                    int poo = mydb.getPooCount(name, 1);
                    no_of_poo.setText(Integer.toString(poo));
                }
                if(mydb.isTableExist(POINT_OF_INFO)){
                    int poi = mydb.getPoiCount(name, 1);
                    no_of_poi.setText(Integer.toString(poi));
                }
                if(mydb.isTableExist(CHIT)) {
                    int chit = mydb.getChitCount(name, 1);
                    no_of_chits.setText(Integer.toString(chit));
                }
                if(mydb.isTableExist(DIRECTIVE)) {
                    int dir = mydb.getDirCount(name, 1);
                    no_of_dir.setText(Integer.toString(dir));
                }
                if(mydb.isTableExist(DR)) {
                    int dr = mydb.getDrCount(name, 1);
                    no_of_dr.setText(Integer.toString(dr));
                }
                break;
            case "DayTwo":
                int val1 = mydb.getSpeechCount(name, 2);
                no_of_speeches.setText(Integer.toString(val1));
                if(mydb.isTableExist(POINT_OF_ORDER)){
                    int poo = mydb.getPooCount(name, 2);
                    no_of_poo.setText(Integer.toString(poo));
                }
                if(mydb.isTableExist(POINT_OF_INFO)){
                    int poi = mydb.getPoiCount(name, 2);
                    no_of_poi.setText(Integer.toString(poi));
                }
                if(mydb.isTableExist(CHIT)) {
                    int chit = mydb.getChitCount(name, 2);
                    no_of_chits.setText(Integer.toString(chit));
                }
                if(mydb.isTableExist(DIRECTIVE)) {
                    int dir = mydb.getDirCount(name, 2);
                    no_of_dir.setText(Integer.toString(dir));
                }
                if(mydb.isTableExist(DR)) {
                    int dr = mydb.getDrCount(name, 2);
                    no_of_dr.setText(Integer.toString(dr));
                }
                break;
            case "DayThree":
                int val2 = mydb.getSpeechCount(name, 3);
                no_of_speeches.setText(Integer.toString(val2));
                if(mydb.isTableExist(POINT_OF_ORDER)){
                    int poo = mydb.getPooCount(name, 3);
                    no_of_poo.setText(Integer.toString(poo));
                }
                if(mydb.isTableExist(POINT_OF_INFO)){
                    int poi = mydb.getPoiCount(name, 3);
                    no_of_poi.setText(Integer.toString(poi));
                }
                if(mydb.isTableExist(CHIT)) {
                    int chit = mydb.getChitCount(name, 3);
                    no_of_chits.setText(Integer.toString(chit));
                }
                if(mydb.isTableExist(DIRECTIVE)) {
                    int dir = mydb.getDirCount(name, 3);
                    no_of_dir.setText(Integer.toString(dir));
                }
                if(mydb.isTableExist(DR)) {
                    int dr = mydb.getDrCount(name, 3);
                    no_of_dr.setText(Integer.toString(dr));
                }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.speech_menu, menu);
        Log.d(POINT_OF_ORDER, String.valueOf(mydb.isTableExist(POINT_OF_ORDER)));
        Log.d(POINT_OF_INFO, String.valueOf(mydb.isTableExist(POINT_OF_INFO)));
        Log.d(CHIT, String.valueOf(mydb.isTableExist(CHIT)));
        Log.d(DIRECTIVE, String.valueOf(mydb.isTableExist(DIRECTIVE)));
        Log.d(DR, String.valueOf(mydb.isTableExist(DR)));
        if(mydb.isTableExist(POINT_OF_ORDER)){
            menu.findItem(R.id.new_poo).setVisible(true);
        }
        /*else{
            menu.findItem(R.id.new_poo).setVisible(false);
        }*/
        if(mydb.isTableExist(POINT_OF_INFO)){
            menu.findItem(R.id.new_poi).setVisible(true);
        }
        /*else{
            menu.findItem(R.id.new_poi).setVisible(false);
        }*/
        if(mydb.isTableExist(CHIT)){
            menu.findItem(R.id.new_chit).setVisible(true);
        }
        /*else{
            menu.findItem(R.id.new_chit).setVisible(false);
        }*/
        if(mydb.isTableExist(DIRECTIVE)){
            menu.findItem(R.id.new_dir).setVisible(true);
        }
        /*else{
            menu.findItem(R.id.new_dir).setVisible(false);
        }*/
        if(mydb.isTableExist(DR)){
            menu.findItem(R.id.new_dr).setVisible(true);
        }
        /*else{
            menu.findItem(R.id.new_dr).setVisible(false);
        }*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_poo:
                switch (act) {
                    case "DayOne":
                        Intent in = new Intent(CountryDetails.this, DayOneScore.class);
                        in.putExtra("name", name);
                        in.putExtra("type", "poo");
                        startActivityForResult(in, 4);
                        break;

                    case "DayTwo":
                        Intent i = new Intent(CountryDetails.this, DayTwoScore.class);
                        i.putExtra("name", name);
                        i.putExtra("type", "poo");
                        startActivityForResult(i, 5);
                        break;

                    case "DayThree":
                        Intent o = new Intent(CountryDetails.this, DayThreeScore.class);
                        o.putExtra("name", name);
                        o.putExtra("type", "poo");
                        startActivityForResult(o, 6);
                        break;

                }
                break;

            case R.id.new_poi:
                switch (act) {
                    case "DayOne":
                        Intent in = new Intent(CountryDetails.this, DayOneScore.class);
                        in.putExtra("name", name);
                        in.putExtra("type", "poi");
                        startActivityForResult(in, 7);
                        break;

                    case "DayTwo":
                        Intent i = new Intent(CountryDetails.this, DayTwoScore.class);
                        i.putExtra("name", name);
                        i.putExtra("type", "poi");
                        startActivityForResult(i, 8);
                        break;

                    case "DayThree":
                        Intent o = new Intent(CountryDetails.this, DayThreeScore.class);
                        o.putExtra("name", name);
                        o.putExtra("type", "poi");
                        startActivityForResult(o, 9);
                        break;

                }
                break;

            case R.id.new_chit:
                switch (act) {
                    case "DayOne":
                        Intent in = new Intent(CountryDetails.this, DayOneScore.class);
                        in.putExtra("name", name);
                        in.putExtra("type", "chit");
                        startActivityForResult(in, 10);
                        break;

                    case "DayTwo":
                        Intent i = new Intent(CountryDetails.this, DayTwoScore.class);
                        i.putExtra("name", name);
                        i.putExtra("type", "chit");
                        startActivityForResult(i, 11);
                        break;

                    case "DayThree":
                        Intent o = new Intent(CountryDetails.this, DayThreeScore.class);
                        o.putExtra("name", name);
                        o.putExtra("type", "chit");
                        startActivityForResult(o, 12);
                        break;

                }
                break;

            case R.id.new_speech:
                switch (act) {
                    case "DayOne":
                        Intent in = new Intent(CountryDetails.this, DayOneScore.class);
                        in.putExtra("name", name);
                        in.putExtra("type", "speech");
                        startActivityForResult(in, 1);
                        break;

                    case "DayTwo":
                        Intent i = new Intent(CountryDetails.this, DayTwoScore.class);
                        i.putExtra("name", name);
                        i.putExtra("type", "speech");
                        startActivityForResult(i, 2);
                        break;

                    case "DayThree":
                        Intent o = new Intent(CountryDetails.this, DayThreeScore.class);
                        o.putExtra("name", name);
                        o.putExtra("type", "speech");
                        startActivityForResult(o, 3);
                        break;

                }
                break;

            case R.id.new_dr:
                switch (act) {
                    case "DayOne":
                        Intent in = new Intent(CountryDetails.this, DayOneScore.class);
                        in.putExtra("name", name);
                        in.putExtra("type", "dr");
                        startActivityForResult(in, 13);
                        break;

                    case "DayTwo":
                        Intent i = new Intent(CountryDetails.this, DayTwoScore.class);
                        i.putExtra("name", name);
                        i.putExtra("type", "dr");
                        startActivityForResult(i, 14);
                        break;

                    case "DayThree":
                        Intent o = new Intent(CountryDetails.this, DayThreeScore.class);
                        o.putExtra("name", name);
                        o.putExtra("type", "dr");
                        startActivityForResult(o, 15);
                        break;

                }
                break;

            case R.id.new_dir:
                switch (act) {
                    case "DayOne":
                        Intent in = new Intent(CountryDetails.this, DayOneScore.class);
                        in.putExtra("name", name);
                        in.putExtra("type", "dir");
                        startActivityForResult(in, 16);
                        break;

                    case "DayTwo":
                        Intent i = new Intent(CountryDetails.this, DayTwoScore.class);
                        i.putExtra("name", name);
                        i.putExtra("type", "dir");
                        startActivityForResult(i, 17);
                        break;

                    case "DayThree":
                        Intent o = new Intent(CountryDetails.this, DayThreeScore.class);
                        o.putExtra("name", name);
                        o.putExtra("type", "dir");
                        startActivityForResult(o, 18);
                        break;

                }
                break;
        }
        return super.onOptionsItemSelected(item);
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
        else if(requestCode == 4){
            if(resultCode == RESULT_OK){
                Snackbar.make(parentView, "New point of order for day 1 added for "+ name, Snackbar.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 5){
            if(resultCode == RESULT_OK){
                Snackbar.make(parentView, "New point of order for day 2 added for "+ name, Snackbar.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 6){
            if(resultCode == RESULT_OK){
                Snackbar.make(parentView, "New point of order for day 3 added for "+ name, Snackbar.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 7){
            if(resultCode == RESULT_OK){
                Snackbar.make(parentView, "New point of info for day 1 added for "+ name, Snackbar.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 8){
            if(resultCode == RESULT_OK){
                Snackbar.make(parentView, "New point of info for day 2 added for "+ name, Snackbar.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 9){
            if(resultCode == RESULT_OK){
                Snackbar.make(parentView, "New point of info for day 3 added for "+ name, Snackbar.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 10){
            if(resultCode == RESULT_OK){
                Snackbar.make(parentView, "New chit for day 1 added for "+ name, Snackbar.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 11){
            if(resultCode == RESULT_OK){
                Snackbar.make(parentView, "New chit for day 2 added for "+ name, Snackbar.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 12){
            if(resultCode == RESULT_OK){
                Snackbar.make(parentView, "New chit for day 3 added for "+ name, Snackbar.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 13){
            if(resultCode == RESULT_OK){
                Snackbar.make(parentView, "New draft reso for day 1 added for "+ name, Snackbar.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 14){
            if(resultCode == RESULT_OK){
                Snackbar.make(parentView, "New draft reso for day 2 added for "+ name, Snackbar.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 15){
            if(resultCode == RESULT_OK){
                Snackbar.make(parentView, "New draft reso for day 3 added for "+ name, Snackbar.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 16){
            if(resultCode == RESULT_OK){
                Snackbar.make(parentView, "New directive for day 3 added for "+ name, Snackbar.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 17){
            if(resultCode == RESULT_OK){
                Snackbar.make(parentView, "New directive for day 3 added for "+ name, Snackbar.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 18){
            if(resultCode == RESULT_OK){
                Snackbar.make(parentView, "New directive for day 3 added for "+ name, Snackbar.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*@Override
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
    }*/
}
