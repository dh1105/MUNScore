package com.munscore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 7/20/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyDBName.db";
    private static final String COMMITTEE_TABLE_NAME = "committee";
    private static final String COUNTRY_TABLE_NAME = "country";
    private static final String JUDGE_TABLE_NAME = "judge";
    public static final String COMMITTEE_NAME = "committee_name";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*db.execSQL("create table if not exists" + COMMITTEE_TABLE_NAME + "(id integer primary key, committee_name text)");
        db.execSQL("create table if not exists" + COUNTRY_TABLE_NAME + "(com_id integer, country text)");*/
        //db.execSQL("create table" + JUDGE_TABLE_NAME + "(country_name text)");
    }

    void createTables(Context context, String[] countries) {
        try {
            SQLiteDatabase myDataBase = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_WORLD_WRITEABLE, null);
            myDataBase.execSQL("create table if not exists " + COMMITTEE_TABLE_NAME + "(id integer primary key, committee_name text)");
            myDataBase.execSQL("create table if not exists " + COUNTRY_TABLE_NAME + "(com_id integer, country text, foreign key (com_id) references " + COMMITTEE_TABLE_NAME +"(id))");
            String q;
            int i;
            q = countries[0] + " integer,";
            for (i = 1; i < countries.length ; i++) {
                q += countries[i];
                q += " integer";
                q += ", ";
            }
            //q += countries[i] + " integer,";
            q = "create table if not exists " + JUDGE_TABLE_NAME + "(country_name text primary key, " + q + "score integer, foreign key (country_name) references " + COUNTRY_TABLE_NAME+ "(country))";
            myDataBase.execSQL(q);
            Log.d("Tables: ", myDataBase.toString());
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    boolean insertCommittee(String name, int id){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", id);
            contentValues.put("committee_name", name);
            db.insert(COMMITTEE_TABLE_NAME, null, contentValues);
            String tableString = String.format("Table %s:\n", COMMITTEE_TABLE_NAME);
            Cursor allRows  = db.rawQuery("SELECT * FROM " + COMMITTEE_TABLE_NAME, null);
            if (allRows.moveToFirst() ){
                String[] columnNames = allRows.getColumnNames();
                do {
                    for (String vname: columnNames) {
                        tableString += String.format("%s: %s\n", vname,
                                allRows.getString(allRows.getColumnIndex(vname)));
                    }
                    tableString += "\n";

                } while (allRows.moveToNext());
            }
            Log.d("Rows comm: ", tableString);
            return true;
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    ArrayList<String> getCountryName(){
        try{
            ArrayList<String> co = new ArrayList<>();
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery("select country from country where com_id = 1", null);
            if(c != null){
                if(c.moveToFirst()){
                    do {
                        String coun_name = c.getString(c.getColumnIndex("country"));
                        Log.d("Country: ", coun_name);
                        co.add(coun_name);
                    }while (c.moveToNext());
                }
                return co;
            }
            else{
                return null;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    boolean insertCountry(List<String> a, int com_id){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            for(int i=0; i<a.size(); i++){
                contentValues.put("com_id", com_id);
                contentValues.put("country", a.get(i));
                db.insert(COUNTRY_TABLE_NAME, null, contentValues);
            }
            String tableString = String.format("Table %s:\n", COUNTRY_TABLE_NAME);
            Cursor allRows  = db.rawQuery("SELECT * FROM " + COUNTRY_TABLE_NAME, null);
            if (allRows.moveToFirst() ){
                String[] columnNames = allRows.getColumnNames();
                do {
                    for (String name: columnNames) {
                        tableString += String.format("%s: %s\n", name,
                                allRows.getString(allRows.getColumnIndex(name)));
                    }
                    tableString += "\n";

                } while (allRows.moveToNext());
            }
            Log.d("Rows country: ", tableString);
            return true;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    Cursor getComName(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select committee_name from committee where id = 1", null );
        return res;
    }

    boolean dropTab(){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS committee");
            db.execSQL("DROP TABLE IF EXISTS country");
            db.execSQL("DROP TABLE IF EXISTS judge");
            return true;
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS committee");
        onCreate(db);
    }
}
