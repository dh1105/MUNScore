package com.munscore;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class AddCountries extends AppCompatActivity {

    View parent;
    LinearLayout linearLayout;
    ArrayList<EditText> textView = new ArrayList<>();
    String [] co;
    String [] cr;
    String name;
    DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_countries);
        mydb = new DBHelper(AddCountries.this);
        parent = findViewById(android.R.id.content);
        linearLayout = (LinearLayout) findViewById(R.id.ll_1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_deltext).setVisible(false);
        menu.findItem(R.id.action_create).setVisible(false);
        menu.findItem(R.id.action_home).setVisible(false);
        if(textView.size() != 0){
            menu.findItem(R.id.action_deltext).setVisible(true);
            menu.findItem(R.id.action_create).setVisible(true);
        }
        else{
            menu.findItem(R.id.action_deltext).setVisible(false);
            menu.findItem(R.id.action_create).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_addtext:
                EditText mtextView = new EditText(this);
                textView.add(mtextView);
                Log.d("Text: ", textView.toString());
                mtextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                mtextView.setHint("Enter country/portfolio");
                mtextView.setTextColor(getResources().getColor(android.R.color.black));
                mtextView.setGravity(Gravity.CENTER_HORIZONTAL);
                mtextView.setTextSize(18.0f);
                Log.d("TextView: ", mtextView.toString());
                linearLayout.addView(mtextView);
                Log.d("LL", linearLayout.toString());
                this.invalidateOptionsMenu();
                break;

            case R.id.action_deltext:
                linearLayout.removeView(textView.get(textView.size()-1));
                textView.remove(textView.size()-1);
                Log.d("Text1: ", textView.toString());
                this.invalidateOptionsMenu();
                break;

            case R.id.action_create:
                co = getEditText();
                if(co != null) {
                    AlertDialog.Builder al = new AlertDialog.Builder(AddCountries.this);
                    al.setCancelable(true);
                    al.setTitle("Save details");
                    al.setMessage("Are you sure you want to save these changes?");
                    al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent in = getIntent();
                            name = in.getStringExtra("name");
                            Bundle b;
                            b = in.getExtras();
                            cr = b.getStringArray("criteria");
                            new InsertAsync().execute();
                            Intent fin = new Intent(AddCountries.this, ParamClass.class);
                            Log.d("Starting ", fin.toString());
                            setResult(Activity.RESULT_OK, fin);
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

    public String[] getEditText(){
        List<String> c = new ArrayList<>();
        for(int k = 0; k < textView.size(); k++){
            String criteria = textView.get(k).getText().toString();
            c.add(criteria);
        }
        String [] cr = new String[c.size()];
        for(int i = 0; i<c.size(); i++){
            cr[i] = c.get(i);
        }
        for (String aCr : cr) {
            if (aCr.isEmpty()) {
                Snackbar.make(parent, "Please fill all fields or delete the empty fields", Snackbar.LENGTH_LONG).show();
                return null;
            }
        }
        return cr;
    }

    public class InsertAsync extends AsyncTask<Void, Void, Void> {

        ProgressDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(AddCountries.this);
            p.setCancelable(false);
            p.setMessage("Creating committee");
            p.setIndeterminate(false);
            p.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            mydb.dropTab();
            mydb.createTables(AddCountries.this, cr);
            mydb.insertCommittee(name, 1);
            List<String> c = new ArrayList<>();
            for(int i = 0; i<co.length; i++){
                c.add(co[i]);
            }
            mydb.insertCountry(c, 1);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            p.dismiss();
        }
    }
}
