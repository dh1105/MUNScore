package com.munscore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParamClass extends AppCompatActivity {

    LinearLayout linearLayout, countries_ll;
    ArrayList<EditText> textView = new ArrayList<>();
    Button add_countries, countries;
    View parent;
    private final int PERM = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param_class);
        parent = findViewById(android.R.id.content);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        countries_ll = (LinearLayout) findViewById(R.id.button_ll);

        add_countries = (Button) findViewById(R.id.add_countries);
        countries = (Button) findViewById(R.id.countries);

        add_countries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] cr = getEditText();
                if(cr != null) {
                    String dref_res, direct, chit_m, p_o_i, p_o_o;
                    Log.d("Array: ", Arrays.toString(cr));
                    Intent i = getIntent();
                    dref_res = i.getStringExtra("dr");
                    direct = i.getStringExtra("dir");
                    chit_m = i.getStringExtra("chit");
                    p_o_i = i.getStringExtra("poi");
                    p_o_o = i.getStringExtra("poo");
                    String name = i.getStringExtra("name");
                    String day = i.getStringExtra("day");
                    Log.d("Day param: ", day);
                    Intent in = new Intent(ParamClass.this, AddCountries.class);
                    in.putExtra("name", name);
                    in.putExtra("day", day);
                    Bundle b = new Bundle();
                    b.putStringArray("criteria", cr);
                    in.putExtras(b);
                    in.putExtra("chit", chit_m);
                    in.putExtra("poi", p_o_i);
                    in.putExtra("poo", p_o_o);
                    in.putExtra("dir", direct);
                    in.putExtra("dr", dref_res);
                    startActivityForResult(in, PERM);
                }
            }
        });

        countries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] cr = getEditText();
                if(cr != null) {
                    String dref_res, direct, chit_m, p_o_i, p_o_o;
                    Log.d("Array: ", Arrays.toString(cr));
                    Intent i = getIntent();
                    String name = i.getStringExtra("name");
                    String day = i.getStringExtra("day");
                    dref_res = i.getStringExtra("dr");
                    direct = i.getStringExtra("dir");
                    chit_m = i.getStringExtra("chit");
                    p_o_i = i.getStringExtra("poi");
                    p_o_o = i.getStringExtra("poo");
                    Log.d("Day param: ", day);
                    Intent in = new Intent(ParamClass.this, SelectCountries.class);
                    in.putExtra("name", name);
                    in.putExtra("day", day);
                    Bundle b = new Bundle();
                    b.putStringArray("criteria", cr);
                    in.putExtras(b);
                    in.putExtra("chit", chit_m);
                    in.putExtra("poi", p_o_i);
                    in.putExtra("poo", p_o_o);
                    in.putExtra("dir", direct);
                    in.putExtra("dr", dref_res);
                    startActivityForResult(in, PERM);
                }
            }
        });
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
        }
        else{
            countries_ll.setVisibility(View.GONE);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PERM:
                if(resultCode == RESULT_OK){
                    Intent in = new Intent(ParamClass.this, WriteDetermine.class);
                    setResult(Activity.RESULT_OK, in);
                    finish();
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_addtext:
                countries_ll.setVisibility(View.VISIBLE);
                EditText mtextView = new EditText(this);
                textView.add(mtextView);
                Log.d("Text: ", textView.toString());
                mtextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                mtextView.setHint("Enter criteria");
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
}
