package com.munscore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 7/20/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyDBName.db";
    private static final String COMMITTEE_TABLE_NAME = "committee";
    private static final String COUNTRY_TABLE_NAME = "country";
    private static final String JUDGE_TABLE_NAME = "judge";
    private static final String POINT_OF_INFO = "point_of_info";
    private static final String POINT_OF_ORDER = "point_of_order";
    private static final String CHIT = "chit";
    private static final String DR = "draft_reso";
    private static final String DIRECTIVE = "directive";
    private static final String RESULT = "result";
    public static final String COMMITTEE_NAME = "committee_name";
    //public static final String DAYS = "days";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*db.execSQL("create table if not exists" + COMMITTEE_TABLE_NAME + "(id integer primary key, committee_name text)");
        db.execSQL("create table if not exists" + COUNTRY_TABLE_NAME + "(com_id integer, country text)");*/
        //db.execSQL("create table" + JUDGE_TABLE_NAME + "(country_name text)");
    }

    boolean checkDataBase(Context context) {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

    void createTables(Context context, String[] countries) {
        try {
            SQLiteDatabase myDataBase = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_WORLD_WRITEABLE, null);
            myDataBase.execSQL("create table if not exists " + COMMITTEE_TABLE_NAME + "(id integer primary key, days integer, committee_name text)");
            myDataBase.execSQL("create table if not exists " + COUNTRY_TABLE_NAME + "(com_id integer, country text, foreign key (com_id) references " + COMMITTEE_TABLE_NAME +"(id))");
            String q;
            int i;
            q = countries[0] + " float,";
            for (i = 1; i < countries.length ; i++) {
                q += countries[i];
                q += " float";
                q += ", ";
            }
            //q += countries[i] + " integer,";
            q = "create table if not exists " + JUDGE_TABLE_NAME + "(speech_id integer primary key AUTOINCREMENT NOT NULL, day integer, country_name text, " + q + "foreign key (country_name) references " + COUNTRY_TABLE_NAME+ "(country))";
            myDataBase.execSQL(q);
            Log.d("Tables: ", myDataBase.toString());
            SQLiteDatabase db = getReadableDatabase();
            String tableString = String.format("Table %s:\n", JUDGE_TABLE_NAME);
            Cursor allRows  = db.rawQuery("SELECT * FROM " + JUDGE_TABLE_NAME, null);
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
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    void poiTable(Context context){
        try{
            SQLiteDatabase db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_WORLD_WRITEABLE, null);
            db.execSQL("create table if not exists " + POINT_OF_INFO + "(poi_id integer primary key AUTOINCREMENT NOT NULL, day integer, country_name text, score float, foreign key (country_name) references " + COUNTRY_TABLE_NAME+ "(country))");
            Log.d("Tables: ", db.toString());
            SQLiteDatabase db_one = getReadableDatabase();
            String tableString = String.format("Table %s:\n", POINT_OF_INFO);
            Cursor allRows  = db_one.rawQuery("SELECT * FROM " + POINT_OF_INFO, null);
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
        }
        catch(SQLiteException e){
            e.printStackTrace();
        }
    }

    void InsertTabScore(Context context, String country, String name, int day, float score){
        try{
            SQLiteDatabase dbname = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_WORLD_WRITEABLE, null);
            /*String INSERT="insert into "+ JUDGE_TABLE_NAME + "(country_name,"+titleString+"day)"+ " values" +"('" + name + "'," + markString + String.valueOf(day) + ")";
            System.out.println("Insert statement: "+INSERT);*/
            String INSERT = "insert into " + name + "(country_name, day, score) values ('" + country +"'," + String.valueOf(day) + "," + String.valueOf(score) + ")";
            System.out.println("Insert statement: "+INSERT);
            SQLiteStatement insertStmt = dbname.compileStatement(INSERT);
            insertStmt.executeInsert();
            SQLiteDatabase db = getReadableDatabase();
            String tableString = String.format("Table %s:\n", name);
            Cursor allRows  = db.rawQuery("SELECT * FROM " + name, null);
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
        }
        catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    void pooTable(Context context){
        try{
            SQLiteDatabase db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_WORLD_WRITEABLE, null);
            db.execSQL("create table if not exists " + POINT_OF_ORDER + "(poo_id integer primary key AUTOINCREMENT NOT NULL, day integer, country_name text, score float, foreign key (country_name) references " + COUNTRY_TABLE_NAME+ "(country))");
            Log.d("Tables: ", db.toString());
            SQLiteDatabase db_one = getReadableDatabase();
            String tableString = String.format("Table %s:\n", POINT_OF_ORDER);
            Cursor allRows  = db_one.rawQuery("SELECT * FROM " + POINT_OF_ORDER, null);
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
        }
        catch(SQLiteException e){
            e.printStackTrace();
        }
    }

    void chitTable(Context context){
        try{
            SQLiteDatabase db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_WORLD_WRITEABLE, null);
            db.execSQL("create table if not exists " + CHIT + "(chit_id integer primary key AUTOINCREMENT NOT NULL, day integer, country_name text, score float, foreign key (country_name) references " + COUNTRY_TABLE_NAME+ "(country))");
            Log.d("Tables: ", db.toString());
            SQLiteDatabase db_one = getReadableDatabase();
            String tableString = String.format("Table %s:\n", CHIT);
            Cursor allRows  = db_one.rawQuery("SELECT * FROM " + CHIT, null);
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
        }
        catch(SQLiteException e){
            e.printStackTrace();
        }
    }

    void drTable(Context context){
        try{
            SQLiteDatabase db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_WORLD_WRITEABLE, null);
            db.execSQL("create table if not exists " + DR + "(chit_id integer primary key AUTOINCREMENT NOT NULL, day integer, country_name text, score float, foreign key (country_name) references " + COUNTRY_TABLE_NAME+ "(country))");
            Log.d("Tables: ", db.toString());
            SQLiteDatabase db_one = getReadableDatabase();
            String tableString = String.format("Table %s:\n", DR);
            Cursor allRows  = db_one.rawQuery("SELECT * FROM " + DR, null);
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
        }
        catch(SQLiteException e){
            e.printStackTrace();
        }
    }

    void direcTable(Context context){
        try{
            SQLiteDatabase db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_WORLD_WRITEABLE, null);
            db.execSQL("create table if not exists " + DIRECTIVE + "(chit_id integer primary key AUTOINCREMENT NOT NULL, day integer, country_name text, score float, foreign key (country_name) references " + COUNTRY_TABLE_NAME+ "(country))");
            Log.d("Tables: ", db.toString());
            SQLiteDatabase db_one = getReadableDatabase();
            String tableString = String.format("Table %s:\n", DIRECTIVE);
            Cursor allRows  = db_one.rawQuery("SELECT * FROM " + DIRECTIVE, null);
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
        }
        catch(SQLiteException e){
            e.printStackTrace();
        }
    }

    boolean isTableExist(String name) {
        SQLiteDatabase mDatabase = this.getReadableDatabase();
        if(!mDatabase.isReadOnly()) {
            mDatabase.close();
            mDatabase = getReadableDatabase();
        }
        //Cursor cursor = mDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+name+"'", null);
        Cursor cursor = mDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='"+ name + "'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    int getSpeechCount(String name, int day){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("select count(*) from " + JUDGE_TABLE_NAME + " where country_name = '" + name + "' and day=" + String.valueOf(day), null);
        c.moveToFirst();
        int temp = c.getInt(0);
        Log.d("Value: ", String.valueOf(temp));
        return temp;
    }

    int getPooCount(String name, int day){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("select count(*) from " + POINT_OF_ORDER + " where country_name = '" + name + "' and day=" + String.valueOf(day), null);
        c.moveToFirst();
        int temp = c.getInt(0);
        Log.d("Value: ", String.valueOf(temp));
        return temp;
    }

    int getPoiCount(String name, int day){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("select count(*) from " + POINT_OF_INFO + " where country_name = '" + name + "' and day=" + String.valueOf(day), null);
        c.moveToFirst();
        int temp = c.getInt(0);
        Log.d("Value: ", String.valueOf(temp));
        return temp;
    }

    int getChitCount(String name, int day){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("select count(*) from " + CHIT + " where country_name = '" + name + "' and day=" + String.valueOf(day), null);
        c.moveToFirst();
        int temp = c.getInt(0);
        Log.d("Value: ", String.valueOf(temp));
        return temp;
    }

    int getDrCount(String name, int day){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("select count(*) from " + DR + " where country_name = '" + name + "' and day=" + String.valueOf(day), null);
        c.moveToFirst();
        int temp = c.getInt(0);
        Log.d("Value: ", String.valueOf(temp));
        return temp;
    }

    int getDirCount(String name, int day){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("select count(*) from " + DIRECTIVE + " where country_name = '" + name + "' and day=" + String.valueOf(day), null);
        c.moveToFirst();
        int temp = c.getInt(0);
        Log.d("Value: ", String.valueOf(temp));
        return temp;
    }

    private int getPoints(String a){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("select count(*) from " + a, null);
        c.moveToFirst();
        int temp = c.getInt(0);
        Log.d("Value: ", String.valueOf(temp));
        return temp;
    }

    private int getSpeech(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("select count(*) from " + JUDGE_TABLE_NAME , null);
        c.moveToFirst();
        int temp = c.getInt(0);
        Log.d("Value: ", String.valueOf(temp));
        return temp;
    }

    String[] getJudgeCol(){
        SQLiteDatabase m=getReadableDatabase();
        Cursor dbCursor = m.query(JUDGE_TABLE_NAME, null, null, null, null, null, null);
        String []a= dbCursor.getColumnNames();
        for (String anA : a) {
            Log.d("Column: ", anA);
        }
        return dbCursor.getColumnNames();
    }

    float getScore(String table_name, String name, int day){
        float score = 0.0f;
        try{
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("select sum(score) from " + table_name + " where country_name='" + name + "' and day=" + String.valueOf(day), null);
            c.moveToFirst();
            score = c.getFloat(0);
        }
        catch (SQLiteException e){
            e.printStackTrace();
        }
        return score;
    }

    boolean getResultQuery(){
        SQLiteDatabase mDatabase = this.getReadableDatabase();
        if(!mDatabase.isReadOnly()) {
            mDatabase.close();
            mDatabase = getReadableDatabase();
        }
        //Cursor cursor = mDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+name+"'", null);
        Cursor cursor = mDatabase.rawQuery("SELECT * from " + RESULT, null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    boolean insertCommittee(String name, int id, int day){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", id);
            contentValues.put("committee_name", name);
            Log.d("Temp ins: ", Integer.toString(day));
            contentValues.put("days", day);
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

    HashMap<String, Float> calFinalSpeechScore(Context context, ArrayList<String> coun, ArrayList<String> b){
        HashMap<String, Float> f = new HashMap<>();
        try{
            SQLiteDatabase db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_WORLD_WRITEABLE, null);
            int i;
            for(i=0; i<coun.size(); i++){
                Float score = 0.0f;
                for(int j=0; j<b.size(); j++) {
                    String query = "SELECT SUM(" + b.get(j) + ") from " + JUDGE_TABLE_NAME + " where country_name = '" + coun.get(i) + "'";
                    Cursor c = db.rawQuery(query, null);
                    if (c != null) {
                        c.moveToFirst();
                        do {
                            score = score + c.getFloat(0);
                            Log.d("Score: " + coun.get(i) + Integer.toString(j), Float.toString(score));
                        } while (c.moveToNext());
                    }
                    int speech_count = getSpeech();
                    Log.d(coun.get(i), Integer.toString(speech_count));
                    if(speech_count != 0){
                        f.put(coun.get(i), score/speech_count);
                    }
                    else{
                        f.put(coun.get(i), score);
                    }
                    Log.d("Hashmap: ", f.toString());
                }
            }
        }
        catch(SQLiteException e){
            e.printStackTrace();
        }
        Log.d("My map: ", f.toString());
        return f;
    }

    HashMap<String, Float> calPointScore(Context context, ArrayList<String> coun, String table_name){
        HashMap<String, Float> f = new HashMap<>();
        try{
            SQLiteDatabase db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_WORLD_WRITEABLE, null);
            int i;
            for(i=0; i<coun.size(); i++){
                Float score = 0.0f;
                String query = "SELECT SUM(score) from " + table_name + " where country_name = '" + coun.get(i) + "'";
                Cursor c = db.rawQuery(query, null);
                if (c != null) {
                    c.moveToFirst();
                    do {
                        score = score + c.getFloat(0);
                        Log.d("Score: " + coun.get(i), Float.toString(score));
                    } while (c.moveToNext());
                }
                int speech_count = getPoints(table_name);
                Log.d(coun.get(i), Integer.toString(speech_count));
                if(speech_count != 0){
                    f.put(coun.get(i), score/speech_count);
                }
                else{
                    f.put(coun.get(i), score);
                }
                Log.d("Hashmap: ", f.toString());
            }
        }
        catch(SQLiteException e){
            e.printStackTrace();
        }
        Log.d("My map: ", f.toString());
        return f;
    }

    void createResultTable(Context context){
        try{
            SQLiteDatabase db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_WORLD_WRITEABLE, null);
            db.execSQL("create table if not exists " + RESULT + "(result_id integer primary key AUTOINCREMENT NOT NULL, country_name text, score float)");
        }
        catch(SQLiteException e){
            e.printStackTrace();
        }
    }

    void insertResultTable(HashMap<String, Float> hmap, Context context){
        try{
            SQLiteDatabase db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_WORLD_WRITEABLE, null);
            ArrayList<String> countries;
            countries = getCountryName();
            for(String coun: hmap.keySet()){
                countries.add(coun);
            }
            ContentValues contentValues = new ContentValues();
            for(int i=0; i<hmap.size(); i++){
                contentValues.put("country_name", countries.get(i));
                contentValues.put("score", hmap.get(countries.get(i)));
                db.insert(RESULT, null, contentValues);
            }
            String tableString = String.format("Table %s:\n", RESULT);
            Cursor allRows  = db.rawQuery("SELECT * FROM " + RESULT, null);
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
        }
        catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    void insertScore(Context context, String [] a, String [] b, int day, String name){
        try{
            SQLiteDatabase myDataBase = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_WORLD_WRITEABLE, null);
            String titleString;
            String markString;
            int i;
            titleString = "";
            markString = "";
            Log.d("**createDynamicDatabase", "in oncreate");
            for(i=0;i<a.length;i++)
            {
                titleString += a[i];
                titleString +=",";
                markString += b[i];
                markString += ",";
            }


            String INSERT="insert into "+ JUDGE_TABLE_NAME + "(country_name,"+titleString+"day)"+ " values" +"('" + name + "'," + markString + String.valueOf(day) + ")";
            System.out.println("Insert statement: "+INSERT);
            SQLiteStatement insertStmt = myDataBase.compileStatement(INSERT);
            insertStmt.executeInsert();
            SQLiteDatabase db = getReadableDatabase();
            String tableString = String.format("Table %s:\n", JUDGE_TABLE_NAME);
            Cursor allRows  = db.rawQuery("SELECT * FROM " + JUDGE_TABLE_NAME, null);
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
        }
        catch(SQLException e){
            e.printStackTrace();
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
        Cursor c = db.rawQuery( "select committee_name from committee where id = 1", null );
        return c;
    }

    int getDays(){
        int temp=0;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor rs = db.rawQuery("select days from committee where id = 1",null);
            rs.moveToFirst();
            temp = rs.getInt(0);
            //db.close();
            Log.d("Temp db: ", Integer.toString(temp));
        }
        catch (SQLiteException e){
            e.printStackTrace();
        }
        return temp;
    }

    boolean dropTab(){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS committee");
            db.execSQL("DROP TABLE IF EXISTS country");
            db.execSQL("DROP TABLE IF EXISTS judge");
            db.execSQL("DROP TABLE IF EXISTS point_of_order");
            db.execSQL("DROP TABLE IF EXISTS point_of_info");
            db.execSQL("DROP TABLE IF EXISTS draft_reso");
            db.execSQL("DROP TABLE IF EXISTS chit");
            db.execSQL("DROP TABLE IF EXISTS directive");
            db.execSQL("DROP TABLE IF EXISTS result");
            db.close();
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
        db.execSQL("DROP TABLE IF EXISTS country");
        db.execSQL("DROP TABLE IF EXISTS judge");
        onCreate(db);
    }

    String[] getCol(String b) {
        SQLiteDatabase m=getReadableDatabase();
        Cursor dbCursor = m.query(b, null, null, null, null, null, null);
        String [] a= dbCursor.getColumnNames();
        for (String anA : a) {
            Log.d("Column: ", anA);
        }
        return dbCursor.getColumnNames();
    }

    public float getSpeechScore(String name, ArrayList<String> j, int i) {
        float score = 0.0f;
        try{
            SQLiteDatabase db = getReadableDatabase();
            for(int k=0; k<j.size(); k++){
                Cursor c = db.rawQuery("select sum(" + j.get(k) + ") from " + JUDGE_TABLE_NAME + " where country_name='" + name + "' and day = " + String.valueOf(i), null);
                c.moveToFirst();
                score = score + c.getFloat(0);
            }
        }
        catch (SQLiteException e){
            e.printStackTrace();
        }
        return score;
    }
}
