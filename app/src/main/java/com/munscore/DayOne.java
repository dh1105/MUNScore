package com.munscore;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 7/29/2017.
 */

public class DayOne extends Fragment {

    View v;
    ListView lv_country;
    ArrayList<String> coun_names = new ArrayList<>();
    DBHelper mydb;
    TextView name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.day_one, container, false);
        lv_country = (ListView) v.findViewById(R.id.lv_country);
        lv_country.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name=(String) lv_country.getItemAtPosition(position);
                Intent in=new Intent(getActivity(), CountryDetails.class);
                in.putExtra("name", name);
                in.putExtra("act", "DayOne");
                startActivity(in);
            }
        });
        mydb = new DBHelper(getActivity());
        name = (TextView) v.findViewById(R.id.com_name);
        new GetCountry().execute();
        getActivity().invalidateOptionsMenu();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("", "Showing home");
        inflater.inflate(R.menu.main, menu);
        menu.findItem(R.id.action_addtext).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_create).setVisible(false);
        menu.findItem(R.id.action_deltext).setVisible(true);
        menu.findItem(R.id.action_home).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_deltext:
                AlertDialog.Builder al = new AlertDialog.Builder(getActivity());
                al.setMessage("Are you sure you want to delete this committee?");
                al.setCancelable(true);
                al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mydb.dropTab();
                        TextView day1=(TextView) v.findViewById(R.id.day_one);
                        day1.setVisibility(View.GONE);
                        lv_country.setVisibility(View.GONE);
                        coun_names.clear();
                        Intent in = new Intent(getActivity(), MainActivity.class);
                        Log.d("Intent: ", in.toString());
                        startActivity(in);
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

    private class GetCountry extends AsyncTask<Void, Void, Void> {

        String nam;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Cursor rs = mydb.getComName();
                rs.moveToFirst();
                nam = rs.getString(rs.getColumnIndex(DBHelper.COMMITTEE_NAME));
                coun_names = mydb.getCountryName();
                if(coun_names != null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lv_country.setVisibility(View.VISIBLE);
                            lv_country.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, coun_names));
                        }
                    });
                }
                else{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //lv.setVisibility(View.GONE);
                        }
                    });
                }
                if (!nam.isEmpty()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //fab.setVisibility(View.GONE);
                            //name.setVisibility(View.VISIBLE);
                            name.setText(nam);
                            //MainActivity.this.invalidateOptionsMenu();
                            //navigationView.setCheckedItem(R.id.day_one);
                            //FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            //transaction.replace(R.id.content_frame, new DayOne()).commit();
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //fab.setVisibility(View.VISIBLE);
                            name.setVisibility(View.GONE);
                        }
                    });
                }
            }
            catch (SQLException f){
                f.printStackTrace();
                /*getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //fab.setVisibility(View.VISIBLE);
                        name.setVisibility(View.GONE);
                    }
                });*/
            }
            return null;
        }
    }
}
