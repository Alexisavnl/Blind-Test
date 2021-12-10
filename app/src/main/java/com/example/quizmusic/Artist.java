package com.example.quizmusic;


import java.io.Serializable;
import java.net.MalformedURLException;



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

    public void isSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean getSelected(){
        return selected;
    }


}
