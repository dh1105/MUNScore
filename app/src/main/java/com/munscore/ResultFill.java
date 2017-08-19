package com.munscore;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultFill extends AppCompatActivity implements View.OnClickListener {

    TextView best_del, high_comm, spec_men;
    Button cal_res;
    DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_fill);
        mydb = new DBHelper(getApplicationContext());
        mydb.createResultTable(getApplicationContext());
        best_del = (TextView) findViewById(R.id.best_del);
        high_comm = (TextView) findViewById(R.id.high_comm);
        spec_men = (TextView) findViewById(R.id.spec_men);
        cal_res = (Button) findViewById(R.id.cal_res);
        cal_res.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cal_res:
                String best = best_del.getText().toString();
                String high = high_comm.getText().toString();
                String spec = spec_men.getText().toString();
                DBHelper  mydb = new DBHelper(getApplicationContext());
                ArrayList<String> coun_names = mydb.getCountryName();
                if(Integer.parseInt(best) + Integer.parseInt(high) + Integer.parseInt(spec) > coun_names.size()){
                    Snackbar.make(v, "Please ensure that the total awards are not more than the countries in the committee", Snackbar.LENGTH_LONG).show();
                }
                else if(!best.isEmpty() && !high.isEmpty() && !spec.isEmpty()) {
                    Intent in = new Intent(ResultFill.this, CalculateResult.class);
                    in.putExtra("best_del", best);
                    in.putExtra("high", high);
                    in.putExtra("spec", spec);
                    in.putExtra("empty", "res");
                    //finish();
                    //in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(in, 6);
                }
                else{
                    Snackbar.make(v, "Please fill out all fields. In case of no award write 0", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 6){
            if(resultCode == RESULT_OK){
                Log.d("Deleted", "Comm");
                //this.invalidateOptionsMenu();
                //navigationView.setCheckedItem(R.id.home);
                Intent in = new Intent(ResultFill.this, MainActivity.class);
                setResult(RESULT_OK, in);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*@Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }*/
}
