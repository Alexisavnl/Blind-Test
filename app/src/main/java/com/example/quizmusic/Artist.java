package com.example.quizmusic;


import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;



public class Artist implements Serializable{


    private int id;

    private String name;

    private String picture_small;

    private int nb_fan;
    private boolean selected = false;

    public Artist(int id, String name, String picture_small, int nb_fan) throws MalformedURLException {
        this.id = id;
        this.name = name;
        this.picture_small = picture_small;
        this.nb_fan = nb_fan;
        System.out.println(id+name+picture_small+nb_fan);
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public String getURLPicture() {
        return picture_small;
    }

    public int getNb_fan() {
        return nb_fan;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPicture_small(String picture_small) {
        this.picture_small = picture_small;
    }

    public void setNb_fan(int nb_fan) {
        this.nb_fan = nb_fan;
    }

    public void isSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean getSelected(){
        return selected;
    }


}
