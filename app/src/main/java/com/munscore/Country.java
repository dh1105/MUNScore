package com.munscore;

/**
 * Created by user on 7/19/2017.
 */

public class Country {

    String name = null;
    boolean selected = false;

    public Country(String name, boolean selected) {
        super();
        this.name = name;
        this.selected = selected;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
