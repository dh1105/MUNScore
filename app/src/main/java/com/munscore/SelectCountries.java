package com.munscore;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SelectCountries extends AppCompatActivity {

    MyCustomAdapter dataAdapter = null;
    int n = 0;
    TextView count;
    List<String> co = new ArrayList<>();
    DBHelper mydb;
    String [] cr;
    String name, day;
    int d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_countries);
        count = (TextView) findViewById(R.id.count);
        count.setText(Integer.toString(n));
        mydb = new DBHelper(SelectCountries.this);
        displayListView();
    }

    private void displayListView() {
        ListView lv = (ListView) findViewById(R.id.country_list);
        String [] countries = {"Afghanistan",  "Albania", " Algeria",  "Andorra",  "Angola",  "Antigua and Barbuda",  "Argentina",   "Armenia", "Australia", "Austria", "Azerbaijan",  "The Bahamas",  "Bahrain People", "Bangladesh", "Barbados", " Belarus", "Belgium", "Belize", "Bhutan", " Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Cambodia"," Cameroon"," Canada"," Cabo Verde",  "Central African Republic"," Chad"," Chile", "People's Republic of China", "Colombia ", "Comoros", " Congo", "Costa Rica", "Côte d’Ivoire", "Croatia", "Cuba"," Cyprus"," Czech Republic", "Democratic People’s  of Korea",  "Democratic Republic of the Congo", "Denmark", " Djibouti", " Dominica Dominican", "Republic of Ecuador",  "Egypt"," El Salvador", "Equatorial Guinea",  "Eritrea", "Estonia", "Ethiopia", "Republic of Fiji", "Finland", "France",  "Gabon", "Georgia",  "Germany",  "Ghana" ,"Greece", "Grenada", "Guatemala" , "Guinea", "Guinea-Bissau"," Guyana", "Haiti", "Honduras", " Hungary", "Iceland", "India", "Indonesia",  "Islamic Republic of Iran", "Republic of Iraq", "Ireland", "Israel", "Italy",  "Jamaica",  "Japan", "Jordan", "Kazakhstan", "Kenya", "Kuwait", "Kyrgyzstan", " Lao", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania", "Mauritius", "Mexico", "Micronesia", "Monaco", "Mongolia", " Montenegro", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Qatar", "Republic  of Korea", "Moldova",  "Romania",  "Russian Federation", "Rwanda",  "Saint Kitts and Nevis", "Saint Lucia",  "Saint Vincent and the Grenadines",  "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovak Republic", "Slovenia",  "Solomon Islands", "Somalia", "South Africa", "South Sudan", "Spain",  "Sri Lanka", "Sudan", "Suriname", "Swaziland", "Sweden", "Switzerland",  "Syrian Arab Republic", " Tajikistan", "Thailand", "Yugoslav", "Macedonia", "Timor-Leste", "Togo", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey",  "Turkmenistan", "Tuvalu", "Uganda",  "Ukraine", "United Arab Emirates", " United Kingdom", "Tanzania",  "United States of America", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Viet Nam", "Yemen", " Zambia", "Zimbabwe"};

        //Array list of countries
        ArrayList<Country> countryList = new ArrayList<>();
        for( int i =0; i<countries.length; i++){
            Country country = new Country(countries[i], false);
            countryList.add(country);
        }

        Log.d("Country list: ", countryList.toString());
        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(getApplicationContext(), R.layout.country_layout, countryList);
        lv.setAdapter(dataAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_deltext).setVisible(false);
        menu.findItem(R.id.action_addtext).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_home).setVisible(false);
        if(n == 0) {
            menu.findItem(R.id.action_create).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_create:
                AlertDialog.Builder al = new AlertDialog.Builder(SelectCountries.this);
                al.setCancelable(true);
                al.setTitle("Save details");
                al.setMessage(Integer.toString(n) + " countries have been selected. Are you sure you want to save these changes?");
                al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent in = getIntent();
                        name = in.getStringExtra("name");
                        day = in.getStringExtra("day");
                        Log.d("Day select: ", day);
                        try {
                            d = Integer.parseInt(day);
                            Log.d("Day add: ", day);
                        } catch(NumberFormatException nfe) {
                            Log.d("Could not parse ", nfe.toString());
                        }
                        Bundle b;
                        b = in.getExtras();
                        cr = b.getStringArray("criteria");
                        ArrayList<Country> countryList = dataAdapter.countryList;
                        for(int i=0;i<countryList.size();i++){
                            Country country = countryList.get(i);
                            if(country.isSelected()){
                                co.add(country.getName());
                                Log.d("Countries: ",co.toString());
                            }
                        }
                        new InsertAsync().execute();
                        Intent fin =  new Intent(SelectCountries.this, ParamClass.class);
                        Log.d("Starting ", fin.toString());
                        setResult(Activity.RESULT_OK, fin);
                        finish();
                    }
                });
                al.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        co.clear();
                        Log.d("Clear", co.toString());
                    }
                });
                al.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyCustomAdapter extends ArrayAdapter<Country> {

        private ArrayList<Country> countryList;

        MyCustomAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<Country> countryList) {
            super(context, resource, countryList);
            this.countryList = new ArrayList<>();
            this.countryList.addAll(countryList);
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

            ViewHolder holder = null;
            Log.d("ConvertView", String.valueOf(position));
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.country_layout, null);

                holder = new ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.checkCountry);
                convertView.setTag(holder);
                final Country country = countryList.get(position);
                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        if(cb.isChecked()){
                            n++;
                            Log.d("n: ", Integer.toString(n));
                            count.setText(Integer.toString(n));
                            SelectCountries.this.invalidateOptionsMenu();
                        }
                        else{
                            n--;
                            Log.d("n: ", Integer.toString(n));
                            count.setText(Integer.toString(n));
                            SelectCountries.this.invalidateOptionsMenu();
                        }
                        Country country = (Country) cb.getTag();
                        country.setSelected(cb.isChecked());

                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Country country = countryList.get(position);
            holder.name.setText(country.getName());
            holder.name.setChecked(country.isSelected());
            holder.name.setTag(country);
            return convertView;
        }
    }

    public class InsertAsync extends AsyncTask<Void, Void, Void>{

        ProgressDialog p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(SelectCountries.this);
            p.setCancelable(false);
            p.setMessage("Creating committee");
            p.setIndeterminate(false);
            p.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //mydb.delDb(getApplicationContext());
            mydb.createTables(SelectCountries.this, cr);
            mydb.insertCommittee(name, 1, d);
            mydb.insertCountry(co, 1);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            p.dismiss();
        }
    }
}
