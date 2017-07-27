package com.munscore;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;
    private final int COMMITTEE_PERM =1 ;
    private DBHelper mydb;
    TextView name, comm;
    ArrayList<String> coun_names = new ArrayList<>();
    ListView lv;
    View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parentLayout = findViewById(android.R.id.content);
        mydb = new DBHelper(MainActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent com = new Intent(MainActivity.this, CommitteeActivity.class);
                startActivityForResult(com, COMMITTEE_PERM);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        lv = (ListView) findViewById(R.id.lv);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        name =  (TextView) findViewById(R.id.name);
        comm = (TextView) findViewById(R.id.committee);
        new GetDetails().execute();
        this.invalidateOptionsMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_addtext).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_create).setVisible(false);
        if(name.getText().toString().isEmpty()){
            Log.d("Name: ", name.getText().toString());
            Log.d("Hiding ", "item");
            menu.findItem(R.id.action_deltext).setVisible(false);
        }
        else{
            Log.d("Name: ", name.getText().toString());
            Log.d("Showing ", "item");
            menu.findItem(R.id.action_deltext).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_deltext){
            AlertDialog.Builder al = new AlertDialog.Builder(MainActivity.this);
            al.setMessage("Are you sure you want to delete this committee?");
            al.setCancelable(true);
            al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mydb.dropTab();
                    name.setText(null);
                    fab.setVisibility(View.VISIBLE);
                    name.setVisibility(View.GONE);
                    comm.setVisibility(View.VISIBLE);
                    lv.setVisibility(View.VISIBLE);
                    coun_names.clear();
                    Snackbar.make(parentLayout, "Committee deleted", Snackbar.LENGTH_LONG).show();
                    MainActivity.this.invalidateOptionsMenu();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == COMMITTEE_PERM){
            if(resultCode == RESULT_OK){
                Toast.makeText(MainActivity.this, "Committee created!", Toast.LENGTH_LONG).show();
                new GetDetails().execute();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class GetDetails extends AsyncTask<Void, Void, Void>{

        String nam;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Cursor rs = mydb.getComName();
                rs.moveToFirst();
                nam = rs.getString(rs.getColumnIndex(DBHelper.COMMITTEE_NAME));
                coun_names = mydb.getCountryName();
                if(coun_names != null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lv.setVisibility(View.VISIBLE);
                            lv.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, coun_names));
                        }
                    });
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lv.setVisibility(View.GONE);
                        }
                    });
                }
                if (!nam.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fab.setVisibility(View.GONE);
                            name.setVisibility(View.VISIBLE);
                            comm.setVisibility(View.GONE);
                            name.setText(nam);
                            MainActivity.this.invalidateOptionsMenu();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fab.setVisibility(View.VISIBLE);
                            name.setVisibility(View.GONE);
                            comm.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
            catch (SQLException f){
                f.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fab.setVisibility(View.VISIBLE);
                        name.setVisibility(View.GONE);
                        comm.setVisibility(View.VISIBLE);
                    }
                });
            }
            return null;
        }
    }
}
