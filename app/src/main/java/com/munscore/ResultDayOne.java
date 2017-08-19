package com.munscore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by user on 8/19/2017.
 */

public class ResultDayOne extends Fragment {

    View v;
    DBHelper mydb;
    private static final String POINT_OF_INFO = "point_of_info";
    private static final String POINT_OF_ORDER = "point_of_order";
    private static final String CHIT = "chit";
    private static final String DR = "draft_reso";
    private static final String DIRECTIVE = "directive";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.resultdayone, container, false);
        mydb = new DBHelper(getActivity());
        Bundle args = getArguments();
        String name = (String) args.get("name");
        Log.d("name", name);
        setDetails(name);
        return v;
    }

    private void setDetails(String name) {
        LinearLayout res_poo = (LinearLayout) v.findViewById(R.id.res_poo);
        LinearLayout res_poi = (LinearLayout) v.findViewById(R.id.res_poi);
        LinearLayout res_chit = (LinearLayout) v.findViewById(R.id.res_chit);
        LinearLayout res_dir = (LinearLayout) v.findViewById(R.id.res_dir);
        LinearLayout res_dr = (LinearLayout) v.findViewById(R.id.res_dr);
        TextView total_speech = (TextView) v.findViewById(R.id.total_speech);
        int speech = mydb.getSpeechCount(name, 1);
        String [] b = mydb.getJudgeCol();
        ArrayList<String> j = new ArrayList<>();
        j.addAll(Arrays.asList(b).subList(3, b.length));
        float speech_score = mydb.getSpeechScore(name, j, 1);
        TextView res_speech_score = (TextView) v.findViewById(R.id.total_speech_score);
        res_speech_score.setText(String.valueOf(speech_score));
        total_speech.setText(String.valueOf(speech));
        if(mydb.isTableExist(POINT_OF_ORDER)){
            res_poo.setVisibility(View.VISIBLE);
            int poo = mydb.getPooCount(name , 1);
            float poo_score = mydb.getScore(POINT_OF_ORDER, name, 1);
            TextView total_poo = (TextView) v.findViewById(R.id.total_poo);
            TextView total_poo_score = (TextView) v.findViewById(R.id.total_poo_score);
            total_poo.setText(String.valueOf(poo));
            total_poo_score.setText(String.valueOf(poo_score));
        }
        if(mydb.isTableExist(POINT_OF_INFO)){
            res_poi.setVisibility(View.VISIBLE);
            int poi = mydb.getPoiCount(name , 1);
            float poi_score = mydb.getScore(POINT_OF_INFO, name, 1);
            TextView total_poi = (TextView) v.findViewById(R.id.total_poi);
            TextView total_poi_score = (TextView) v.findViewById(R.id.total_poi_score);
            total_poi.setText(String.valueOf(poi));
            total_poi_score.setText(String.valueOf(poi_score));
        }
        if(mydb.isTableExist(CHIT)){
            res_chit.setVisibility(View.VISIBLE);
            int chit = mydb.getChitCount(name , 1);
            float chit_score = mydb.getScore(CHIT, name, 1);
            TextView total_chit = (TextView) v.findViewById(R.id.total_chits);
            TextView total_chit_score = (TextView) v.findViewById(R.id.total_chit_score);
            total_chit.setText(String.valueOf(chit));
            total_chit_score.setText(String.valueOf(chit_score));
        }
        if(mydb.isTableExist(DIRECTIVE)){
            res_dir.setVisibility(View.VISIBLE);
            int dir = mydb.getDirCount(name , 1);
            float dir_score = mydb.getScore(DIRECTIVE, name, 1);
            TextView total_dir = (TextView) v.findViewById(R.id.total_dir);
            TextView total_dir_score = (TextView) v.findViewById(R.id.total_dir_score);
            total_dir.setText(String.valueOf(dir));
            total_dir_score.setText(String.valueOf(dir_score));
        }
        if(mydb.isTableExist(DR)){
            res_dr.setVisibility(View.VISIBLE);
            int dr = mydb.getDrCount(name , 1);
            float dr_score = mydb.getScore(DR, name, 1);
            TextView total_dr = (TextView) v.findViewById(R.id.total_dr);
            TextView total_dr_score = (TextView) v.findViewById(R.id.total_dr_score);
            total_dr.setText(String.valueOf(dr));
            total_dr_score.setText(String.valueOf(dr_score));
        }
    }

}
