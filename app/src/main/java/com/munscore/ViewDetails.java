package com.munscore;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ViewDetails extends AppCompatActivity {

    DBHelper mydb;
    ListView listView;
    List<NameAndScore> nameAndScores;
    android.support.v7.app.ActionBar ab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);
        TextView comm_name_det = (TextView) findViewById(R.id.comm_name_det);
        mydb = new DBHelper(getApplicationContext());
        setCommText(comm_name_det);
        ArrayList<String> countries;
        countries = mydb.getCountryName();
        String [] b = mydb.getJudgeCol();
        String [] names = Country_name(countries);
        float [] scores = getFinalScores(countries, b);
        nameAndScores = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            NameAndScore item = new NameAndScore(scores[i], names[i]);
            nameAndScores.add(item);
        }
        listView = (ListView) findViewById(R.id.lv_res);
        CustomNameScoreAdapter adapter = new CustomNameScoreAdapter(getApplicationContext(), R.layout.score_detail, nameAndScores);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name=((TextView) view.findViewById(R.id.coun_sc_name)).getText().toString();
                Intent in=new Intent(ViewDetails.this, CountryResultDetails.class);
                in.putExtra("name", name);
                startActivity(in);
            }
        });
        String title = "Countries and Scores";
        ab = getSupportActionBar();
        ab.setTitle(title);
    }

    void setCommText(TextView a){
        Cursor c = mydb.getComName();
        c.moveToFirst();
        a.setText(c.getString(c.getColumnIndex(DBHelper.COMMITTEE_NAME)));
    }

    float[] getFinalScores(ArrayList<String> c, String [] b){
        ArrayList<String> j = new ArrayList<>();
        j.addAll(Arrays.asList(b).subList(3, b.length));
        HashMap<String, Float> hmap = mydb.calFinalSpeechScore(getApplicationContext(), c, j);
        float [] n = new float[hmap.size()];
        for(int i=0; i<n.length; i++){
            n[i] = hmap.get(c.get(i));
        }
        return n;
    }

    String [] Country_name(ArrayList<String> countries){
        String [] a = new String[countries.size()];
        for(int i=0; i<a.length; i++){
            a[i] = countries.get(i);
        }
        return a;
    }
}
