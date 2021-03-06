package com.munscore;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class ResultDayThree extends Fragment {

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
        v = inflater.inflate(R.layout.resultdaythree, container, false);
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
        TextView total_speech = (TextView) v.findViewById(R.id.total_speech_3);
        int speech = mydb.getSpeechCount(name, 3);
        String [] b = mydb.getJudgeCol();
        ArrayList<String> j = new ArrayList<>();
        j.addAll(Arrays.asList(b).subList(3, b.length));
        float speech_score = mydb.getSpeechScore(name, j, 3);
        TextView res_speech_score = (TextView) v.findViewById(R.id.total_speech_score_3);
        res_speech_score.setText(String.valueOf(speech_score));
        total_speech.setText(String.valueOf(speech));
        if(mydb.isTableExist(POINT_OF_ORDER)){
            res_poo.setVisibility(View.VISIBLE);
            int poo = mydb.getPooCount(name , 3);
            float poo_score = mydb.getScore(POINT_OF_ORDER, name, 3);
            TextView total_poo = (TextView) v.findViewById(R.id.total_poo_3);
            TextView total_poo_score = (TextView) v.findViewById(R.id.total_poo_score_3);
            total_poo.setText(String.valueOf(poo));
            total_poo_score.setText(String.valueOf(poo_score));
        }
        if(mydb.isTableExist(POINT_OF_INFO)){
            res_poi.setVisibility(View.VISIBLE);
            int poi = mydb.getPoiCount(name , 3);
            float poi_score = mydb.getScore(POINT_OF_INFO, name, 3);
            TextView total_poi = (TextView) v.findViewById(R.id.total_poi_3);
            TextView total_poi_score = (TextView) v.findViewById(R.id.total_poi_score_3);
            total_poi.setText(String.valueOf(poi));
            total_poi_score.setText(String.valueOf(poi_score));
        }
        if(mydb.isTableExist(CHIT)){
            res_chit.setVisibility(View.VISIBLE);
            int chit = mydb.getChitCount(name , 3);
            float chit_score = mydb.getScore(CHIT, name, 3);
            TextView total_chit = (TextView) v.findViewById(R.id.total_chits_3);
            TextView total_chit_score = (TextView) v.findViewById(R.id.total_chit_score_3);
            total_chit.setText(String.valueOf(chit));
            total_chit_score.setText(String.valueOf(chit_score));
        }
        if(mydb.isTableExist(DIRECTIVE)){
            res_dir.setVisibility(View.VISIBLE);
            int dir = mydb.getDirCount(name , 3);
            float dir_score = mydb.getScore(DIRECTIVE, name, 3);
            TextView total_dir = (TextView) v.findViewById(R.id.total_dir_3);
            TextView total_dir_score = (TextView) v.findViewById(R.id.total_dir_score_3);
            total_dir.setText(String.valueOf(dir));
            total_dir_score.setText(String.valueOf(dir_score));
        }
        if(mydb.isTableExist(DR)){
            res_dr.setVisibility(View.VISIBLE);
            int dr = mydb.getDrCount(name , 3);
            float dr_score = mydb.getScore(DR, name, 3);
            TextView total_dr = (TextView) v.findViewById(R.id.total_dr_3);
            TextView total_dr_score = (TextView) v.findViewById(R.id.total_dr_score_3);
            total_dr.setText(String.valueOf(dr));
            total_dr_score.setText(String.valueOf(dr_score));
        }
    }
}
