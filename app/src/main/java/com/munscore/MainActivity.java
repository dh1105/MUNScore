package com.munscore;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuInflater;
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
    private final int COMMITTEE_PERM=1;
    private DBHelper mydb;
    TextView name, comm;
    ArrayList<String> coun_names = new ArrayList<>();
    //ListView lv;
    DrawerLayout drawer;
    NavigationView navigationView;
    int day;
    View fabView;
    private static final String COMMITTEE_TABLE_NAME = "committee";
    private static final String RESULT = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mydb = new DBHelper(MainActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent com = new Intent(MainActivity.this, CommitteeActivity.class);
                startActivityForResult(com, COMMITTEE_PERM);
            }
        });
        fabView = findViewById(R.id.fab);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //lv = (ListView) findViewById(R.id.lv);
        name =  (TextView) findViewById(R.id.name);
        comm = (TextView) findViewById(R.id.committee);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(mydb.isTableExist(RESULT)){
            ResultCheck();
            Log.d("Exists: ", "Result");
            Intent in = new Intent(MainActivity.this, ResultFill.class);
            //finish();
            //in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(in, 6);
        }
        if(mydb.isTableExist(COMMITTEE_TABLE_NAME)){
            setNavDraw();
        }
        else {
            hideFrag();
        }
        this.invalidateOptionsMenu();
        navigationView.setCheckedItem(R.id.home);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, new Home()).commit();
        //new GetDetails().execute();
    }

    @Override
    protected void onResume() {
        if(mydb.isTableExist(RESULT)){
            ResultCheck();
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
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
        menu.findItem(R.id.action_home).setVisible(false);
        menu.findItem(R.id.action_deltext).setVisible(false);
        if(!mydb.isTableExist(COMMITTEE_TABLE_NAME)){
            menu.findItem(R.id.action_deltext).setVisible(false);
        }
        else{
            menu.findItem(R.id.action_deltext).setVisible(true);
        }
        return true;
    }

    public void hideFrag(){
        Log.d("Table: ", "Exists");
        fab.setVisibility(View.VISIBLE);
        Menu menuNav = navigationView.getMenu();
        MenuItem nav_item2 = menuNav.findItem(R.id.day_one);
        nav_item2.setEnabled(false);
        MenuItem nav_item3 = menuNav.findItem(R.id.day_two);
        nav_item3.setEnabled(false);
        MenuItem nav_item4 = menuNav.findItem(R.id.day_three);
        nav_item4.setEnabled(false);
        MenuItem nav_item5 = menuNav.findItem(R.id.results);
        nav_item5.setEnabled(false);
        MenuItem n = menuNav.findItem(R.id.stats);
        n.setEnabled(false);
    }

    public void ResultCheck(){
        Menu menuNav = navigationView.getMenu();
        MenuItem nav_item2 = menuNav.findItem(R.id.day_one);
        nav_item2.setEnabled(false);
        MenuItem nav_item3 = menuNav.findItem(R.id.day_two);
        nav_item3.setEnabled(false);
        MenuItem nav_item4 = menuNav.findItem(R.id.day_three);
        nav_item4.setEnabled(false);
        MenuItem nav_item5 = menuNav.findItem(R.id.results);
        nav_item5.setEnabled(true);
        MenuItem n = menuNav.findItem(R.id.stats);
        n.setEnabled(true);
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
                    //mydb.delDb(getApplicationContext());
                    mydb.dropTab();
                    hideFrag();
                    navigationView.setCheckedItem(R.id.home);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_frame, new Home()).commit();
                    //name.setVisibility(View.GONE);
                    //comm.setVisibility(View.VISIBLE);
                    //lv.setVisibility(View.GONE);
                    coun_names.clear();
                    Snackbar.make(fabView, "Committee deleted", Snackbar.LENGTH_LONG).show();
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

    public void setNavDraw(){
        //int temp = mydb.getDays();
        day = mydb.getDays();
        Log.d("Days: ", Integer.toString(day));
        fab.setVisibility(View.GONE);
        Menu menuNav=navigationView.getMenu();
        if(day==1) {
            MenuItem nav_item2 = menuNav.findItem(R.id.day_one);
            nav_item2.setEnabled(true);
            MenuItem nav_item3 = menuNav.findItem(R.id.day_two);
            nav_item3.setEnabled(false);
            MenuItem nav_item4 = menuNav.findItem(R.id.day_three);
            nav_item4.setEnabled(false);
        }
        else if(day==2){
            MenuItem nav_item2 = menuNav.findItem(R.id.day_one);
            nav_item2.setEnabled(true);
            MenuItem nav_item3 = menuNav.findItem(R.id.day_two);
            nav_item3.setEnabled(true);
            MenuItem nav_item4 = menuNav.findItem(R.id.day_three);
            nav_item4.setEnabled(false);
        }
        else if(day==3){
            MenuItem nav_item2 = menuNav.findItem(R.id.day_one);
            nav_item2.setEnabled(true);
            MenuItem nav_item3 = menuNav.findItem(R.id.day_two);
            nav_item3.setEnabled(true);
            MenuItem nav_item4 = menuNav.findItem(R.id.day_three);
            nav_item4.setEnabled(true);
        }
        MenuItem m = menuNav.findItem(R.id.results);
        m.setEnabled(true);
        MenuItem n = menuNav.findItem(R.id.stats);
        n.setEnabled(true);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        switch (id){
            case R.id.home:
                transaction.replace(R.id.content_frame, new Home()).commit();
                break;

            case R.id.day_one:
                transaction.replace(R.id.content_frame, new DayOne()).commit();
                break;

            case R.id.day_two:
                transaction.replace(R.id.content_frame, new DayTwo()).commit();
                break;

            case R.id.day_three:
                transaction.replace(R.id.content_frame, new DayThree()).commit();
                break;

            case R.id.results:
                Menu menuNav = navigationView.getMenu();
                MenuItem nav_item2 = menuNav.findItem(R.id.day_one);
                if(nav_item2.isEnabled()) {
                    AlertDialog.Builder al = new AlertDialog.Builder(MainActivity.this);
                    al.setMessage("Are you sure you want to calculate the final result? Doing so will not allow you to change/add any more scores.");
                    al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /*Menu menuNav = navigationView.getMenu();
                            MenuItem nav_item2 = menuNav.findItem(R.id.day_one);
                            nav_item2.setEnabled(false);
                            MenuItem nav_item3 = menuNav.findItem(R.id.day_two);
                            nav_item3.setEnabled(false);
                            MenuItem nav_item4 = menuNav.findItem(R.id.day_three);
                            nav_item4.setEnabled(false);*/
                            navigationView.setCheckedItem(R.id.home);
                            /*FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.content_frame, new Home()).commit();*/
                            ResultCheck();
                            Intent in = new Intent(MainActivity.this, ResultFill.class);
                            //finish();
                            //in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityForResult(in, 6);
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.content_frame, new Home()).commit();
                        }
                    });
                    al.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            navigationView.setCheckedItem(R.id.home);
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.content_frame, new Home()).commit();
                        }
                    });
                    al.setCancelable(false);
                    al.show();
                }
                else{
                    navigationView.setCheckedItem(R.id.home);
                    ResultCheck();
                    Intent in = new Intent(MainActivity.this, ResultFill.class);
                    //finish();
                    //in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(in, 6);
                }
                break;

            case R.id.stats:
                Intent in = new Intent(MainActivity.this, CommitteeStats.class);
                startActivityForResult(in, 5);
                break;
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
                setNavDraw();
                this.invalidateOptionsMenu();
                navigationView.setCheckedItem(R.id.home);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, new Home()).commit();
            }
        }
        if(requestCode == 5){
            if(resultCode == RESULT_OK){
                navigationView.setCheckedItem(R.id.home);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, new Home()).commit();
            }
        }
        if(requestCode == 6){
            if(resultCode == RESULT_OK){
                Log.d("Deleted main", "Comm");
                Log.d("Starting", "Main");
                navigationView.setCheckedItem(R.id.home);
                recreate();
                /*hideFrag();
                //this.invalidateOptionsMenu();
                //navigationView.setCheckedItem(R.id.home);
                navigationView.setCheckedItem(R.id.home);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, new Home()).commit();
                coun_names.clear();
                Snackbar.make(fabView, "Committee deleted", Snackbar.LENGTH_LONG).show();
                MainActivity.this.invalidateOptionsMenu();*/
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
