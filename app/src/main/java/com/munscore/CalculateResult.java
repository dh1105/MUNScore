package com.munscore;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CalculateResult extends AppCompatActivity {

    ArrayList<TextView> textViews = new ArrayList<>();
    ArrayList<TextView> text_score = new ArrayList<>();
    String best_del, high_comm, spec_men;
    DBHelper mydb;
    private static final String POINT_OF_INFO = "point_of_info";
    private static final String POINT_OF_ORDER = "point_of_order";
    private static final String CHIT = "chit";
    private static final String DR = "draft_reso";
    private static final String DIRECTIVE = "directive";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_result);
        Intent in = getIntent();
        best_del = in.getStringExtra("best_del");
        high_comm = in.getStringExtra("high");
        spec_men = in.getStringExtra("spec");
        mydb = new DBHelper(getApplicationContext());
        ArrayList<String> c = getCountryNames(mydb);
        String [] b = mydb.getJudgeCol();
        HashMap<String, Float> hmap = getScores(c, mydb, b);
        Log.d("Initial Map", hmap.toString());
        HashMap<String, Float> hpoo = null;
        HashMap<String, Float> hpoi = null;
        HashMap<String, Float> hchit = null;
        HashMap<String, Float> hdr = null;
        HashMap<String, Float> hdir = null;
        if(mydb.isTableExist(POINT_OF_INFO)){
            hpoi = mydb.calPointScore(getApplicationContext(), c, POINT_OF_INFO);
            Log.d(POINT_OF_INFO, hpoi.toString());
        }
        if(mydb.isTableExist(POINT_OF_ORDER)){
            hpoo = mydb.calPointScore(getApplicationContext(), c, POINT_OF_ORDER);
            Log.d(POINT_OF_ORDER, hpoo.toString());
        }
        if(mydb.isTableExist(CHIT)){
            hchit = mydb.calPointScore(getApplicationContext(), c, CHIT);
            Log.d(CHIT, hchit.toString());
        }
        if(mydb.isTableExist(DR)){
            hdr = mydb.calPointScore(getApplicationContext(), c, DR);
            Log.d(DR, hdr.toString());
        }
        if(mydb.isTableExist(DIRECTIVE)){
            hdir = mydb.calPointScore(getApplicationContext(), c, DIRECTIVE);
            Log.d(DIRECTIVE, hdir.toString());
        }
        hmap = TotScore(hmap, hpoi, hpoo, hchit, hdr, hdir, c);
        hmap = sortByComparator(hmap, false);
        Log.d("Final Map", hmap.toString());
        ArrayList<String> names = new ArrayList<>();
        for(String coun: hmap.keySet()){
            names.add(coun);
        }
        names  = addText(best_del, "Best Delegate", names);
        names = addText(high_comm, "High Commendation", names);
        addText(spec_men, "Special Mention", names);
        Log.d("Not Inserting", "Res");
        if(!mydb.getResultQuery()) {
            Log.d("Inserting", "Res");
            InsertandCreateRes(hmap);
        }
    }

    void InsertandCreateRes(HashMap<String, Float> hmap){
        mydb.insertResultTable(hmap, getApplicationContext());
    }

    private HashMap<String, Float> TotScore(HashMap<String, Float> hmap, HashMap<String, Float> poi, HashMap<String, Float> poo, HashMap<String, Float> chit, HashMap<String, Float> dr, HashMap<String, Float> dir, ArrayList<String> c){
        for(int i=0; i<hmap.size(); i++){
            if(poi!=null){
                hmap.put(c.get(i), hmap.get(c.get(i)) + poi.get(c.get(i)));
            }
            if(poo!=null){
                hmap.put(c.get(i), hmap.get(c.get(i)) + poo.get(c.get(i)));
            }
            if(chit!=null){
                hmap.put(c.get(i), hmap.get(c.get(i)) + chit.get(c.get(i)));
            }
            if(dr!=null){
                hmap.put(c.get(i), hmap.get(c.get(i)) + dr.get(c.get(i)));
            }
            if(dir!=null){
                hmap.put(c.get(i), hmap.get(c.get(i)) + dir.get(c.get(i)));
            }
        }
        return hmap;
    }

    private ArrayList<String> addText(String a, String b, ArrayList<String> names){
        Log.d("Names: ", names.toString());
        int a_val = parseString(a);
        Log.d("A val: ", Integer.toString(a_val));
        for(int i=0; i<a_val; i++){
            LinearLayout chat = (LinearLayout) findViewById(R.id.result_lin);
            LinearLayout lv = new LinearLayout(getApplicationContext());
            lv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            lv.setOrientation(LinearLayout.HORIZONTAL);
            chat.addView(lv);
            TextView t = new TextView(CalculateResult.this);
            TextView tv = new TextView(CalculateResult.this);
            textViews.add(t);
            text_score.add(tv);
            tv.setText(names.get(0));
            tv.setPadding(10, 10, 10, 10);
            tv.setTextColor(getResources().getColor(android.R.color.black));
            t.setText(b);
            t.setPadding(10, 10, 10, 10);
            t.setTextColor(getResources().getColor(android.R.color.black));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(30, 30, 20, 10);
            params.gravity = Gravity.START;
            t.setLayoutParams(params);
            t.setGravity(Gravity.CENTER);
            t.setTextSize(18.0f);
            LinearLayout.LayoutParams params_name = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params_name.setMargins(5, 30, 20, 10);
            params_name.gravity = Gravity.END;
            tv.setLayoutParams(params_name);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(18.0f);
            lv.addView(t);
            lv.addView(tv);
            String rem = names.remove(0);
            Log.d("i: ", Integer.toString(i));
            Log.d("Removed: ", rem);
        }
        return names;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    String contentMessage(){
        Cursor c = mydb.getComName();
        c.moveToFirst();
        String val = c.getString(c.getColumnIndex(DBHelper.COMMITTEE_NAME));
        val += "\n";
        int bd = parseString(best_del);
        int hc = parseString(high_comm);
        int sm = parseString(spec_men);
        int i=0;
        if(bd != 0){
            for(i=0; i<bd; i++){
                val += "Best Delegate: " + text_score.get(i).getText().toString();
                val += "\n";
            }
        }
        if(hc != 0){
            i=bd;
            while(hc>0){
                val += "High Commendation: " + text_score.get(i).getText().toString();
                val += "\n";
                i++;
                hc--;
            }
        }
        if(sm != 0){
            while(sm>0){
                val += "Special Mention: " + text_score.get(i).getText().toString();
                val += "\n";
                i++;
                sm--;
            }
        }
        Log.d("Val: ", val);
        return val;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.send_det:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                //intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                intent.putExtra(Intent.EXTRA_TEXT, contentMessage());
                startActivity(Intent.createChooser(intent, ""));
                break;

            case R.id.action_del:
                AlertDialog.Builder al = new AlertDialog.Builder(CalculateResult.this);
                al.setMessage("Are you sure you want to delete this committee?");
                al.setCancelable(true);
                al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mydb.dropTab();
                        Intent in = new Intent(getApplicationContext(), ResultFill.class);
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

        }
        return super.onOptionsItemSelected(item);
    }

    HashMap<String, Float> getScores(ArrayList<String> c, DBHelper mydb , String [] b){
        ArrayList<String> j = new ArrayList<>();
        j.addAll(Arrays.asList(b).subList(3, b.length));
        return mydb.calFinalSpeechScore(getApplicationContext(), c, j);
    }

    int parseString(String a){
        return Integer.parseInt(a);
    }

    ArrayList<String> getCountryNames(DBHelper mydb){
        ArrayList<String> countries;
        countries = mydb.getCountryName();
        return countries;
    }

    private HashMap<String, Float> sortByComparator(HashMap<String, Float> unsortMap, final boolean order)
    {

        List<Map.Entry<String, Float>> list = new LinkedList<Map.Entry<String, Float>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<String, Float>>()
        {
            public int compare(Map.Entry<String, Float> o1,
                               Map.Entry<String, Float> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        HashMap<String, Float> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Float> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
