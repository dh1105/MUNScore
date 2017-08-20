package com.munscore;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ViewDetails extends AppCompatActivity implements SearchView.OnQueryTextListener {

    DBHelper mydb;
    ListView listView;
    List<NameAndScore> nameAndScores;
    List<NameAndScore> filter;
    android.support.v7.app.ActionBar ab;
    CustomNameScoreAdapter adapter;
    CountryFilter countryFilter = new CountryFilter();
    String [] names;
    float [] scores;

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
        names = Country_name(countries);
        scores = getFinalScores(countries, b);
        nameAndScores = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            NameAndScore item = new NameAndScore(scores[i], names[i]);
            nameAndScores.add(item);
        }
        listView = (ListView) findViewById(R.id.lv_res);
        adapter = new CustomNameScoreAdapter(getApplicationContext(), R.layout.score_detail, nameAndScores);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_bar, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(ViewDetails.this);
        return true;
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        countryFilter.getFilter().filter(newText);
        return false;
    }

    private class CountryFilter extends Filter{

        public Filter getFilter(){
            if(countryFilter == null){
                countryFilter = new CountryFilter();
            }
            return countryFilter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if(constraint !=null && constraint.length()>0){
                List<NameAndScore> temp = new ArrayList<>();
                for(int i=0; i<nameAndScores.size(); i++){
                    if(nameAndScores.get(i).getCounName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        Log.d("Name: ", nameAndScores.get(i).getCounName());
                        Log.d("Score: ", Float.toString(nameAndScores.get(i).getScore()));
                        NameAndScore item = new NameAndScore(scores[i], names[i]);
                        temp.add(item);
                    }
                }
                filterResults.count = temp.size();
                filterResults.values = temp;
                Log.d("TempList: ", temp.toString());
            }
            else{
                filterResults.count = nameAndScores.size();
                filterResults.values = nameAndScores;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filter = (List<NameAndScore>) results.values;
            adapter = new CustomNameScoreAdapter(getApplicationContext(), R.layout.score_detail, filter);
            listView.setAdapter(adapter);
        }
    }
}
