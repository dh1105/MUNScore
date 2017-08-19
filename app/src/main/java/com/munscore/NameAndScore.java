package com.munscore;

/**
 * Created by user on 8/19/2017.
 */

public class NameAndScore {

    private float score;
    String name;

    NameAndScore(float score, String name){
        this.score = score;
        this.name = name;
    }

    float getScore(){
        return this.score;
    }

    public void setScore(float score){
        this.score = score;
    }

    String getCounName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }
}
