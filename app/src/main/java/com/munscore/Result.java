package com.munscore;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by user on 8/9/2017.
 */

public class Result extends Fragment implements View.OnClickListener {

    View v;
    TextView best_del, high_comm, spec_men;
    Button cal_res;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.result, container, false);
        best_del = (TextView) v.findViewById(R.id.best_del);
        high_comm = (TextView) v.findViewById(R.id.high_comm);
        spec_men = (TextView) v.findViewById(R.id.spec_men);
        cal_res = (Button) v.findViewById(R.id.cal_res);
        cal_res.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cal_res:
                String best = best_del.getText().toString();
                String high = high_comm.getText().toString();
                String spec = spec_men.getText().toString();
                if(!best.isEmpty() && !high.isEmpty() && !spec.isEmpty()) {
                    Intent in = new Intent(getActivity(), CalculateResult.class);
                    in.putExtra("best_del", best);
                    in.putExtra("high", high);
                    in.putExtra("spec", spec);
                    startActivity(in);
                }
                else{
                    Snackbar.make(v, "Please fill out all fields. In case of no award write 0", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }
}
