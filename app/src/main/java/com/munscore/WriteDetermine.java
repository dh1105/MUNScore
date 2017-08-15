package com.munscore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class WriteDetermine extends AppCompatActivity implements View.OnClickListener {

    CheckBox dr, dir, chit, poi, poo;
    Button next_bun;
    private final int WRITE =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_determine);
        dr = (CheckBox) findViewById(R.id.dr);
        dir = (CheckBox) findViewById(R.id.dir);
        chit = (CheckBox) findViewById(R.id.chits);
        poi = (CheckBox) findViewById(R.id.poi);
        poo = (CheckBox) findViewById(R.id.poi);
        next_bun = (Button) findViewById(R.id.next_but);
        next_bun.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.next_but:
                String dref_res, direct, chit_m, p_o_i, p_o_o;
                if(dr.isChecked()){
                    dref_res = "YES";
                }
                else{
                    dref_res = "NO";
                }
                if(dir.isChecked()){
                    direct = "YES";
                }
                else{
                    direct = "NO";
                }
                if(chit.isChecked()){
                    chit_m = "YES";
                }
                else{
                    chit_m = "NO";
                }
                if(poi.isChecked()){
                    p_o_i = "YES";
                }
                else{
                    p_o_i = "NO";
                }
                if(poo.isChecked()){
                    p_o_o = "YES";
                }
                else{
                    p_o_o = "NO";
                }
                Intent i = getIntent();
                String name = i.getStringExtra("name");
                String day = i.getStringExtra("day");
                Intent in = new Intent(WriteDetermine.this, ParamClass.class);
                in.putExtra("chit", chit_m);
                in.putExtra("poi", p_o_i);
                in.putExtra("poo", p_o_o);
                in.putExtra("dir", direct);
                in.putExtra("dr", dref_res);
                in.putExtra("name", name);
                in.putExtra("day", day);
                startActivityForResult(in, WRITE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == WRITE){
            if(resultCode == RESULT_OK){
                Intent in = new Intent(WriteDetermine.this, CommitteeActivity.class);
                setResult(RESULT_OK, in);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
