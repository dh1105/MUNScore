package com.munscore;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 8/19/2017.
 */

public class CustomNameScoreAdapter extends ArrayAdapter<NameAndScore> {

    private Context context;

    public CustomNameScoreAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<NameAndScore> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    private class ViewHolder{
        TextView coun_name;
        TextView score;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
        NameAndScore nameAndScore = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.score_detail, null);
            holder = new ViewHolder();
            holder.coun_name = (TextView) convertView.findViewById(R.id.coun_sc_name);
            holder.score = (TextView) convertView.findViewById(R.id.coun_sc_score);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();
        holder.coun_name.setText(nameAndScore.getCounName());
        holder.score.setText(Float.toString(nameAndScore.getScore()));
        return convertView;
    }
}
